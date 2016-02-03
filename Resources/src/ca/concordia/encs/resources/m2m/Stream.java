package ca.concordia.encs.resources.m2m;

import ca.concordia.encs.constants.TransportProtocol;
import ca.concordia.encs.constants.VideoQuality;

public class Stream {
	private String id;
	/**
	 * The M2M device is the source of the media
	 */
	private String deviceId;
	private VideoQuality videoQuality;
	/**
	 * RTP
	 */
	private String destinationIP;
	/**
	 * RTP
	 */
	private long destinationPort;
	
	private TransportProtocol protocol;
	/**
	 * RTSP
	 */
	private String mediaUrl;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public VideoQuality getVideoQuality() {
		return videoQuality;
	}
	public void setVideoQuality(VideoQuality videoQuality) {
		this.videoQuality = videoQuality;
	}
	public String getDestinationIP() {
		return destinationIP;
	}
	public void setDestinationIP(String destinationIP) {
		this.destinationIP = destinationIP;
	}
	public long getDestinationPort() {
		return destinationPort;
	}
	public void setDestinationPort(long destinationPort) {
		this.destinationPort = destinationPort;
	}
	public TransportProtocol getProtocol() {
		return protocol;
	}
	public void setProtocol(TransportProtocol protocol) {
		this.protocol = protocol;
	}
	public String getMediaUrl() {
		return mediaUrl;
	}
	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}
	
	
	
	
	
	
}
