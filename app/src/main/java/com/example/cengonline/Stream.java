package com.example.cengonline;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Stream implements Serializable {

    private String postID;
    private String postContext;
    private Date date;
    private String teacherUsername;
    private List<HashMap<String, String>> comments; //user id, comment

    public Stream(String postID, String postContext, Date date, String teacherUsername){
        this.postID = postID;
        this.postContext = postContext;
        this.date = date;
        this.teacherUsername = teacherUsername;
        this.comments=new ArrayList<>();
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostContext() {
        return postContext;
    }

    public void setPostContext(String postContext) {
        this.postContext = postContext;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTeacherUsername() {
        return teacherUsername;
    }

    public void setTeacherUsername(String teacherUsername) {
        this.teacherUsername = teacherUsername;
    }

    public List<HashMap<String, String>> getComments() {
        return comments;
    }

}
