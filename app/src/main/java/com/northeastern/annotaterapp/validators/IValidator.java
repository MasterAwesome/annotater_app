package com.northeastern.annotaterapp.validators;

/**
 * Validator interface for string inputs.
 */
public interface IValidator {
    /**
     * Validates the given input to see if it's a part of ACCEPTABLE_ACTIONS defined in {@link
     * com.northeastern.annotaterapp.Constants}.
     *
     * @param input input to the system
     * @return true if valid.
     */
    boolean isValid(String input);
}
