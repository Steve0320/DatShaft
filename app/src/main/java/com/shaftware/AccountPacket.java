package com.shaftware;

/**
 * Created by Steven Bertolucci on 11/24/2016.
 */

public class AccountPacket {

    public String handle;
    public String language;

    public AccountPacket() {

    }

    public AccountPacket(String handle, String language) {
        this.handle = handle;
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }
}
