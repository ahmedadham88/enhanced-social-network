package com.iLabs.spice.dto;

import com.iLabs.pagecode.PageCodeBase;
import com.iLabs.spice.dto.UserAuth;
import com.iLabs.spice.beans.ProfileBean;

public class ProfileTaskHandler extends PageCodeBase{
	
	
	public String getContentFilter(){
		ProfileBean profileBean = (ProfileBean) getSessionScope().get(
				"currentProfile");
		UserAuth userAuth = profileBean.getUserAuth();
		String filter = "";
		String user = userAuth.getProfile().getFirstName();
		if (user!=null){
		//Filtering of the unwanted category of Friendship requests
		filter = userAuth.getProfile().getInterests();
		System.out.println("the content filter of user "+user+" is: " + filter);
		}
		return filter;
	}
}
