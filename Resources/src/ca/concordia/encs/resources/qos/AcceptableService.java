package ca.concordia.encs.resources.qos;

import java.io.Serializable;

public class AcceptableService implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public AcceptableService(){
		
	}
	
	private long maxRequestedBandwidthDL;

	public long getMaxRequestedBandwidthDL() {
		return maxRequestedBandwidthDL;
	}

	public void setMaxRequestedBandwidthDL(long maxRequestedBandwidthDL) {
		this.maxRequestedBandwidthDL = maxRequestedBandwidthDL;
	}

	public long getMaxRequestedBandwidthUL() {
		return maxRequestedBandwidthUL;
	}

	public void setMaxRequestedBandwidthUL(long maxRequestedBandwidthUL) {
		this.maxRequestedBandwidthUL = maxRequestedBandwidthUL;
	}

	private long maxRequestedBandwidthUL;
}
