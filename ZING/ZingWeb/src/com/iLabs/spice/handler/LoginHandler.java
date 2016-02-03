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

import java.io.*;
import java.nio.file.Files;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.jxpath.servlet.HttpSessionHandler;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.Protocol;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import ca.concordia.encs.common.ConnectionParty;
import ca.concordia.encs.common.ServiceInfo;
import ca.concordia.encs.constants.MediaType;
import ca.concordia.encs.resources.qos.Session;
import ca.concordia.encs.resources.qos.SessionRequestResult;

import com.iLabs.pagecode.PageCodeBase;
import com.iLabs.spice.beans.ProfileBean;
import com.iLabs.spice.common.exception.SysException;
import com.iLabs.spice.common.servicelocator.ServiceLocator;
import com.iLabs.spice.dto.UserAuth;
import com.iLabs.spice.dto.UserFriends;
import com.iLabs.spice.services.IPerson;

import com.iLabs.spice.dto.Application;

/**
 * This class is used to handle functionality related to login like 
 * authenticate user,forgot password and logout.
 * @author iLabs
 * @date 01/08/2008
 */
public class LoginHandler extends PageCodeBase{

	/**
	 * This function is called when user enters the site.
	 * It first authenticates the user and after successful authentication, user info is 
	 * set into currentProfile bean and ownerProfile bean.
	 * On successful authentication user is redirected to home page.
	 * 
	 * @return String 
	 */
	private String value;
	private SessionIdentifier SI = new SessionIdentifier();
	private String qoslevel;
	
	public String connectionAction(){
		System.out.println("............CONNECTION SUCCESS..........");
		return "success";
	}
	
	public String goBack(){
		return "success";
	}
	
	
	
	public String loginAction() throws IOException, ClassNotFoundException, SQLException  {
		String result="failure";
		try {
			java.util.Date date= new java.util.Date();
			System.out.println("The Start Time (1): "+new Timestamp(date.getTime()));
			
			ProfileBean ownerProfile = (ProfileBean)  getSessionScope().get("ownerProfile");
			ProfileBean currentProfile = (ProfileBean)  getSessionScope().get("currentProfile");
			if(ownerProfile==null)
			{
				ownerProfile = new ProfileBean();
			}
			if(currentProfile==null)
			{
				currentProfile = new ProfileBean();
			}
			
			
			IPerson person = (IPerson)ServiceLocator.getService("PersonSvc");
			UserAuth authPerson = person.authenticateUser(currentProfile.getUserAuth().getUserName(),currentProfile.getUserAuth().getUserPassword());
			
			//This condition checks if the authPerson returned from authentication service is null or not.
			//If the user who enters the site is an authenticated user, the user's info and his friends info is stored in 
			//currentProfile as well as ownerProfile bean.
		     if(authPerson!=null && authPerson.getUserName()!=null){	    	 
		    	 //Save the QoS Level of the user (Platinum, Gold or Silver)
		    	 qoslevel = currentProfile.getUserAuth().getProfile().getProfileURL();

					
				ClientResource clientResource = new ClientResource("http://192.168.1.41:8180/sessions");
				
				Session session = new Session();
				
				ConnectionParty owner = new ConnectionParty();
				HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
				owner.setIp(request.getHeader("X-Forwarded-For"));
				owner.setPort(request.getRemotePort());
				System.out.println("Client address: " + request.getRemoteAddr());
				System.out.println("Client port: " + request.getRemotePort());
				if(qoslevel.equalsIgnoreCase("GOLD")){
					owner.setSip_uri("sip:alice@openepc.test");
					session.setApplicationId("surveillance");
				}
				else{if(qoslevel.equalsIgnoreCase("Silver")){
					owner.setSip_uri("sip:bob@openepc.test");
					session.setApplicationId("IMS");
				}else{
					owner.setSip_uri("sip:charlie@openepc.test");
					session.setApplicationId("YouTube");
				}
				}
				System.out.println("REQUEST  "+request.getRemoteAddr()+request.getRemoteHost()+request.getLocalPort()+request.getRequestURI());
				System.out.println( request.getHeader("X-Forwarded-For"));
				ConnectionParty otherParty = new ConnectionParty();
				otherParty.setIp("192.168.1.41");
				otherParty.setPort(8080);
				otherParty.setSip_uri("");
				
				session.setSessionOwner(owner);
				session.setSessionOtherParty(otherParty);
				
				
				
				ServiceInfo serviceInfo = new ServiceInfo();
				//serviceInfo.setServiceId("Webcamstream");
				setBandwidthAndPriority(serviceInfo, qoslevel);
				serviceInfo.setMediaType(MediaType.DATA);
				serviceInfo.setLifeTime(6000);

				session.setServiceInfo(serviceInfo);
				
				
				Representation response = clientResource.post(session);
				String resp = response.getText();
				SI.setSessionID(resp.substring(70, 125));
				System.out.println("200 OK RESPONSE IS: "+resp);
				String s = SI.getSessionID();
				System.out.println("SESSION ID IS: "+s);
				

		    	 UserFriends userFriends = person.getFriends(authPerson.getUserId());		    	    	 		    	
		    	 ownerProfile.setUserAuth(authPerson);
		    	 ownerProfile.setUserFriends(userFriends);
		    	 currentProfile.setUserAuth(authPerson);
		    	 currentProfile.setUserFriends(userFriends);
		    	 getSessionScope().put("ownerProfile", ownerProfile);
		    	 getSessionScope().put("currentProfile", currentProfile);
		    	 
		    	DatabaseConnector r = new DatabaseConnector();
		    	r.Write(s, authPerson.getUserId());
		    	//authPerson.getProfile().setProfileURL(s);
		    	System.out.println("User "+authPerson.getProfile().getFirstName() + " "+authPerson.getProfile().getProfileURL());
		 		
		    	
		    	System.out.println("The Start Time (3): "+new Timestamp(date.getTime()));
		    	 result="success";
		     }
		     else{               // if user is not an authenticate user, then error message is generated.
		    	 FacesMessage message = new FacesMessage("Please Check Username and password");			
				  FacesContext.getCurrentInstance().addMessage("login:user_password", message);
		     }
		     
		    		   
		} catch (SysException e) {			
			e.printStackTrace();
		}
    return result; 
	
 	}
	
