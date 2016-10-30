package com.shaftware;

/**
 * Created by Steven Bertolucci on 10/30/2016.
 * Contains all data to be sent to server when a message is
 * sent. Contains message text, sender name, user image URL,
 * and timestamp.
 */

public class MessagePacket {

    private String text;
    private String name;
    private String photoURL;
    private String timestamp;

    public MessagePacket(String text, String name, String photoURL, String timestamp) {
        this.text = text;
        this.name = name;
        this.photoURL = photoURL;
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
