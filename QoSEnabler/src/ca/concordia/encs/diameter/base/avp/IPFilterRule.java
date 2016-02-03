
package ca.concordia.encs.diameter.base.avp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.concordia.encs.diameter.base.avptype.OctetString;
import ca.concordia.encs.diameter.exception.IllegalAVPDataException;



public class IPFilterRule extends OctetString {

	public static Pattern HOST;
	//public static Pattern PORTS;
	public static Pattern PORTRANGE;
	public static Pattern SOCKET;
	public static Pattern PROTOCOL;
	public static Pattern IPFILTERRULE;
	
	static{
		String host = "(((\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3}))(\\/(\\d{1,2}))?)";
		String port = "\\d{1,5}";
		String portRange = port+"[,"+port+"]*";
		String protocol = "(\\d{1,3}|ip)";
		String socket = host+"( ("+portRange+"))?";
		PROTOCOL = Pattern.compile(protocol);
		HOST = Pattern.compile(host);
		//PORTS = Pattern.compile(ports);
		PORTRANGE = Pattern.compile(portRange);
		SOCKET = Pattern.compile(socket);
		
		IPFILTERRULE = Pattern.compile("(permit|deny) (in|out) "+protocol+" from "+socket+" to "+socket+"( .+)*");
		//FIXME removed options
		//IPFILTERRULE = Pattern.compile("(permit|deny) (in|out) "+protocol+" from "+socket+" to "+socket);
	}
	
	/**
	 * for defining the action
	 */
	public static final String PERMIT_TOKEN="permit";
	public static final String DENY_TOKEN="deny";
	
	/**
	 * for defining the direction
	 */
	public static final String IN_TOKEN="in";
	public static final String OUT_TOKEN="out";
	
	public static final String TO_TOKEN="to";
	/**
	 * indicates the String which is the actual to the variables
	 */
	private String lastParsedString = "";
	
	/**
	 * is true if this action is a permit action
	 */
	private boolean actionIsPermit = false;
	/**
	 * is true if this rule defines an in direction
	 */
	private boolean directionIsIn = false;
	/**
	 * the protocol which is applied on this rule
	 */
	private String proto=null;

	/**
	 * the source of the flow
	 */
	private String srcHost = null;
	private String srcPort = null;
	/**
	 * the destination of the flow
	 */
	private String dstHost = null;
	private String dstPort = null;
	
	/**
	 * further options
	 */
	private String options = null;
	
	public IPFilterRule() {
		super();
		lastParsedString = "";
	}
	
	/**
	 * @param Code
	 * @param Mandatory
	 * @param Vendor_id
	 */
	public IPFilterRule(int Code, boolean Mandatory, int Vendor_id) {
		super(Code, Mandatory, Vendor_id);
	}