	private void setBandwidthAndPriority(ServiceInfo serviceInfo, String qosLevel) {
		long bandwidth = 1;
		int priority = 3;
		if ("GOLD".equalsIgnoreCase(qosLevel)) {
			bandwidth = 8000;
			priority = 1;
			serviceInfo.setServiceId("Webcamstream");
		} else if ("SILVER".equalsIgnoreCase(qosLevel)) {
			bandwidth = 20;
			priority = 2;
			serviceInfo.setServiceId("IMS Services");
		}
		else{
			serviceInfo.setServiceId("www.youtube.com");
		}
		serviceInfo.setDownloadBandwidth(bandwidth);
		serviceInfo.setUploadBandwidth(bandwidth);
		serviceInfo.setPriority(priority);
	}
	/**
	 * This function is used to send mail to the user who forgets password.
	 * User enters his user name which is used to retrieve his password and emailId.
	 * User's password is sent to corresponding mail id using mail service.
	 * 
	 * @return void
	 */
	
	public void forgotPwd()   {
		
		IPerson person;
		try {
			ProfileBean currentProfile = (ProfileBean)  getSessionScope().get("currentProfile");
			UserAuth userAuth = currentProfile.getUserAuth();
			person = (IPerson)ServiceLocator.getService("PersonSvc");			
			String userName=userAuth.getUserName();
		    UserAuth userUpdate= person.getPassword(userName);	
		    //this checks whether the provided user name exists or not.
		    if(userUpdate != null){
		    	FacesMessage message = new FacesMessage("Your password has been sent to your mail account");			
				FacesContext.getCurrentInstance().addMessage("forgotPassword:submit", message);
		    }
		    else{
		    	FacesMessage message = new FacesMessage("The user name does not exist!!!");			
				FacesContext.getCurrentInstance().addMessage("forgotPassword:submit", message);
		    }
		} catch (SysException e) {			
			e.printStackTrace();
		}			
	}
   
	/**
	 * This function is called when user logs out from the system.
	 * It invalidates the session info.
	 * 
	 * @return String "success" is returned which determines the navigation. User is redirected to login page.
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
   public String logout() throws IOException, ClassNotFoundException, SQLException   {
	   ProfileBean profileBean = (ProfileBean) getSessionScope().get(
				"currentProfile");
		UserAuth userAuth = profileBean.getUserAuth();
		//String s = userAuth.getProfile().getProfileURL();
		int Q = userAuth.getProfile().getUserId();
		DatabaseConnector r = new DatabaseConnector();
		String s = r.Read(Q);
		 System.out.println("Closing Session: "+s);
		 ClientResource clientResource = new ClientResource("http://192.168.1.41:8180/sessions/"+s);
		 clientResource.delete();
		r.DELETE(Q);
		 
		 HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		 session.invalidate();
	   
		
		
	return "success";
	   
   }
   /*
   public void deletefile() throws IOException{
	   BufferedWriter out = new BufferedWriter
		         (new FileWriter("/root/Desktop/ZING/sessionid.txt"));
		         out.write("aString1\n");
		         out.close();
		         boolean success = (new File
		         ("filename")).delete();
		         if (success) {
		            System.out.println("The file has been successfully deleted"); 
		         }
   }
   public String read() throws FileNotFoundException{
	   String content = new Scanner(new File("/root/Desktop/ZING/sessionid.txt")).useDelimiter("\\Z").next();
	   return content;
   }*/
}