import org.restlet.Client;
import org.restlet.resource.ClientResource;

import ca.concordia.encs.common.ConnectionParty;
import ca.concordia.encs.common.ServiceInfo;
import ca.concordia.encs.constants.MediaType;
import ca.concordia.encs.resources.qos.Session;
import ca.concordia.encs.resources.qos.SessionRequestResult;


public class Test {
	public static void main(String[] args) {
		  // TODO Auto-generated method stub
		  ClientResource client = null;
		  try {
		   // client = new ClientResource("http://localhost:8190/");
		   client = new ClientResource("http://192.168.1.41:8180/sessions");
		   // s = client.get(Session.class);
		   // client.delete();
		   ConnectionParty source = new ConnectionParty("", "192.168.1.41",
		     444);
		   ConnectionParty dest = new ConnectionParty(
		     "sip:bob@openepc.test", "192.168.3.131", 23002);
//		   ServiceInfo serviceInfo = new ServiceInfo("Webcamstream", 1000,
//		     400, MediaType.VIDEO, 40);
		   
		   ServiceInfo serviceInfo = new ServiceInfo("IMS Services", 1000,
				     400, MediaType.DATA, 40);
		   
		   
		   Session newSession = new Session("surveillance", dest, source,
		     serviceInfo);
		   System.out.println(newSession.getSessionOwner().getSip_uri());
		   // con.Store(newSession);
		   // POST
		   SessionRequestResult result = client.post(newSession,SessionRequestResult.class);
		  
		   if(client.getStatus().isSuccess()){
			   System.out.println("Success.");
		   }
		   
		   if(result!=null )
		   {
			   if(result.isSuccess()){
				   System.out.println("result = Success.");
			   }
		   }
		   
		  } catch (Exception e) {
		   // System.out.println("client.getStatus().getDescription()" +
		   // client.getStatus().getDescription());
		   e.printStackTrace();
		  }
		  client = null;
		 }
}
