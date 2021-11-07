/*
 * Copyright (C) 2020 Arvind Mukund <armu30@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
