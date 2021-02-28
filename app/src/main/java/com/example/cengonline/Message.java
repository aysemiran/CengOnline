package com.example.cengonline;

import java.util.Date;

public class Message {

    private User sender;
    private User receiver;
    private Date date;
    private String message;
    private boolean isRead;

    public Message(User sender, User receiver, Date date, String message, boolean isRead)
    {
        this.sender=sender;
        this.receiver=receiver;
        this.date=date;
        this.message=message;
        this.isRead=isRead;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
