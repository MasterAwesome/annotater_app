package com.northeastern.annotaterapp;

import com.northeastern.annotaterapp.tagger.IAsk;

/**
 * This is used to return result from the call to getOneSentence() from {@link IAsk}.
 */
public interface ICallback {
    enum StatusCode {
        FAILURE,
        SUCCESS,
        IN_PROGRESS
    }

    /**
     * To be called when status change occurs.
     *
     * @param statusCode status of this callback.
     * @param data       this is required to be not null only when statusCode is SUCCESS
     */
    void handleCallback(StatusCode statusCode, String data);

}
