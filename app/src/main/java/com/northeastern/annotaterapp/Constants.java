package com.northeastern.annotaterapp;

/**
 * Collection of all the constants.
 */
public final class Constants {
    /**
     * 15 minute intervals for the worker. This is also the minimum interval for a worker. For other
     * lower intervals we might have to use other implementations such as AlarmManager (Deprecated
     * as of newer APIs).
     */
    public static final int INTERVAL_TIME = 15;

    /**
     * According to spec this was the list of acceptable actions
     */
    public static final String[] ACCEPTABLE_ACTIONS = {
            "cooking",
            "walking",
            "sitting",
            "working",
            "driving",
            "watching",
    };

    /**
     * Format of timestamp needed for logging activities.
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd H:m:s";

}
