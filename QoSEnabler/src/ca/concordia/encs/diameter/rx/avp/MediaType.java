
package ca.concordia.encs.diameter.rx.avp;

import ca.concordia.encs.diameter.base.avptype.Enumerated;
import ca.concordia.encs.diameter.base.constants.Vendors;


/**
 * The Media-Type AVP (AVP code 520) is of type Enumerated, and it determines 
 * the media type of a session component. The media types indicate the type of 
 * media in the same way as the SDP media types with the same names defined in 
 * RFC 4566 [13]. 
 * 
 * TS 29.214
 * 
 *
 *
 */
public class MediaType extends Enumerated {

	public static final int AVP_CODE = 520;
	public static final int VENDOR_ID = Vendors.threeGPP_VENDOR_ID;

	
	public static final int AUDIO = 0;
	public static final int VIDEO = 1;
	public static final int DATA  = 2;
	public static final int	APPLICATION = 3;
	public static final int CONTROL = 4;
	public static final int TEXT = 5;
	public static final int	MESSAGE = 6;
	public static final int	OTHER = 0xFFFFFFFF;

	
	
	public MediaType() {
		super(AVP_CODE,true,VENDOR_ID);
		init();
	}
	

	public MediaType(int _mediaType) {
		super(AVP_CODE,true,VENDOR_ID);
		setInteger32(_mediaType);
		init();
	}

	public static String getMediaTypeName(MediaType _mediaType)	{
		switch(_mediaType.getEnumerated())	{
		case AUDIO:
			return "audio";
		case VIDEO:
			return "video";
		case DATA:
			return "data";
		case APPLICATION:
			return "application";
		case CONTROL:
			return "control";
		case TEXT:
			return "text";
		case MESSAGE:
			return "message";
		default:	
			return "other";
		}
	}
	private void init()	{
		mapping.put(0, "AUDIO");
		mapping.put(1, "VIDEO");
		mapping.put(2, "DATA");
		mapping.put(3, "APPLICATION");
		mapping.put(4, "CONTROL");
		mapping.put(5, "TEXT");
		mapping.put(6, "MESSAGE");
		mapping.put(0xFFFFFFFF, "OTHER");

	}
}
