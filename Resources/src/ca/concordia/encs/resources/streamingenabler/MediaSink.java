package ca.concordia.encs.resources.streamingenabler;


public class MediaSink {
	private String id;
	//private MediaSourceType type;
	private String mediaSourceId;
	private String sinkURL;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMediaSourceId() {
		return mediaSourceId;
	}
	public void setMediaSourceId(String mediaSourceId) {
		this.mediaSourceId = mediaSourceId;
	}
	public String getSinkURL() {
		return sinkURL;
	}
	public void setSinkURL(String sinkURL) {
		this.sinkURL = sinkURL;
	}
}
