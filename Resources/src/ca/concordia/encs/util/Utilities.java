package ca.concordia.encs.util;

import java.util.Date;
import java.util.UUID;

public class Utilities {

	public static boolean isNullOrEmpty(String value) {
		return value == null || value.length() == 0;
	}

	public static String getUniqueID(String moduleName, String prefix) {
		UUID uniqueKey = UUID.randomUUID();
		uniqueKey = UUID.randomUUID();
		return moduleName + ";"
				+ (!Utilities.isNullOrEmpty(prefix) ? prefix + ";" : "")
				+ uniqueKey.toString(); //+ (new Date()).toString();
	}

}
