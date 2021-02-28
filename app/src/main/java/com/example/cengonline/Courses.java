package com.example.cengonline;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Courses implements Serializable {
    private String ID;
    private String courseName;
    private int numOfStudentsTaken;
    private Teacher lecturerGiven;
    private List<Student> students;
    private List<Announcement> announcements;
    private List<Assignment> assignments;

    public Courses() {
    }

    public Courses(String ID, String name, int numOfStudentsTaken, Teacher lecturerGiven) {
        this.ID = ID;
        this.courseName = name;
        this.numOfStudentsTaken = numOfStudentsTaken;
        this.lecturerGiven = lecturerGiven;
        this.students = new ArrayList<>(numOfStudentsTaken);
    }
    public Courses(String ID, String name, int numOfStudentsTaken)
    {
        this.ID = ID;
        this.courseName = name;
        this.numOfStudentsTaken = numOfStudentsTaken;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return courseName;
    }

    public void setName(String name) {
        this.courseName = courseName;
    }

    public int getNumOfStudentsTaken() {
        return numOfStudentsTaken;
    }

    public void setNumOfStudentsTaken(int numOfStudentsTaken) {
        this.numOfStudentsTaken = numOfStudentsTaken;
    }

    public Teacher getLecturerGiven() {
        return lecturerGiven;
    }

    public void setLecturerGiven(Teacher lecturerGiven) {
        this.lecturerGiven = lecturerGiven;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    /*** Student Functions */
    public boolean addNewStudent(Student newStudent) {
        if (students.contains(newStudent)) return false;
        else {
            students.add(newStudent);
            this.numOfStudentsTaken++;
            return true;
        }
    }

    public boolean addNewAnnouncement(Announcement newAnnouncement) {
        if (announcements.contains(newAnnouncement)) return false;
        else {
            announcements.add(newAnnouncement);
            return true;
        }
    }

    public boolean editAnnouncement(Announcement announcement) {
        if (announcements.contains(announcement)) return false;
        else {
            for (int i = 0; i < announcements.size(); i++) {
                if (announcements.get(i).getAnnouncementID().equalsIgnoreCase(announcement.getAnnouncementID())) {
                    announcements.set(i, announcement);
                }
            }
            return true;
        }
    }

    public boolean deleteAnnouncement(Announcement announcement) {
        if (announcements.contains(announcement)) return false;
        else {
            for (int i = 0; i < announcements.size(); i++) {
                if (announcements.get(i).getAnnouncementID().equalsIgnoreCase(announcement.getAnnouncementID())) {
                    announcements.remove(i);
                }
            }
            return true;
        }
    }

    public String viewAnnouncement(Announcement announcement) {
        String output="";
        if(announcements.contains(announcement))
            output="Cannot find this announcement.";
        else
        {
            for (int i = 0; i < announcements.size(); i++) {
                if (announcements.get(i).getAnnouncementID().equalsIgnoreCase(announcement.getAnnouncementID()))
                {
                    output=announcements.get(i).getAnnouncementID()+" "+announcements.get(i).getAnnouncementContext();
                }
            }
        }
        return output;
    }

    public boolean addNewAssignment(Assignment newAssignment) {
        if(assignments.contains(newAssignment)) return false;
        else
        {
            assignments.add(newAssignment);
            return true;
        }
    }

    public boolean editAssignment(Assignment assignment) {
        if(assignments.contains(assignment)) return false;
        else
        {
            for (int i=0;i<assignments.size();i++)
            {
                if(assignments.get(i).getAssignmentID().equalsIgnoreCase(assignment.getAssignmentID()))
                {
                    assignments.set(i,assignment);
                }
            }
            return true;
        }
    }

    public boolean deleteAssignment(Assignment assignment) {
        if(assignments.contains(assignment)) return false;
        else
        {
            for (int i=0;i<assignments.size();i++)
            {
                if(assignments.get(i).getAssignmentID().equalsIgnoreCase(assignment.getAssignmentID()))
                {
                    assignments.remove(i);
                }
            }
            return true;
        }
    }

    public String viewAssignment(Assignment assignment) {
        String output="";
        if(assignments.contains(assignment))
            output="Cannot find this assignment.";
        else
        {
            for (int i = 0; i < assignments.size(); i++) {
                if (assignments.get(i).getAssignmentID().equalsIgnoreCase(assignment.getAssignmentID()))
                {
                    output=assignments.get(i).getAssignmentID()+" "+assignments.get(i).getAssingmentName()
                            +" "+assignments.get(i).getAssignmentContext();
                }
            }
        }
        return output;
    }


}
