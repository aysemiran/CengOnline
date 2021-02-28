package com.example.cengonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class AddStudentsCourseActivity extends AppCompatActivity {

    private DatabaseReference dataStudentRef, dataCourseRef, operationRef;
    private TextView coursePage;
    private EditText enterStudentID;
    private Button addStudent;
    private Button backCoursePage;
    private String studentID, enterCourseID;
    private Teacher teacher;
    private Courses course;
    private long courseCount;
    private List<String> sendCourses = new ArrayList<>();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_students_course);

        ImageView myImageView = (ImageView) findViewById(R.id.logo);
        myImageView.setImageResource(R.drawable.logo);
        coursePage = (TextView) findViewById(R.id.coursePage);
        enterStudentID = (EditText) findViewById(R.id.enterStudentID);
        addStudent = (Button) findViewById(R.id.addStudent);
        backCoursePage = (Button) findViewById(R.id.backCoursePage);

        dataStudentRef = FirebaseDatabase.getInstance().getReference().child("User");
        dataCourseRef = FirebaseDatabase.getInstance().getReference().child("Courses");
        teacher = (Teacher) getIntent().getSerializableExtra("teacher");
        enterCourseID = (String) getIntent().getSerializableExtra("courseID");
        sendCourses = getIntent().getStringArrayListExtra("courseList");
        user = (User) getIntent().getSerializableExtra("user");

        backCoursePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddStudentsCourseActivity.this, TeacherCourseActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("teacher", teacher);
                b.putSerializable("user", user);
                i.putStringArrayListExtra("course", (ArrayList<String>) sendCourses);
                i.putExtras(b);
                startActivity(i);
            }
        });

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentIDMatch();
                Toast.makeText(AddStudentsCourseActivity.this, "The student added to the course.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void studentIDMatch() {
        dataStudentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot studentSnapshot : snapshot.getChildren()) {
                        if (studentSnapshot.getKey().equals("ID")) {
                            studentID = studentSnapshot.getValue().toString();
                        }
                        if (studentID.equals(enterStudentID.getText().toString())) {
                            String studentKey = snapshot.getKey();
                            if (studentSnapshot.getKey().equals("courses")) {
                                for (DataSnapshot stuSnapshot : studentSnapshot.getChildren()) {
                                    courseCount = Integer.parseInt(stuSnapshot.getKey().substring(1));
                                }
                                dataStudentRef.child(studentKey).child("courses").child("c" + (courseCount + 1)).setValue(enterCourseID);
                                courseCount++;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}