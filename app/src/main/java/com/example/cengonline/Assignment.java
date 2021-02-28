package com.example.cengonline;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Assignment implements Serializable {
    private String assignmentID;
    private String assingmentName;
    private String assignmentContext;
    private Date date;
    private boolean isSubmit;
    private int numberOfSubmitters;

    public Assignment(String assignmentID, String assingmentName, String assignmentContext,Date date) {
        this.assignmentID = assignmentID;
        this.assingmentName = assingmentName;
        this.assignmentContext = assignmentContext;
        this.isSubmit = false;
        this.date=date;
    }
    public Assignment(String assignmentID, String assingmentName, String assignmentContext,Date date,int numberOfSubmitters) {
        this.assignmentID = assignmentID;
        this.assingmentName = assingmentName;
        this.assignmentContext = assignmentContext;
        this.isSubmit = false;
        this.date=date;
        this.numberOfSubmitters=numberOfSubmitters;
    }

    public String getAssignmentID() {
        return assignmentID;
    }

    public void setAssignmentID(String assignmentID) {
        this.assignmentID = assignmentID;
    }

    public String getAssingmentName() {
        return assingmentName;
    }

    public void setAssingmentName(String assingmentName) {
        this.assingmentName = assingmentName;
    }

    public String getAssignmentContext() {
        return assignmentContext;
    }

    public void setAssignmentContext(String assignmentContext) {
        this.assignmentContext = assignmentContext;
    }

    public boolean isSubmit() {
        return isSubmit;
    }

    public void setSubmit(boolean submit) {
        isSubmit = submit;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNumberOfSubmitters() {
        return numberOfSubmitters;
    }

    public void setNumberOfSubmitters(int numberOfSubmitters) {
        this.numberOfSubmitters = numberOfSubmitters;
    }
}
