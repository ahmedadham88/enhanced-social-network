package ca.concordia.encs.webservice.application;

public class ApplicationConfiguration {

	private static int webServicPort = 8180;
	private static String webServicAddress = "192.168.1.41";
	private static String destinationHost = "pcrf.openepc.test";
	private static String destinationRealm = "openepc.test";
	private static String xmlConfigFilename = "config/DiameterPeer.xml";

	public static int getWebServicPort() {
		return webServicPort;
	}

	public static String getWebServicAddress() {
		return webServicAddress;
	}

	public static String getDestinationHost() {
		return destinationHost;
	}

	public static String getDestinationRealm() {
		return destinationRealm;
	}

	public static String getXmlConfigFilename() {
		return xmlConfigFilename;
	}

	public static String getWebServiceResourcesAddress() {
		return "http://" + webServicAddress + ":"
				+ Integer.toString(webServicPort);
	}

}
