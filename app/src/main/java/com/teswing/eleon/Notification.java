package com.teswing.eleon;

public class Notification {

    private final String title;
    private final String message;

    Notification(String title, String message){
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

}
