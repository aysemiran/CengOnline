package com.example.cengonline;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student extends User implements Serializable{

    private List<Courses> courses;
    private List<Assignment> assignments;
    public Student(String ID, String name, int numberofCourses, String username, String password, String department, boolean usertype) {
        super(ID,name,numberofCourses,username,password,department,usertype);
        courses = new ArrayList<Courses>(numberofCourses);
        assignments=new ArrayList<>();

    }

    public Student(){
    }

    public void setCourses(List<Courses> courses) {
        this.courses = courses;
    }

    public List<Courses> getCourses() {
        return courses;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public boolean addCourse(Courses course)
    {
        if(courses.contains(course)) return false;
        else
        {
            courses.add(course);
            super.setNumberofCourses(super.getNumberofCourses()+1);
            return true;
        }
    }
}
