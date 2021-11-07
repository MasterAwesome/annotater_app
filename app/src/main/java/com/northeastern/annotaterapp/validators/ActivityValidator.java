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

package com.northeastern.annotaterapp.validators;

import com.northeastern.annotaterapp.Constants;

public class ActivityValidator implements IValidator {
    @Override
    public boolean isValid(String input) {
        boolean valid = false;

        for (String action : Constants.ACCEPTABLE_ACTIONS) {
            if (input.toLowerCase().contains(action)) {
                valid = true;
                break;
            }
        }
        return valid;
    }
}
