/**
 * Sourced from https://support.datastax.com/entries/22391451-Converting-TimeUUID-Strings-to-Dates
 */
package org.tclone;

import java.util.Date;
import java.util.UUID;

public class UUIDToDate {
	// This method comes from Hector's TimeUUIDUtils class:
	// https://github.com/rantav/hector/blob/master/core/src/main/java/me/...
	static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 0x01b21dd213814000L;
	public static long getTimeFromUUID(UUID uuid) {
		return (uuid.timestamp() - NUM_100NS_INTERVALS_SINCE_UUID_EPOCH) / 10000;
	}
}
