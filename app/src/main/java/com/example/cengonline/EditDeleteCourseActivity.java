package com.example.cengonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

public class EditDeleteCourseActivity extends AppCompatActivity {

    private DatabaseReference dataOperationRef, operationRef, userRef;
    private ListView listView;
    private TextView coursePage;
    private TextView selectedCourse;
    private EditText editCourseName, editNumStudents;
    private Button editCourse;
    private Button deleteCourse;
    private Button backCoursePage;
    private Teacher teacher;
    private User user;
    private String courseID, courseName, userKey;
    private List<String> courses = new ArrayList<>();
    private List<String> coursesList = new ArrayList<>();
    private Courses course;
    private String selectedFromList;
    private String[] courseInfo;
    private String courseKey, userCourseKey;
    private int count = 0;
    private CustomAdapter customAdapter;
    private List<String> courseList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_course);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ImageView myImageView = (ImageView) findViewById(R.id.logo);
        myImageView.setImageResource(R.drawable.logo);
        coursePage = (TextView) findViewById(R.id.coursePage);
        selectedCourse = (TextView) findViewById(R.id.selectedCourse);
        editCourseName = (EditText) findViewById(R.id.editCourseName);
        editNumStudents = (EditText) findViewById(R.id.editNumStudents);
        editCourse = (Button) findViewById(R.id.editCourse);
        deleteCourse = (Button) findViewById(R.id.deleteCourse);
        backCoursePage = (Button) findViewById(R.id.backCoursePage);
        listView = findViewById(R.id.courseListEdit);

        dataOperationRef = FirebaseDatabase.getInstance().getReference().child("Courses");
        operationRef = FirebaseDatabase.getInstance().getReference().child("Courses");
        userRef = FirebaseDatabase.getInstance().getReference().child("User");

        teacher = (Teacher) getIntent().getSerializableExtra("teacher");
        courses = getIntent().getStringArrayListExtra("course");

        getData();

       CustomAdapter customAdapter = new CustomAdapter(EditDeleteCourseActivity.this, R.layout.activity_rowcustom, (ArrayList<String>) courseList);
       listView.setAdapter(customAdapter);

        editCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editCourseName.getText().toString().equals("")) {
                    selectedCourse.setText(courseID + " - " + editCourseName.getText().toString());
                    operationRef.child(courseKey).child("courseName").setValue(editCourseName.getText().toString());
                    Toast.makeText(EditDeleteCourseActivity.this, "Course Name is Edited Successfully", Toast.LENGTH_LONG).show();
                }
                if (!editNumStudents.getText().toString().equals("")) {
                    operationRef.child(courseKey).child("numOfStudentsTaken").setValue(editNumStudents.getText().toString());
                    Toast.makeText(EditDeleteCourseActivity.this, "Number of Students Taking the Course is Edited Successfully", Toast.LENGTH_LONG).show();
                }
            }
        });

        deleteCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserCourses();
                operationRef.child(courseKey).removeValue();
                Toast.makeText(EditDeleteCourseActivity.this, "Course Name is Deleted Successfully.", Toast.LENGTH_SHORT).show();
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedFromList = (String) listView.getItemAtPosition(position);
                courseInfo = selectedFromList.split(" - ");
                courseID = courseInfo[0];
                courseName = courseInfo[1];
                selectedCourse.setText(courseInfo[0] + " - " + courseInfo[1]);

                listView.setVisibility(View.INVISIBLE);

                selectedCourse.setVisibility(View.VISIBLE);
                editCourse.setVisibility(View.VISIBLE);
                deleteCourse.setVisibility(View.VISIBLE);
                editCourseName.setVisibility(View.VISIBLE);
                editNumStudents.setVisibility(View.VISIBLE);
                changeSelectedCourseName();
                countUserCourse();

            }
        });

        backCoursePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditDeleteCourseActivity.this, TeacherCourseActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("teacher", teacher);
                b.putSerializable("user", user);
                i.putStringArrayListExtra("course", (ArrayList<String>) courses);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    public void changeSelectedCourseName() {
        dataOperationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    if (courseSnapshot.child("courseID").getValue().toString().equals(courseID)) {
                        courseKey = courseSnapshot.getKey();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void deleteUserCourses() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot uSnapshot : userSnapshot.getChildren()) {
                        if (uSnapshot.getKey().equals("courses")) {
                            for (DataSnapshot usrSnapshot : uSnapshot.getChildren()) {
                                if (usrSnapshot.getValue().toString().equals(courseID)) {
                                    userCourseKey = usrSnapshot.getKey();
                                    userKey = userSnapshot.getKey();
                                    userRef.child(userKey).child("courses").child(userCourseKey).removeValue();
                                }
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

    public void countUserCourse() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot uSnapshot : userSnapshot.getChildren()) {
                        if (uSnapshot.getKey().equals("courses")) {
                            for (DataSnapshot usrSnapshot : uSnapshot.getChildren()) {
                                if (usrSnapshot.getValue().toString().equals(courseID)) {
                                    count++;
                                }
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

    public void getData()
    {
        dataOperationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot cSnapshot : courseSnapshot.getChildren()) {
                        for (int i = 0; i < courses.size(); i++) {
                            if (cSnapshot.getValue().toString().equals(courses.get(i))) {
                                String className = courseSnapshot.child("courseName").getValue().toString();
                                String cID = courseSnapshot.child("courseID").getValue().toString();
                                courseList.add(cID+ " - " +className);
                            }
                        }
                    }
                }
                customAdapter = new CustomAdapter(EditDeleteCourseActivity.this, R.layout.activity_rowcustom, (ArrayList<String>) courseList);
                listView.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}