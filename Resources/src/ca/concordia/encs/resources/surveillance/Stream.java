package ca.concordia.encs.resources.surveillance;

import java.io.Serializable;

import ca.concordia.encs.constants.StreamSourceType;
import ca.concordia.encs.constants.VideoQuality;

public class Stream implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String sourceId;
	private StreamSourceType sourceType;
	private String classOfServiceId;
	private String videoClassId;
	private String owner;
	private String mediaUrl;

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public StreamSourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(StreamSourceType sourceType) {
		this.sourceType = sourceType;
	}

	public String getClassOfServiceID() {
		return classOfServiceId;
	}

	public void setClassOfService(String classOfServiceId) {
		this.classOfServiceId = classOfServiceId;
	}

	public String getVideoClassID() {
		return videoClassId;
	}

	public void setVideoClassID(String videoClassId) {
		this.videoClassId = videoClassId;
	}

}
