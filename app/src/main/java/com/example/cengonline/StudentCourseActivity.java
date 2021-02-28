package com.example.cengonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class StudentCourseActivity extends AppCompatActivity  {
    private DatabaseReference dataOperationRef;
    private ListView listView;
    private TextView coursePage;
    private ImageView myImageView;
    private Student student;
    private User user;
    private List<String> courses;
    private List<String> courseList;
    private Button messageButton;
    private Button logout;
    private CustomAdapter customAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentcourse);
        courses = new ArrayList<>();
        courseList = new ArrayList<>();
        myImageView = findViewById(R.id.logo);
        myImageView.setImageResource(R.drawable.logo);
        coursePage = findViewById(R.id.coursePage);
        logout = findViewById(R.id.logoutButton);
        dataOperationRef=FirebaseDatabase.getInstance().getReference().child("Courses");
        student = (Student) getIntent().getSerializableExtra("student");
        courses = getIntent().getStringArrayListExtra("course");
        user = (User) getIntent().getSerializableExtra("user");
        messageButton = (Button) findViewById(R.id.messageButton);
        listView = findViewById(R.id.courseList);

        getData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFromList = (String) listView.getItemAtPosition(position);
                String courseID=courses.get(position);
                Intent sendUser = new Intent(StudentCourseActivity.this,StudentActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("student", student);
                b.putSerializable("user", user);
                b.putString("courseID",courseID);
                b.putString("courseInformation",selectedFromList);
                sendUser.putStringArrayListExtra("course", (ArrayList<String>) courses);
                sendUser.putExtras(b);
                startActivity(sendUser);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentCourseActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendUser = new Intent(StudentCourseActivity.this, MessageActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("student", student);
                b.putSerializable("user", user);
                sendUser.putStringArrayListExtra("course", (ArrayList<String>) courses);
                sendUser.putExtras(b);
                startActivity(sendUser);
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
                customAdapter = new CustomAdapter(StudentCourseActivity.this, R.layout.activity_rowcustom, (ArrayList<String>) courseList);
                listView.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}