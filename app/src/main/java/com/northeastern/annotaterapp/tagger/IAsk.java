package com.northeastern.annotaterapp.tagger;

public interface IAsk {
    /**
     * Method allows you to ask the user their current activity.
     */
    void getOneSentence();

    /**
     * Force close asking.
     */
    void stopListening();
}
