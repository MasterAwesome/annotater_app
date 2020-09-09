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
