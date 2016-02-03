/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.apache.shindig.social.sample.spi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import org.apache.commons.io.IOUtils;
import org.apache.shindig.common.SecurityToken;
import org.apache.shindig.common.util.ImmediateFuture;
import org.apache.shindig.common.util.ResourceLoader;
import org.apache.shindig.social.ResponseError;
import org.apache.shindig.social.ResponseItem;
import org.apache.shindig.social.opensocial.model.Activity;
import org.apache.shindig.social.opensocial.model.Person;
import org.apache.shindig.social.opensocial.service.BeanConverter;
import org.apache.shindig.social.opensocial.spi.ActivityService;
import org.apache.shindig.social.opensocial.spi.AppDataService;
import org.apache.shindig.social.opensocial.spi.DataCollection;
import org.apache.shindig.social.opensocial.spi.GroupId;
import org.apache.shindig.social.opensocial.spi.PersonService;
import org.apache.shindig.social.opensocial.spi.RestfulCollection;
import org.apache.shindig.social.opensocial.spi.UserId;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Implementation of supported services backed by a JSON DB.
 */
@Singleton
public class JsonDbOpensocialService implements ActivityService, PersonService,
		AppDataService {

	private SpiceDBLayer spiceDBLayer = SpiceDBLayer.get();
	private static final Comparator<Person> NAME_COMPARATOR = new Comparator<Person>() {
		public int compare(Person person, Person person1) {
			String name = person.getName().getUnstructured();
			String name1 = person1.getName().getUnstructured();
			return name.compareTo(name1);
		}
	};

	/**
	 * The JSON<->Bean converter
	 */
	private JSONObject db;

	/**
	 * The JSON<->Bean converter
	 */
	private BeanConverter converter;



	@Inject
	  public JsonDbOpensocialService(@Named("shindig.canonical.json.db")String jsonLocation,
	      @Named("shindig.bean.converter.json")BeanConverter converter) throws Exception {
	    String content = IOUtils.toString(ResourceLoader.openResource(jsonLocation), "UTF-8");
	    this.db = new JSONObject(content);
	    this.converter = converter;
	  }

	public JSONObject getDb() {
		return db;
	}

	public void setDb(JSONObject db) {
		this.db = db;
	}

	public Future<ResponseItem<RestfulCollection<Activity>>> getActivities(
			Set<UserId> userIds, GroupId groupId, String appId,
			Set<String> fields, SecurityToken token) {

		List<String> list = new ArrayList<String>();
		Set<String> idSet = null;
		try {
			idSet = getIdSet(userIds, groupId, token);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		for (String userId : idSet) {
			list.add(userId);
		}

		List<Activity> userActivityList = spiceDBLayer.getActivities(list);
		return ImmediateFuture
				.newInstance(new ResponseItem<RestfulCollection<Activity>>(
						new RestfulCollection<Activity>(userActivityList)));
	}

	public Future<ResponseItem<RestfulCollection<Activity>>> getActivities(
			UserId userId, GroupId groupId, String appId, Set<String> fields,
			Set<String> activityIds, SecurityToken token) {

		List<String> list = new ArrayList<String>();
		list.add(userId.getUserId(token));
		List<Activity> userActivityList = spiceDBLayer.getActivities(list);
		return ImmediateFuture
				.newInstance(new ResponseItem<RestfulCollection<Activity>>(
						new RestfulCollection<Activity>(userActivityList)));

	}

	public Future<ResponseItem<Activity>> getActivity(UserId userId,
			GroupId groupId, String appId, Set<String> fields,
			String activityId, SecurityToken token) {

		List<String> list = new ArrayList<String>();
		list.add(userId.getUserId(token));
		List<Activity> userActivityList = spiceDBLayer.getActivities(list);
		for (Activity activity : userActivityList) {
			if (activity.getId().equalsIgnoreCase(activityId)) {
				return ImmediateFuture.newInstance(new ResponseItem<Activity>(
						activity));
			}
		}
		return null;

	}

	public Future<ResponseItem<Object>> deleteActivities(UserId userId,
			GroupId groupId, String appId, Set<String> activityIds,
			SecurityToken token) {

		List<String> list = new ArrayList<String>();
		list.add(userId.getUserId(token));
		return null;

	}

	public Future<ResponseItem<Object>> createActivity(UserId userId,
			GroupId groupId, String appId, Set<String> fields,
			Activity activity, SecurityToken token) {

		activity.setUserId(userId.getUserId(token));
		activity.setPostedTime(System.currentTimeMillis());
		try {
			spiceDBLayer.createActivity(userId.getUserId(token), activity,
					appId);
			return ImmediateFuture.newInstance(new ResponseItem<Object>(""));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Future<ResponseItem<RestfulCollection<Person>>> getPeople(
			Set<UserId> userIds, GroupId groupId, SortOrder sortOrder,
			FilterType filter, int first, int max, Set<String> fields,
			SecurityToken token) {

		Set<String> idSet = null;
		try {
			idSet = getIdSet(userIds, groupId, token);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, Person> allPeople = spiceDBLayer
				.getPeople(convertSetToLoist(idSet));
		List<Person> result = new ArrayList<Person>();
		for (String id : convertSetToLoist(idSet)) {
			Person person = allPeople.get(id);
			if (id.equals(token.getViewerId())) {
				person.setIsViewer(true);
			}
			if (id.equals(token.getOwnerId())) {
				person.setIsOwner(true);
			}
			result.add(person);
		}
		if (sortOrder.equals(PersonService.SortOrder.name)) {
			Collections.sort(result, NAME_COMPARATOR);
		}

		int totalSize = result.size();
		int last = first + max;
		result = result.subList(first, Math.min(last, totalSize));

		RestfulCollection<Person> collection = new RestfulCollection<Person>(
				result, first, totalSize);

		return ImmediateFuture
				.newInstance(new ResponseItem<RestfulCollection<Person>>(
						collection));

	}

	public Future<ResponseItem<Person>> getPerson(UserId id,
			Set<String> fields, SecurityToken token) {

		List<String> list = new ArrayList<String>();
		list.add(id.getUserId(token));
		Map<String, Person> allPeople = spiceDBLayer.getPeople(list);
		Person person = allPeople.get(id.getUserId(token));
		return ImmediateFuture.newInstance(new ResponseItem<Person>(person));
	}

	public Future<ResponseItem<DataCollection>> getPersonData(
			Set<UserId> userIds, GroupId groupId, String appId,
			Set<String> fields, SecurityToken token) {

		try {
			Set<String> idSet = getIdSet(userIds, groupId, token);
			Map<String, Map<String, String>> idToData = spiceDBLayer
					.getPersonAppData(convertSetToLoist(idSet),
							convertSetToLoist(fields));

			return ImmediateFuture
					.newInstance(new ResponseItem<DataCollection>(
							new DataCollection(idToData)));
		} catch (JSONException je) {
			return ImmediateFuture
					.newInstance(new ResponseItem<DataCollection>(
							ResponseError.INTERNAL_ERROR, je.getMessage(), null));
		}

	}

	public Future<ResponseItem<Object>> deletePersonData(UserId userId,
			GroupId groupId, String appId, Set<String> fields,
			SecurityToken token) {

		String user = userId.getUserId(token);
		for (String key : fields) {
			spiceDBLayer.setAppData(user, key, null, appId);
		}

		return ImmediateFuture.newInstance(new ResponseItem<Object>(""));

	}

	public Future<ResponseItem<Object>> updatePersonData(UserId userId,
			GroupId groupId, String appId, Set<String> fields,
			Map<String, String> values, SecurityToken token) {

		String user = userId.getUserId(token);
		Set<Map.Entry<String, String>> entrySet = values.entrySet();
		Set<String> keySet = values.keySet();
		for (String key : keySet) {
			String value = values.get(key);
			spiceDBLayer.setAppData(user, key, value, appId);
		}
		for (String key : fields) {
			String value = values.get(key);
			spiceDBLayer.setAppData(user, key, value, appId);
		}
		return ImmediateFuture.newInstance(new ResponseItem<Object>(""));

	}

	/**
	 * Get the set of user id's from a user and group
	 */
	private Set<String> getIdSet(UserId user, GroupId group, SecurityToken token)
			throws JSONException {
		
		
		String userId = user.getUserId(token);

		if (group == null) {
			Set<String> set = new HashSet<String>();
			set.add(userId);
			return set;
		}

		Set<String> set = new HashSet<String>();
		switch (group.getType()) {
		case all:
		case friends:
		case groupId:
			List<String> friendList = spiceDBLayer.getFriendsIds(userId);

			for (String friendId : friendList) {
				set.add(friendId);
			}
			break;
		case self:
			set.add(userId);
			break;
		}
		return set;
	}

	/**
	 * Get the set of user id's for a set of users and a group
	 */
	private Set<String> getIdSet(Set<UserId> users, GroupId group,
			SecurityToken token) throws JSONException {
		Set<String> ids = new HashSet<String>();
		for (UserId user : users) {
			ids.addAll(getIdSet(user, group, token));
		}
		return ids;
	}

	

	private List<String> convertSetToLoist(Set<String> set) {
		List<String> list = new ArrayList<String>();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			String id = (String) it.next();
			list.add(id);
		}
		return list;

	}
}
