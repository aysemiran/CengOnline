package com.example.cengonline;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Teacher extends User implements Serializable {

    private List<Courses> courses;

    public Teacher(String ID, String name, int numberofCourses, String username, String password, String department, boolean usertype) {
        super(ID, name, numberofCourses, username, password, department,  usertype);
        courses = new ArrayList<Courses>(super.getNumberofCourses());
    }

    public Teacher() {
    }

    public Teacher(String ID){
        super(ID);
    }



    public List<Courses> getCourses() {
        return courses;
    }

    public void setCourses(List<Courses> courses) {
        this.courses = courses;
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
    public boolean editCourse(Courses course)
    {
        if(!courses.contains(course)) return false;
        else
        {
            for (int i=0;i<getCourses().size();i++)
            {
                if (courses.get(i).getID() == course.getID()) {
                    courses.get(i).setName(course.getName());
                    courses.get(i).setNumOfStudentsTaken(course.getNumOfStudentsTaken());
                }
            }

        }
        return true;
    }

    public boolean deleteCourse(Courses course)
    {
        if(!courses.contains(course)) return false;
        else {
            courses.remove(course);
            super.setNumberofCourses(super.getNumberofCourses()-1);

            return true;
        }
    }
    public String printAllCourses() {

        return null;
    }
}
