package util;

import java.security.InvalidParameterException;

import org.apache.log4j.Logger;

import ca.concordia.encs.constants.EventNotificationType;
import ca.concordia.encs.constants.MediaType;
import ca.concordia.encs.diameter.rx.avp.SpecificAction;

public final class UtilHelper {
	private static final Logger LOGGER = Logger.getLogger(UtilHelper.class);

	public static int mediaTypeToDiameterMediaType(MediaType mediaType) {
		LOGGER.info("getDiameterMediaType method called.");

		int diameterMediaType = -1;
		if (mediaType == MediaType.APPLICATION)
			diameterMediaType = ca.concordia.encs.diameter.rx.avp.MediaType.APPLICATION;
		else if (mediaType == MediaType.AUDIO)
			diameterMediaType = ca.concordia.encs.diameter.rx.avp.MediaType.AUDIO;
		if (mediaType == MediaType.DATA)
			diameterMediaType = ca.concordia.encs.diameter.rx.avp.MediaType.DATA;
		if (mediaType == MediaType.OTHER)
			diameterMediaType = ca.concordia.encs.diameter.rx.avp.MediaType.OTHER;
		if (mediaType == MediaType.VIDEO)
			diameterMediaType = ca.concordia.encs.diameter.rx.avp.MediaType.VIDEO;
		else if (mediaType == MediaType.APPLICATION)
			diameterMediaType = -1;

		LOGGER.debug("Diameter media type = " + diameterMediaType);
		return diameterMediaType;
	}

	public static int eventNotificationTypeToSpecifcAction(
			EventNotificationType eventNotificationType) {

		int specificAction = -1;
		switch (eventNotificationType) {
		case CHARGING_CORRELATION_EXCHANGE:
			specificAction = SpecificAction.CHARGING_CORRELATION_EXCHANGE;
			break;
		case INDICATION_OF_LOSS_OF_BEARER:
			specificAction = SpecificAction.INDICATION_OF_LOSS_OF_BEARER;
			break;
		case INDICATION_OF_RECOVERY_OF_BEARER:
			specificAction = SpecificAction.INDICATION_OF_RECOVERY_OF_BEARER;
			break;
		case INDICATION_OF_RELEASE_OF_BEARER:
			specificAction = SpecificAction.INDICATION_OF_RELEASE_OF_BEARER;
			break;
		case IP_CAN_CHANGE:
			specificAction = SpecificAction.IP_CAN_CHANGE;
			break;
		case INDICATION_OF_OUT_OF_CREDIT:
			specificAction = SpecificAction.INDICATION_OF_OUT_OF_CREDIT;
			break;
		case INDICATION_OF_SUCCESSFUL_RESOURCES_ALLOCATION:
			specificAction = SpecificAction.INDICATION_OF_SUCCESSFUL_RESOURCES_ALLOCATION;
			break;
		case INDICATION_OF_FAILED_RESOURCES_ALLOCATION:
			specificAction = SpecificAction.INDICATION_OF_FAILED_RESOURCES_ALLOCATION;
			break;
		case INDICATION_OF_LIMITED_PCC_DEPLOYMENT:
			specificAction = SpecificAction.INDICATION_OF_LIMITED_PCC_DEPLOYMENT;
			break;
		default:
			specificAction = -1;
		}

		return specificAction;
	}

	public static EventNotificationType specifcActionToEventNotificationTypeTo(
			int specifcAction) {

		EventNotificationType eventNotificationType;
		switch (specifcAction) {
		case SpecificAction.CHARGING_CORRELATION_EXCHANGE:
			eventNotificationType = EventNotificationType.CHARGING_CORRELATION_EXCHANGE;
			break;

		case SpecificAction.INDICATION_OF_LOSS_OF_BEARER:
			eventNotificationType = EventNotificationType.INDICATION_OF_LOSS_OF_BEARER;
			break;

		case SpecificAction.INDICATION_OF_RECOVERY_OF_BEARER:
			eventNotificationType = EventNotificationType.INDICATION_OF_RECOVERY_OF_BEARER;
			break;

		case SpecificAction.INDICATION_OF_RELEASE_OF_BEARER:
			eventNotificationType = EventNotificationType.INDICATION_OF_RELEASE_OF_BEARER;
			break;

		case SpecificAction.IP_CAN_CHANGE:
			eventNotificationType = EventNotificationType.IP_CAN_CHANGE;
			break;

		case SpecificAction.INDICATION_OF_OUT_OF_CREDIT:
			eventNotificationType = EventNotificationType.INDICATION_OF_OUT_OF_CREDIT;
			break;

		case SpecificAction.INDICATION_OF_SUCCESSFUL_RESOURCES_ALLOCATION:
			eventNotificationType = EventNotificationType.INDICATION_OF_SUCCESSFUL_RESOURCES_ALLOCATION;
			break;

		case SpecificAction.INDICATION_OF_FAILED_RESOURCES_ALLOCATION:
			eventNotificationType = EventNotificationType.INDICATION_OF_FAILED_RESOURCES_ALLOCATION;
			break;

		case SpecificAction.INDICATION_OF_LIMITED_PCC_DEPLOYMENT:
			eventNotificationType = EventNotificationType.INDICATION_OF_LIMITED_PCC_DEPLOYMENT;
			break;

		default:
			eventNotificationType = null;
		}

		return eventNotificationType;
	}
}
