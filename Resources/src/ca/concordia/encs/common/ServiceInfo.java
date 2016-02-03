package ca.concordia.encs.common;

import java.io.Serializable;

import ca.concordia.encs.constants.MediaType;

public class ServiceInfo  implements Serializable{
	private static final long serialVersionUID = 1L;
	// TODO :Check the design , you may need to support multiple flows
	private String serviceId;
	private long downloadBandwidth;
	private long uploadBandwidth;
	private MediaType mediaType;
	private int lifeTime;
	private int priority;

	/**
	 * @param serviceId
	 * @param downloadBandwidth
	 * @param uploadBandwidth
	 * @param mediaType
	 * @param lifeTime
	 */
	public ServiceInfo(String serviceId, long downloadBandwidth,
			long uploadBandwidth, MediaType mediaType, int lifeTime) {
		this.serviceId = serviceId;
		this.downloadBandwidth = downloadBandwidth;
		this.uploadBandwidth = uploadBandwidth;
		this.mediaType = mediaType;
		this.lifeTime = lifeTime;
	}
	
	public ServiceInfo(){
		
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public long getDownloadBandwidth() {
		return downloadBandwidth;
	}

	public void setDownloadBandwidth(long downloadBandwidth) {
		this.downloadBandwidth = downloadBandwidth;
	}

	public long getUploadBandwidth() {
		return uploadBandwidth;
	}

	public void setUploadBandwidth(long uploadBandwidth) {
		this.uploadBandwidth = uploadBandwidth;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	public int getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime(int lifeTime) {
		this.lifeTime = lifeTime;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
