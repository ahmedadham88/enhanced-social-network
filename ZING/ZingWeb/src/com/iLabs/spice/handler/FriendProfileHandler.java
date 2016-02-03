/*
 * Copyright 2008 Impetus Infotech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iLabs.spice.handler;

import java.util.Collection;
import java.util.List;

import java.sql.Timestamp;
import java.util.Date;

import com.iLabs.pagecode.PageCodeBase;
import com.iLabs.spice.beans.ProfileBean;
import com.iLabs.spice.beans.EditProfileBean;
import com.iLabs.spice.common.exception.SysException;
import com.iLabs.spice.common.servicelocator.ServiceLocator;
import com.iLabs.spice.dto.UserAlbum;
import com.iLabs.spice.dto.UserAuth;
import com.iLabs.spice.dto.UserFriends;
import com.iLabs.spice.services.IPerson;

/**
 * This class handles all actions related to visit friends profile like
 * populating friend profile details and friend's album.
 * 
 * @author iLabs
 * @date 01/08/2008
 */
public class FriendProfileHandler extends PageCodeBase {
	/**
	 * Attribute for holding user album list.
	 */
	private Collection userAlbumList;

	/**
	 * This method called when user visit one's profile and it
	 * set profile details in current profile. 
	 * @return String
	 */
	public String getFriendProfile() {
		try {
			IPerson person = (IPerson) ServiceLocator.getService("PersonSvc");
			int friendsId = Integer.parseInt(getRequestParam().get("friendsId").toString());
			UserAuth authPerson = person.getPersonDetails(friendsId);
			if (authPerson != null && authPerson.getUserName() != null) {
				ProfileBean currentProfile =(ProfileBean) getSessionScope().get("currentProfile");
				UserAuth userAuth = currentProfile.getUserAuth();
				UserFriends userFriends = person.getFriends(authPerson
						.getUserId());
				currentProfile.setUserAuth(authPerson);
				currentProfile.setUserFriends(userFriends);
				getSessionScope().put("currentProfile", currentProfile);
				
				java.util.Date date= new java.util.Date();
				System.out.println("The End Time: "+new Timestamp(date.getTime()));
			}

		} catch (SysException e) {
			e.printStackTrace();
		}

		return "lnkFriendProfile";
	}
	
	/**
	 * This method will retrieve a string to be filtered
	 */
	public String getFilterString() {
		ProfileBean profileBean = (ProfileBean) getSessionScope().get(
				"currentProfile");
		UserAuth userAuth = profileBean.getUserAuth();

		EditProfileBean editProfileBean = (EditProfileBean) getSessionScope()
				.get("editProfileBean");
		String result = "failure";
		String finalInterest = "";

		/* 
		 * This loop is to convert the list of interests from EditProfileBean
		 * into a single concatenated String 
		 * The String is then set into UserProfile data Object
		 */
		for (String interest : editProfileBean.getInterestsList()) {
			if (finalInterest != "")
				finalInterest = finalInterest + "" + interest;
			else
				finalInterest = interest;
		}

		return finalInterest;
	}
	/**
	 * This method fetch friend's album. 
	 * @return String
	 */
	private void populateUserAlbum() {
		IPerson person;
		try {

			ProfileBean currentProfile = (ProfileBean) getSessionScope().get("currentProfile");
			UserAuth userAuth = currentProfile.getUserAuth();
			int userId = userAuth.getUserId();
			person = (IPerson) ServiceLocator.getService("PersonSvc");
			List<UserAlbum> userAlbumData = (List<UserAlbum>) person
					.getPersonAlbum(userId);
			if(userAlbumData!=null)
			userAlbumList= userAlbumData.get(0).getImage();
		} catch (SysException e) {			
			e.printStackTrace();
		}
		
	}
	/**
	 * Getter for userAlbumList.
	 * @return Collection
	 */
	public Collection getUserAlbumList() {
		return userAlbumList;
	}
	
	/**
	 * This method is used to fetch friend's album.
	 * @return String
	 */
	public String showUserAlbum(){
			populateUserAlbum();
		return "lnkFriendAlbum";
	}
	
	/**
 	* Setter for userAlbumList.
	* @param userAlbumList
 	*/
	public void setUserAlbumList(Collection userAlbumList) {
		this.userAlbumList = userAlbumList;
	}

}
