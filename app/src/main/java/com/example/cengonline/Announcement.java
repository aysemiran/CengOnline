package com.example.cengonline;

import java.io.Serializable;
import java.util.Date;

public class Announcement implements Serializable {
    private String announcementID;
    private String announcementContext;
    private Date date;

    public Announcement(String announcementID, String announcementContext, Date date) {
        this.announcementID = announcementID;
        this.announcementContext = announcementContext;
        this.date = date;
    }

    public String getAnnouncementID() {
        return announcementID;
    }

    public void setAnnouncementID(String announcementID) {
        this.announcementID = announcementID;
    }

    public String getAnnouncementContext() {
        return announcementContext;
    }

    public void setAnnouncementContext(String announcementContext) {
        this.announcementContext = announcementContext;
    }
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
