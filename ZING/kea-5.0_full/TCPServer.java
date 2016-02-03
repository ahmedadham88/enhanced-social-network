import java.net.*;
import java.io.*;
import java.util.*;

public class TCPServer extends Thread
{
	private static int i=1;
	private int sentenceCount = 0;
	private int sentenceFilter = 0;
	static ServerSocket welcomeSocket;
	static Socket connectionSocket;
	
	public void run()
	{
		try
		{	
			boolean result = false;
			BufferedReader inFromClient=new BufferedReader
			(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient= new DataOutputStream
			(connectionSocket.getOutputStream());
			String clientSentence;
			String toServer;
			String capitalizedSentence;
			while (true) {
				clientSentence = inFromClient.readLine();
        			if(clientSentence!=null){
					sentenceCount++;
            				System.out.println("Received to filter: " + clientSentence);
					/*if(clientSentence.startsWith("Food")){
						result = true;
					}*/
					ContentFilter content = new ContentFilter();            				
					result = content.Filter(clientSentence, "Food and Agriculture");
			
			if(result==true){
			    System.out.println("Sending Yes ..");
			    sentenceFilter++;
			    outToClient.writeBytes("yes"+ '\n');
			}
			else{
			    System.out.println("Sending No ..");	
			    outToClient.writeBytes("no"+ '\n');
			}
			System.out.println("Number of Sentences received "+sentenceCount+" and number of sentences filtered "+sentenceFilter);
			}
			 }
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
	}
	
	public static void main(String[] args) throws IOException
	{
		try
		{
			welcomeSocket=new ServerSocket(6789);
			while(true)
			{
				connectionSocket =welcomeSocket.accept();
				TCPServer r=new TCPServer();
				//Thread thread =new Thread(r);
				System.out.println("Connection Set with Client .. "+i);
				i++;
				r.start();
			}	
		}
		catch(Exception e)
		{
			System.out.println("entered");
		}
		
	}
}

