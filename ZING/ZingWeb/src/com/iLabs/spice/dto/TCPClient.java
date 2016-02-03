package com.iLabs.spice.dto;

import java.io.*;
import java.net.*;

import com.iLabs.spice.beans.EditProfileBean;
import com.iLabs.spice.beans.ProfileBean;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.iLabs.pagecode.PageCodeBase;
import com.iLabs.spice.dto.UserAuth;
import com.iLabs.spice.dto.UserProfile;
import com.iLabs.spice.handler.AddFriendHandler;

class TCPClient extends PageCodeBase
{
	Socket clientSocket;
	DataOutputStream outToServer;
	BufferedReader inFromServer;
	String modifiedSentence="";
	
	public void Connect() throws UnknownHostException, IOException{
		clientSocket = new Socket("localhost", 6789);
		outToServer = new DataOutputStream(clientSocket.getOutputStream());
		inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	}
	
	public String SocketWrite(String sentence) throws UnknownHostException, IOException
 	{
			outToServer.writeBytes(sentence + '\n');
			modifiedSentence = inFromServer.readLine();
			System.out.println("FROM SERVER: " + modifiedSentence);
		
		if(modifiedSentence!=null)
		return modifiedSentence;
		else
			return "no";
 	}
	public void CloseConnect() throws IOException{
		clientSocket.close();
	}
	
	public static void main(String[]args) throws UnknownHostException, IOException{
		TCPClient c = new TCPClient();
		c.Connect();
		c.SocketWrite("Hello my food chain");
		c.SocketWrite("I'm alive");
		c.CloseConnect();
		System.out.println("Test Success!");
		
	}
} 