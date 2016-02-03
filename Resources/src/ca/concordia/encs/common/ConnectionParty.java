package ca.concordia.encs.common;

import java.io.Serializable;

public class ConnectionParty implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long port;
	private String ip;
	private String sip_uri;

	public ConnectionParty(String sip_uri, String ip, long port) {
		this.sip_uri = sip_uri;
		this.ip = ip;
		this.port = port;
	}
	
	public ConnectionParty() {
		
	}

	public long getPort() {
		return port;
	}

	public void setPort(long port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSip_uri() {
		return sip_uri;
	}

	public void setSip_uri(String sip_uri) {
		this.sip_uri = sip_uri;
	}

}
