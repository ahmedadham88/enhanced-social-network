package ca.concordia.encs.resources.m2m;

public class M2MGateway {
	String id;
	String baseUrl;
	String ipAddress;
	String sip_uri;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIPAddress() {
		return ipAddress;
	}
	public void setIPAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getSIP_URI() {
		return sip_uri;
	}
	public void setSIP_URI(String sip_uri) {
		this.sip_uri = sip_uri;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	 
	
}