	/**
	 * @param octetString
	 * @throws IllegalAVPDataException 
	 */
	/**
	public IPFilterRule(String octetString){
		super();
		this.setOctetString(octetString);
		lastParsedString = "";
		try {
			this.updateMembers();
		} catch (IllegalAVPDataException e) {
			e.printStackTrace();
		}
	}
	**/
	/**
	 * sets the IPFilterRule with a complete configuration String
	 * @param _rule
	 */
	public void setIPFilterRule(String _rule){
		this.setOctetString(_rule);
		try {
			this.updateMembers();
		} catch (IllegalAVPDataException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * is this rule to permit a flow
	 * @return	true if this rule allows a flow
	 */
	public boolean isActionPermit()	{
		checkUpdating();
		return actionIsPermit;
	}
	/**
	 * is this rule to deny a flow
	 * @return	true if this rule rejects a flow 
	 */
	public boolean isActionDeny()	{
		checkUpdating();
		return !actionIsPermit;
	}
	/**
	 * set the action to permit
	 */
	public void setActionPermit()	{
		if(actionIsPermit!= true)
		{
			actionIsPermit = true;
			updateBytes();
		}
	}
	/**
	 * set the action to deny
	 */
	public void setActionDeny()	{
		if(actionIsPermit!=false)
		{
			actionIsPermit = false;
			updateBytes();
		}
	}
	
	/**
	 * is this rule direction in
	 * @return	true if this rule is in
	 */
	public boolean isDirectionIn()	{
		checkUpdating();
		return directionIsIn;
	}
	/**
	 * is this rule direction out
	 * @return	true if this rule is out
	 */
	public boolean isDirectionOut()	{
		checkUpdating();
		return !directionIsIn;
	}
	/**
	 * set the direction to in
	 */
	public void setDirectionIn()	{
		if(directionIsIn!=true)
		{
			directionIsIn = true;
			updateBytes();
		}
	}
	/**
	 * set the direction to out
	 */
	public void setDirectionOut()	{
		if(directionIsIn!=false)
		{
			directionIsIn = false;
			updateBytes();
		}
	}
	/**
	 * requests the protocols which should be handled by this filter
	 * @return	the protocol number (e.g. UDP 17) or ip if all IP traffic is considered 
	 */
	public String getProto()	{
		checkUpdating();
		return proto;
	}
	/**
	 * sets the protocol which should be applied on this rule
	 * @param _proto	pthe protocol number (e.g. UDP 17) or ip if all IP traffic is considered
	 * @throws IllegalAVPDataException  
	 */
	public void setProto(String _proto) throws IllegalAVPDataException	{
		if(!PROTOCOL.matcher(_proto).matches())
			throw new IllegalAVPDataException("The value "+_proto+" for the protocol is not allowed!");
		if(this.proto == null || _proto.compareTo(this.proto)!=0)
		{
			this.proto=_proto;
			this.updateBytes();
		}
	}
	
	
	public String getSrcHost() {
		checkUpdating();
		return srcHost;
	}
	/**
	 * sets the src Host IP address
	 * the parser accepts x.x.x.x[/xx]
	 * @param srcHost
	 * @throws IllegalAVPDataException	the given String does not match with the parser
	 */
	public void setSrcHost(String srcHost) throws IllegalAVPDataException {
		if(!HOST.matcher(srcHost).matches())
			throw new IllegalAVPDataException("Could not accept the sourceHost "+srcHost+" !");
		if(this.srcHost == null || this.srcHost.compareTo(srcHost)!=0)
		{
			this.srcHost = srcHost;
			updateBytes();
		}
	}
	public String getSrcPort() {
		checkUpdating();
		return srcPort;
	}
	/**
	 * sets the src Port
	 * ATTENTION:
	 * This parser only accepts the 3GPP recommendations
	 * 
	 * the parser accepts port[,port]*
	 * @param srcPort
	 * @throws IllegalAVPDataException	the given String does not match with the parser
	 */
	public void setSrcPort(String srcPort) throws IllegalAVPDataException {
		if(!PORTRANGE.matcher(srcPort).matches())
			throw new IllegalAVPDataException("Could not accept the sourceHost "+srcPort+" !");
		if(this.srcPort == null || this.srcPort.compareTo(srcPort)!=0)
		{
			this.srcPort = srcPort;
			updateBytes();
		}
	}
	public String getDstHost() {
		checkUpdating();
		return dstHost;
	}
	/**
	 * sets the dst Host IP address
	 * the parser accepts x.x.x.x[/xx]
	 * @param dstHost
	 * @throws IllegalAVPDataException	the given String does not match with the parser
	 */
	public void setDstHost(String dstHost) throws IllegalAVPDataException {
		if(!HOST.matcher(dstHost).matches())
			throw new IllegalAVPDataException("Could not accept the sourceHost "+dstHost+" !");
		if(this.dstHost == null || this.dstHost.compareTo(dstHost)!=0)
		{
			this.dstHost = dstHost;
			updateBytes();
		}
	}
	public String getDstPort() {
		checkUpdating();
		return dstPort;
	}
	/**
	 * sets the dst Port
	 * ATTENTION:
	 * This parser only accepts the 3GPP recommendations
	 * 
	 * the parser accepts port[,port]*
	 * @param dstPort
	 * @throws IllegalAVPDataException	the given String does not match with the parser
	 */
	public void setDstPort(String dstPort) throws IllegalAVPDataException {
		if(!PORTRANGE.matcher(dstPort).matches())
			throw new IllegalAVPDataException("Could not accept the sourceHost "+dstPort+" !");
		if(this.dstPort == null || this.dstPort.compareTo(dstPort)!=0)
		{
			this.dstPort = dstPort;
			updateBytes();
		}
	}
	
	public String getOptions() {
		checkUpdating();
		return options;
	}
	/**
	 * sets the options
	 * all tokens are allowed!
	 * @param options
	 */
	public void setOptions(String options) {
		if(this.options == null || this.options.compareTo(options)!=0)
		{
			this.options = options;
			updateBytes();
		}
	}
	
	
	/**
	 * should be called if the rule is manipulated but the byte array is not up to date
	 */
	private void updateBytes()	{
		//recreate the string
		StringBuffer sbf = new StringBuffer();
		//append action
		if (isActionPermit())
			sbf.append(PERMIT_TOKEN);
		if (isActionDeny())
			sbf.append(DENY_TOKEN);
		sbf.append(" ");
		//append direction
		if(isDirectionIn())
			sbf.append(IN_TOKEN);
		if(isDirectionOut())
			sbf.append(OUT_TOKEN);
		sbf.append(" ");
		//append protocol
		sbf.append(proto+" from ");
		//append source
		sbf.append(srcHost+" ");
		if(srcPort!=null)
			sbf.append(srcPort+" ");
		// append to
		sbf.append(TO_TOKEN+" ");
		//append destination
		sbf.append(dstHost+" ");
		if(dstPort!=null)
			sbf.append(dstPort+" ");
		//append options
		if(options!=null)
			sbf.append(options);
		this.setOctetString(sbf.toString());
		lastParsedString=sbf.toString();
	}
	/**
	 * should be called if the byte array is changed, but the member variables not
	 * @throws IllegalAVPDataException 
	 */
	public void updateMembers() throws IllegalAVPDataException	{
		//parse
		String str = this.getOctetString();
		//System.out.println("Parsing!!!");
		//System.out.println(str);
		//System.out.println(IPFILTERRULE.pattern());
		Matcher m = IPFILTERRULE.matcher(str);
		if(m.matches())
		{
			int grpCount = m.groupCount();
			try {
				int value;
				for (int i = 0; i <= grpCount; i++) {
					//System.out.println(i + ": " + m.group(i));
					switch (i) {
					case 1:
						if (m.group(i).compareTo(PERMIT_TOKEN) == 0)
							actionIsPermit=true;
						if (m.group(i).compareTo(DENY_TOKEN) == 0)
							actionIsPermit=false;
						break;
					case 2:
						if (m.group(i).compareTo(IN_TOKEN) == 0)
							directionIsIn=true;
						if (m.group(i).compareTo(OUT_TOKEN) == 0)
							directionIsIn=false;
						break;
					case 3:
						proto = m.group(i);
						break;
					case 4:
						//src Host
						srcHost=m.group(i);
						break;
					case 6:
					case 7:
					case 8:
					case 9:
						// single parts of the IP Address
						value = Integer.parseInt(m.group(i));
						if(value<0 && value>256)
							throw new IllegalArgumentException("The Src IP Address is not a valid Address !");
						break;
					case 10:
					case 11:
					case 12:
						break;
					case 13:
						
						srcPort=m.group(i);
						break;
					case 14:
						//dst Host
						dstHost=m.group(i);
						break;
					case 16:
					case 17:
					case 18:
					case 19:
						// single parts of the IP Address
						value = Integer.parseInt(m.group(i));
						if(value<0 && value>256)
							throw new IllegalArgumentException("The Src IP Address is not a valid Address !");
						break;
					case 20:
					case 21:
					case 22:
						break;
					case 23:
						dstPort=m.group(i);
						break;
					case 24:
						if(m.group(i)!=null)
							options=m.group(i).trim();
						else
							options = null;
						break;
					}
				}
			}  catch(IllegalArgumentException e1)	{
				throw new IllegalAVPDataException(e1.getMessage());
			}
			
			lastParsedString = str;
		}
		else
		{
			throw new IllegalArgumentException("String does not match to the given Pattern "+str);
		}
		
	}
	/**
	 * checks if the member variables are up to date
	 * lastParsedString includes the String corresponding to the current member variables
	 */
	private void checkUpdating()	{
		if(lastParsedString.compareTo(this.getOctetString())!=0)	{
			try {
				this.updateMembers();
			} catch (IllegalAVPDataException e) {
				
				e.printStackTrace();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see de.fhg.fokus.diameter.DiameterPeer.data.AVP#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer();
//		sbf.append(commonInfo());
		sbf.append(" IPFilterRule="+this.getOctetString());
		return sbf.toString();
	}
		
}
