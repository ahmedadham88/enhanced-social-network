package ca.concordia.encs.resources.streamingenabler;

import ca.concordia.encs.common.ConnectionParty;
import ca.concordia.encs.constants.MediaSourceType;

public class MediaSource {

	private String id;
	// private ConnectionParty rtpSource;
	private String location;
	private MediaSourceType type;
	private String address;
	private int port;

//	/**
//	 * @param rtpSource
//	 * @param serverAddress
//	 * @param serverPort
//	 */
//	public MediaSource(ConnectionParty rtpSource, String serverAddress,
//			int serverPort) {
//		this.id = "";
//		// this.rtpSource = rtpSource;
//		this.address = serverAddress;
//		this.port = serverPort;
//		this.type = MediaSourceType.RTP;
//
//	}
//
//	/**
//	 * @param fileId
//	 * @param address
//	 * @param port
//	 */
//	public MediaSource(String fileId) {
//		this.id = "";
//		this.location = fileId;
//		this.address = "";
//		this.port = 0;
//		this.type = MediaSourceType.File;
//
//	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	// public ConnectionParty getRTPSource() {
	// return rtpSource;
	// }
	//
	// public void setRTPSource(ConnectionParty rtpSource) {
	// this.rtpSource = rtpSource;
	// }

	public String getLocation() {
		return location;
	}

	public void setLocation(String fileId) {
		this.location = fileId;
	}

	public MediaSourceType getType() {
		return type;
	}

	public void setType(MediaSourceType type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int serverPort) {
		this.port = serverPort;
	}

}
