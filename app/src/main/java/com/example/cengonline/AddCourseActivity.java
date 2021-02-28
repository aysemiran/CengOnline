package com.example.cengonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddCourseActivity extends AppCompatActivity {

    private DatabaseReference dataRefUser, operationRef;
    private EditText enterCourseName;
    private EditText enterCourseID;
    private EditText numberOfStudents;
    private Button addCourse;
    private Button backCoursePage;
    private List<Courses> courses = new ArrayList<>();
    private List<String> courseList = new ArrayList<>();
    private List<String> sendCourses = new ArrayList<>();
    private Teacher teacher;
    private String courseID, courseName, lecturerGiven;
    private int nStudentTake;
    private Courses course;
    private long courseCount;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        ImageView myImageView = (ImageView) findViewById(R.id.logo);
        myImageView.setImageResource(R.drawable.logo);
        enterCourseName = (EditText) findViewById(R.id.enterCourseName);
        enterCourseID = (EditText) findViewById(R.id.enterCourseID);
        numberOfStudents = (EditText) findViewById(R.id.numStudentsTaken);
        addCourse = (Button) findViewById(R.id.addCourse);
        backCoursePage = (Button) findViewById(R.id.backCoursePage);

        operationRef = FirebaseDatabase.getInstance().getReference().child("Courses");
        dataRefUser = FirebaseDatabase.getInstance().getReference().child("User");
        teacher = (Teacher) getIntent().getSerializableExtra("teacher");
        sendCourses = getIntent().getStringArrayListExtra("course");
        user = (User) getIntent().getSerializableExtra("user");

        getInformation();

        backCoursePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddCourseActivity.this, TeacherCourseActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("teacher", teacher);
                b.putSerializable("user", user);
                i.putStringArrayListExtra("course", (ArrayList<String>) sendCourses);
                i.putExtras(b);
                startActivity(i);
            }
        });

        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operationRef.child("c" + (courseCount + 1)).child("courseID").setValue(enterCourseID.getText().toString());
                operationRef.child("c" + (courseCount + 1)).child("courseName").setValue(enterCourseName.getText().toString());
                operationRef.child("c" + (courseCount + 1)).child("numOfStudentsTaken").setValue(Integer.parseInt(numberOfStudents.getText().toString()));
                operationRef.child("c" + (courseCount + 1)).child("lecturerGiven").setValue(teacher.getName());
                Toast.makeText(AddCourseActivity.this, "New Course is Here!", Toast.LENGTH_SHORT).show();
                dataRefUser.child(teacher.getID()).child("courses").child("c" + (courseCount + 1)).setValue(enterCourseID.getText().toString());
                courseCount++;
                getInformation();
                sendCourses.add(enterCourseID.getText().toString());
                Intent intent = new Intent(AddCourseActivity.this, AddStudentsCourseActivity.class);
                Bundle b = new Bundle();
                b.putLong("courseCount" , courseCount);
                b.putString("courseID", enterCourseID.getText().toString());
                b.putString("courseName", enterCourseName.getText().toString());
                b.putSerializable("teacher", teacher);
                b.putSerializable("course", course);
                b.putSerializable("user", user);
                intent.putStringArrayListExtra("courseList", (ArrayList<String>) sendCourses);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    public void getInformation() {
        operationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    courseCount = Integer.parseInt(courseSnapshot.getKey().substring(1));
                    for (DataSnapshot cSnapshot : courseSnapshot.getChildren()) {
                        if (cSnapshot.getKey().equalsIgnoreCase("courseID")) {
                            courseID = cSnapshot.getValue().toString();
                        } else if (cSnapshot.getKey().equalsIgnoreCase("courseName")) {
                            courseName = cSnapshot.getValue().toString();
                        } else if (cSnapshot.getKey().equalsIgnoreCase("numOfStudentsTaken")) {
                            nStudentTake = Integer.parseInt(cSnapshot.getValue().toString());
                        } else if (cSnapshot.getKey().equalsIgnoreCase("lecturerGiven")) {
                            lecturerGiven = cSnapshot.getValue().toString();
                        }

                        course = new Courses(courseID, courseName, nStudentTake, teacher);
                        courses.add(course);
                        courseList.add(courseID + " - " + courseName);
                    }
                    for (int i = 0; i < courseCount; i++) {
                        courseList.add(courses.get(i) .getID() + " - " + courses.get(i).getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}