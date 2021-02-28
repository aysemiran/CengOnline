package com.example.cengonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherCourseActivity extends AppCompatActivity {

    private DatabaseReference dataOperationRef;
    private ListView listView;
    private TextView coursePage;
    private Button messageButton;
    private Button addCourse;
    private Button editOrDelete;
    private Button logout;
    private ImageView myImageView;
    private Teacher teacher;
    private User user;
   private List<String> courses;
    private List<String> courseList;
    private Intent intent;
    private Courses course;
    private CustomAdapter customAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        intent = new Intent(this, TeacherActivity.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachercourse);
        courses = new ArrayList<>();
        courseList = new ArrayList<>();
        myImageView = findViewById(R.id.logo);
        myImageView.setImageResource(R.drawable.logo);
        logout = findViewById(R.id.logoutButton);
        coursePage = findViewById(R.id.coursePage);
        messageButton = findViewById(R.id.messageButton);
        addCourse = findViewById(R.id.addCourse);
        editOrDelete = findViewById(R.id.editOrDelete);
        listView = findViewById(R.id.courseList);

        dataOperationRef=FirebaseDatabase.getInstance().getReference().child("Courses");
        teacher = (Teacher) getIntent().getSerializableExtra("teacher");
        courses = getIntent().getStringArrayListExtra("course");
        user = (User) getIntent().getSerializableExtra("user");

        getData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFromList = (String) listView.getItemAtPosition(position);
                String courseID=courses.get(position);
                Intent sendUser = new Intent(TeacherCourseActivity.this,TeacherActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("teacher", teacher);
                b.putSerializable("user", user);
                b.putString("courseID",courseID);
                b.putString("courseInformation",selectedFromList);
                sendUser.putStringArrayListExtra("course", (ArrayList<String>) courses);
                sendUser.putExtras(b);
                startActivity(sendUser);
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendUser = new Intent(TeacherCourseActivity.this, MessageActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("user", user);
                b.putSerializable("teacher", teacher);
                sendUser.putStringArrayListExtra("course", (ArrayList<String>) courses);
                sendUser.putExtras(b);
                startActivity(sendUser);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherCourseActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeacherCourseActivity.this, AddCourseActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("teacher", teacher);
                b.putSerializable("user", user);
                i.putStringArrayListExtra("course", (ArrayList<String>) courses);
                i.putExtras(b);
                startActivity(i);
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
                customAdapter = new CustomAdapter(TeacherCourseActivity.this, R.layout.activity_rowcustom, (ArrayList<String>) courseList);
                listView.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void editOrDeleteOnClick(View view) {
        Intent intent = new Intent(getApplicationContext(), EditDeleteCourseActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("teacher", teacher);
        intent.putStringArrayListExtra("course", (ArrayList<String>) courses);
        intent.putExtras(b);
        startActivity(intent);
    }
}