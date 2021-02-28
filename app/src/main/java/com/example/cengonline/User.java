package com.example.cengonline;

import java.io.Serializable;

public class User implements Serializable {
    private String ID;
    private String name;
    private int numberofCourses;
    private String username;
    private String password;
    private String department;
    private boolean usertype;

    public User(String ID, String name, int numberofCourses, String username, String password, String department, boolean usertype) {
        this.ID = ID;
        this.name = name;
        this.numberofCourses = numberofCourses;
        this.username = username;
        this.password = password;
        this.department = department;
        this.usertype = usertype;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String ID, String name, String department,String password, String username){
        this.ID = ID;
        this.name = name;
        this.department = department;
        this.password = password;
        this.username = username;
    }


    public User() {
    }

    public User(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getNumberofCourses() {
        return numberofCourses;
    }

    public void setNumberofCourses(int numberofCourses) {
        this.numberofCourses = numberofCourses;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isUsertype() { return usertype; }

    public void setUsertype(boolean usertype) { this.usertype = usertype; }
}
