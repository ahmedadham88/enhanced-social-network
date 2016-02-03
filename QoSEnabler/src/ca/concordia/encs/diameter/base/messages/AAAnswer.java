
package ca.concordia.encs.diameter.base.messages;

import ca.concordia.encs.diameter.base.constants.IMessageCode;

/**
 * The AA-Answer (AAA) message is indicated by setting the Command-Code
 * field to 265 and clearing the 'R' bit in the Command Flags field.  It
 * is sent in response to the AA-Request (AAR) message.  If
 * authorization was requested, a successful response will include the
 * authorization AVPs appropriate for the service being provided, as
 * defined in section 6.
 *
 * For authentication exchanges requiring more than a single round trip,
 * the server MUST set the Result-Code AVP to DIAMETER_MULTI_ROUND_AUTH.
 * An AAA message with this result code MAY include one Reply-Message or
 * more and MAY include zero or one State AVPs.
 *
 * If the Reply-Message AVP was present, the network access server
 * SHOULD send the text to the user's client to display to the user,
 * instructing the client to prompt the user for a response.  For
 * example, this capability can be achieved in PPP via PAP.  If the
 * access client is unable to prompt the user for a new response, it
 * MUST treat the AA-Answer (AAA) with the Reply-Message AVP as an error
 * and deny access.
 *
 * Message Format
 *
 *    <AA-Answer> ::= < Diameter Header: 265, PXY >
 *                    < Session-Id >
 *                    { Auth-Application-Id }
 *                    { Auth-Request-Type }
 *                    { Result-Code }
 *                    { Origin-Host }
 *                    { Origin-Realm }
 *                    [ User-Name ]
 *                    [ Service-Type ]
 *                  * [ Class ]
 *                  * [ Configuration-Token ]
 *                    [ Acct-Interim-Interval ]
 *                    [ Error-Message ]
 *                    [ Error-Reporting-Host ]
 *                  * [ Failed-AVP ]
 *                    [ Idle-Timeout ]
 *                    [ Authorization-Lifetime ]
 *                    [ Auth-Grace-Period ]
 *                    [ Auth-Session-State ]
 *                    [ Re-Auth-Request-Type ]
 *                    [ Multi-Round-Time-Out ]
 *                    [ Session-Timeout ]
 *                    [ State ]
 *                  * [ Reply-Message ]
 *                    [ Origin-AAA-Protocol ]
 *                    [ Origin-State-Id ]
 *                  * [ Filter-Id ]
 *                    [ Password-Retry ]
 *                    [ Port-Limit ]
 *                    [ Prompt ]
 *                    [ ARAP-Challenge-Response ]
 *                    [ ARAP-Features ]
 *                    [ ARAP-Security ]
 *                  * [ ARAP-Security-Data ]
 *                    [ ARAP-Zone-Access ]
 *                    [ Callback-Id ]
 *                    [ Callback-Number ]
 *                    [ Framed-Appletalk-Link ]
 *                  * [ Framed-Appletalk-Network ]
 *                    [ Framed-Appletalk-Zone ]
 *                  * [ Framed-Compression ]
 *                    [ Framed-Interface-Id ]
 *                    [ Framed-IP-Address ]
 *                  * [ Framed-IPv6-Prefix ]
 *                    [ Framed-IPv6-Pool ]
 *                  * [ Framed-IPv6-Route ]
 *                    [ Framed-IP-Netmask ]
 *                  * [ Framed-Route ]
 *                    [ Framed-Pool ]
 *                    [ Framed-IPX-Network ]
 *                    [ Framed-MTU ]
 *                    [ Framed-Protocol ]
 *                    [ Framed-Routing ]
 *                  * [ Login-IP-Host ]
 *                  * [ Login-IPv6-Host ]
 *                    [ Login-LAT-Group ]
 *                    [ Login-LAT-Node ]
 *                    [ Login-LAT-Port ]
 *                    [ Login-LAT-Service ]
 *                    [ Login-Service ]
 *                    [ Login-TCP-Port ]
 *                  * [ NAS-Filter-Rule ]
 *                  * [ QoS-Filter-Rule ]
 *                  * [ Tunneling ]
 *                  * [ Redirect-Host ]
 *                    [ Redirect-Host-Usage ]
 *                    [ Redirect-Max-Cache-Time ]
 *                  * [ Proxy-Info ]
 *                  * [ AVP ]
 *
 * RFC 4005
 *
 *
 */
public class AAAnswer extends DiameterAnswer {
	
	public AAAnswer(int _CommandCode, int _ApplicationId){
		super(_CommandCode, _ApplicationId);
	}
}
