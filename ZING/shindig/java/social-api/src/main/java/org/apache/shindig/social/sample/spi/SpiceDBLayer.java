package org.apache.shindig.social.sample.spi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shindig.social.core.model.ActivityImpl;
import org.apache.shindig.social.core.model.EnumImpl;
import org.apache.shindig.social.core.model.MediaItemImpl;
import org.apache.shindig.social.core.model.NameImpl;
import org.apache.shindig.social.core.model.PersonImpl;
import org.apache.shindig.social.opensocial.model.Activity;
import org.apache.shindig.social.opensocial.model.Enum;
import org.apache.shindig.social.opensocial.model.MediaItem;
import org.apache.shindig.social.opensocial.model.Person;


public class SpiceDBLayer {
	String DB_URL ="jdbc:mysql://192.168.205.15:3306/zing";
	String DB_UID ="zing";// DB USERNAME
	String DB_PW = "zingadmin";// DB PASSWORD
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL_PREFIX = "http://localhost:8080/newspice";
	private static SpiceDBLayer dataLayer;

	public static SpiceDBLayer get() {
		if (dataLayer == null) {
			dataLayer = new SpiceDBLayer();
		}
		return dataLayer;
	}

	public List<String> getFriendsIds(String personId) {

		List<String> friends = new ArrayList<String>();
		try {
			// Execute query to get friends of person with id personID
			int personID = Integer.parseInt(personId);
			String query = "select user_id, friend_id from user_friend where user_id='"
					+ personID + "'" + "or friend_id ='" + personID + "'";
			ResultSet rs = executeSQLQuery(query);

			while (rs.next()) {

				int person_id = rs.getInt("user_id");
				int friend_id = rs.getInt("friend_id");
				int friId = (person_id == personID) ? friend_id : person_id;
				String friendID = "" + friId;
				friends.add(friendID);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return friends;
	}

	public Map<String, Person> getPeople(List<String> ids) {

		Map<String, Person> allPeople = new HashMap<String, Person>();
		// select * from user_profile where id
		String query = "select * from user_profile";

		boolean firstTime = true;

		for (String idString : ids) {
			int id = Integer.parseInt(idString);
			if (firstTime) {
				query += " where user_id = " + id;
			} else {
				query += " or user_id = " + id;
			}
			firstTime = false;
		}

		try {
			ResultSet rs = executeSQLQuery(query);

			// loop through people and add to allPeople map
			while (rs.next()) {

				int id = rs.getInt("user_id");
				String fName = rs.getString("first_name");
				String lName = rs.getString("last_name");
				String fullName = fName + " " + lName;
				NameImpl name = new NameImpl(fullName);
				name.setGivenName(fName);
				name.setFamilyName(lName);
				String personId = "" + id;
				PersonImpl person = new PersonImpl(personId, name);
				person.setProfileUrl(URL_PREFIX + "/profile/" + id);
				String gender = rs.getString("Gender");
				if(gender!=null){
				if(gender.equalsIgnoreCase("m")){
					gender="MALE";
				}else
				{
					gender="FEMALE";
				}
			   if ("FEMALE".equalsIgnoreCase(gender)) {
	           person.setGender(new EnumImpl<Enum.Gender>(Enum.Gender.FEMALE));
			   }else{
				   person.setGender(new EnumImpl<Enum.Gender>(Enum.Gender.MALE)) ;
			   }
			}

				person.setThumbnailUrl(rs.getString("user_image"));

				allPeople.put(personId, person);
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allPeople;
	}

	public void createActivity(String personId, Activity activity, String appId)
			throws SQLException, ClassNotFoundException {

		// appId="0";
		String title = activity.getTitle();
		title = cleanForSql(title);
		title = escapeForHtml(title);

		String body = activity.getBody();
		body = cleanForSql(body);
		body = escapeForHtml(body);
		Integer newActivityId = null;
		List<MediaItem> items = activity.getMediaItems();
		int time = (int) new Date().getTime();
		int userId = Integer.parseInt(personId);
		int appID = 0;

		ResultSet rs = executeSQLUpdate("insert into activities (user_id, app_id, title, body, created) values ("
				+ userId
				+ ", "
				+ appID
				+ ",'"
				+ title
				+ "','"
				+ body
				+ "',"
				+ time + ");");

		ResultSet keys = rs.getStatement().getGeneratedKeys();

		while (keys.next()) {
			newActivityId = keys.getInt(1);
		}
		keys.close();
		rs.close();

		if (items != null) {
			for (MediaItem item : items) {
				String mimetype = item.getMimeType();
				String url = item.getUrl();
				MediaItem.Type type = item.getType();
				rs = executeSQLUpdate("insert into activity_media_items (activity_id, mime_type, media_type, url) values ("
						+ type
						+ "','"
						+ mimetype
						+ "','"
						+ url
						+ "',"
						+ newActivityId + ");");
			}
			rs.close();

		}
	}

	public List<Activity> getActivities(List<String> ids) {
		List<Activity> allActivities = new ArrayList<Activity>();
		for (String idString : ids) {
			int id = Integer.parseInt(idString);
			String query = "select id,user_id,title,body,created from activities where user_id="
					+ id + " order by created desc";
			try {
				ResultSet rs = executeSQLQuery(query);

				while (rs.next()) {
					int personid = rs.getInt("user_id");
					int actvityid = rs.getInt("id");
					ActivityImpl activityImpl = new ActivityImpl(
							"" + actvityid, "" + personid);
					activityImpl.setBody(rs.getString("body"));
					activityImpl.setStreamTitle("activities");
					activityImpl.setTitle(rs.getString("title"));
					long time = (long) rs.getInt("created");
					activityImpl.setPostedTime(time);
					activityImpl.setMediaItems(getMediaItems(actvityid));
					allActivities.add(activityImpl);
				}
				rs.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return allActivities;
	}

	private List<MediaItem> getMediaItems(int actvityid) {
		List<MediaItem> media = new ArrayList<MediaItem>();
		String query = "select mime_type, media_type, url from activity_media_items where activity_id ="
				+ actvityid;
		ResultSet rs;
		try {
			rs = executeSQLQuery(query);
			while (rs.next()) {
				String typeString = rs.getString("media_type");
				String mimeType = rs.getString("mime_type");
				String url = rs.getString("url");
				media.add(new MediaItemImpl(mimeType, MediaItem.Type
						.valueOf(typeString), url));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return media;
	}

	public void setAppData(String person_id, String key, String value,
			String app_id) {
		int application_Id = Integer.parseInt(app_id);
		int person_ID = Integer.parseInt(person_id);
		String query = null;
		if (value == null) {
			query = "delete from application_settings where application_id ="
					+ application_Id + " and user_id =" + person_ID
					+ " and name = " + key;
		} else {
			query = "insert into application_settings (application_id, user_id, module_id, name, value) values("
					+ application_Id
					+ ","
					+ person_ID
					+ ","
					+ 0
					+ ",'"
					+ key
					+ "','"
					+ value
					+ "'"
					+ ") on duplicate key update value ='" + value + "'";
		}
		try {
			executeSQLUpdate(query);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	//	public Map<String, Map<String, List <String>>> getAppData(List<String> ids, List<String> keys, String app_id) {
	//
	//		  if (null == ids){
	//			return null;
	//		  }
	//		  Map<String, List <String>> personData = new HashMap<String,List <String>>();
	//		  Map<String, Map<String, List <String>>> finalAppDataResults = new HashMap<String,Map<String, List <String>>>();
	//			//Map<String, Map<String, String>> partialAppDataResults = new HashMap<String, Map<String, String>>();
	//			for (String idString: ids){
	//	        	int id= Integer.parseInt(idString);
	//	        	personData = getSinglePersonAppData(id,app_id);
	//	        	finalAppDataResults.put(idString, personData);
	//
	//          }
	//			return finalAppDataResults;
	//		}
	//
	//		/*
	//		 * getSinglePersonAppData() - Returns a map of appdata for a single id.
	//		 */
	//		private Map<String, List <String>> getSinglePersonAppData(int id,String aapId) {
	//
	//			String query="select user_id, name, value from application_settings where user_id="+id;
	//			Map<String, List <String>> personData = new HashMap<String,List <String>>();
	//			 // Execute query to pull all data for provided id.
	//			List <String> listKeys= new ArrayList<String>();
	//			List <String> listValues= new ArrayList<String>();
	//
	//	    	try {
	//	    			ResultSet rs = executeSQLQuery(query);
	//	    			String userId=""+id;
	//	    			// loop through results and add to Hashmap
	//	    			while (rs.next()) {
	//	    				String key = rs.getString("name");
	//	    				String value = rs.getString("value");
	//	    				listKeys.add(key);
	//	    				listValues.add(value);
	//	    			}
	//
	//	    			personData.put("name", listKeys);
	//	    			personData.put("value", listValues);
	//
	//	    			rs.close();
	//	    		} catch (ClassNotFoundException e) {
	//	    			// TODO Auto-generated catch block
	//	    			e.printStackTrace();
	//	    		} catch (SQLException e) {
	//	    			// TODO Auto-generated catch block
	//	    			e.printStackTrace();
	//	    		}
	//
	//	    		// Now only select key's (aka fields) from result's app data
	//			return personData;
	//		}

	/*
	 * getPersonAppData() - Returns intersection of application data from provided ids and keys.
	 */
	public Map<String, Map<String, String>> getPersonAppData(List<String> ids,
			List<String> fields) {

		if (null == ids) {
			return null;
		}
		Map<String, Map<String, String>> appDataResults = new HashMap<String, Map<String, String>>();
		Map<String, Map<String, String>> finalAppDataResults = new HashMap<String, Map<String, String>>();

		// Build query string based on passed in ids
		for (String id : ids) {

			String userid="userId";
			Map<String, String> personData = getSinglePersonAppData(id);
			if (personData != null) {
				appDataResults.put(userid, personData);
			}
		}

		// Now only select key's (aka fields) from result's app data
		for (String key : appDataResults.keySet()) {
			Map<String, String> personData = appDataResults.get(key);
			for (String field : personData.keySet()) {
				if (fields.contains(field)) {
					finalAppDataResults.put(key, appDataResults.get(key));
				}
			}
		}
		return finalAppDataResults;
	}

	/*
	 * getSinglePersonAppData() - Returns a map of appdata for a single id.
	 */
	public Map<String, String> getSinglePersonAppData(String id) {

		if (null == id) {
			return null;
		}

		int userId=Integer.parseInt(id);

		Map<String, String> personData = new HashMap<String, String>();

		String query = "select * from application_settings where user_id = '" + userId	+ "'";

		// Execute query to pull all data for provided id.
		try {
			ResultSet rs = executeSQLQuery(query);

			// loop through results and add to Hashmap
			while (rs.next()) {
				String field = rs.getString("name");
				String value = rs.getString("value");

				personData.put(field, value);
			}
			rs.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Now only select key's (aka fields) from result's app data
		return personData;
	}

	public ResultSet executeSQLQuery(String query)
			throws ClassNotFoundException {
		Statement stmt;
		ResultSet rs = null;

		System.out.println("query: " + query);
		try {
					Class.forName(DB_DRIVER);
			Connection con = DriverManager.getConnection(DB_URL, DB_UID, DB_PW);

			stmt = (Statement) con.createStatement();
			rs = stmt.executeQuery(query);
		}

		catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	/*
	 * executeSQLQuery - A utility method to execute a SQL query against a JDBC
	 * source and returns a ResultSet.
	 */
	public ResultSet executeSQLUpdate(String statement)
			throws ClassNotFoundException {
		Statement stmt;
		ResultSet rs = null;

		System.out.println("executing: " + statement);
		try {

			Class.forName(DB_DRIVER);
			Connection con = DriverManager.getConnection(DB_URL, DB_UID, DB_PW);

			stmt = (Statement) con.createStatement();
			stmt.executeUpdate(statement, stmt.RETURN_GENERATED_KEYS);
			rs = stmt.getGeneratedKeys();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	private String escapeForHtml(String input) {

		final StringBuilder result = new StringBuilder();
		final StringCharacterIterator iterator = new StringCharacterIterator(
				input);
		char character = iterator.current();
		while (character != CharacterIterator.DONE) {
			if (character == '<') {
				result.append("&lt;");
			} else if (character == '>') {
				result.append("&gt;");
			} else if (character == '&') {
				result.append("&amp;");
			} else if (character == '\"') {
				result.append("&quot;");
			} else if (character == '\'') {
				result.append("&#039;");
			} else if (character == '(') {
				result.append("&#040;");
			} else if (character == ')') {
				result.append("&#041;");
			} else if (character == '#') {
				result.append("&#035;");
			} else if (character == '%') {
				result.append("&#037;");
			} else if (character == ';') {
				result.append("&#059;");
			} else if (character == '+') {
				result.append("&#043;");
			} else if (character == '-') {
				result.append("&#045;");
			} else {
				// the char is not a special one
				// add it to the result as is
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();

	}

	/*
	 * cleanForSQL - cleans text for use in SQL statement. For now it simply
	 * replaces single quote "'" with a double quote "''". Can be extended to
	 * guard against other SQL/text problems.
	 */
	private String cleanForSql(String input) {
		String Str=" ";
		if(input != null){
			Str = input.replace("'", "''");
		}
		return Str;
	}

}
