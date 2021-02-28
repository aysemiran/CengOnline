package com.example.cengonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class EditDeleteTeacherActivity extends AppCompatActivity  {

    private DatabaseReference dataOperationRef, operationRef;
    private Button edit;
    private Button delete;
    private Button editTheTextButton;
    private Button viewUploadedAssignments;
    private Button backButton;
    private TextView context;
    private TextView courseName;
    private EditText editContext;
    private EditText editNameText;
    private ListView uploadedAssignmentsListView;
    private List<String> uploadedAssignments=new ArrayList<>();
    private Announcement announcement;
    private Assignment assignment;
    private boolean whichSelected;
    private boolean visibility=false;
    private String courseID;
    private String key;
    private String whoUploaded="",uploadDate="",assignContext="";
    private CustomAdapter customAdapter;
    private User user;
    private Teacher teacher;
    private List<String> courses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_teacher);
        courses = new ArrayList<>();
        edit = findViewById(R.id.edit);
        delete = findViewById(R.id.delete);
        context = findViewById(R.id.selected);
        courseName = findViewById(R.id.nameCourse);
        editContext = findViewById(R.id.editContext);
        editNameText = findViewById(R.id.editNameText);
        editTheTextButton = findViewById(R.id.editTheTextButton);
        viewUploadedAssignments= findViewById(R.id.seeUploadedAssignments);
        uploadedAssignmentsListView = findViewById(R.id.uploadedAssignList);
        backButton = findViewById(R.id.backButton);
        announcement = (Announcement) getIntent().getSerializableExtra("announcement");
        assignment = (Assignment) getIntent().getSerializableExtra("assignment");
        whichSelected = (Boolean) getIntent().getSerializableExtra("selected");
        courseID = getIntent().getStringExtra("courseID");
        teacher = (Teacher) getIntent().getSerializableExtra("teacher");
        user = (User) getIntent().getSerializableExtra("user");
        courses = getIntent().getStringArrayListExtra("course");

        dataOperationRef = FirebaseDatabase.getInstance().getReference().child("Courses");
        operationRef = FirebaseDatabase.getInstance().getReference().child("Courses");
        getData();
        customAdapter = new CustomAdapter(EditDeleteTeacherActivity.this, R.layout.activity_rowcustom, (ArrayList<String>) uploadedAssignments);

        processesOfTeacher(whichSelected);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditDeleteTeacherActivity.this, TeacherActivity.class);
                Bundle b = new Bundle();
                b.putString("courseID",courseID);
                b.putSerializable("user", user);
                b.putSerializable("teacher", teacher);
                intent.putStringArrayListExtra("course", (ArrayList<String>) courses);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }
    public void processesOfTeacher(boolean whichSelected)
    {
        if(whichSelected == true){
            viewUploadedAssignments.setVisibility(View.INVISIBLE);
            context.setText(announcement.getAnnouncementContext());
            key = "announcement" + announcement.getAnnouncementID();
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editContext.setVisibility(View.VISIBLE);
                    editContext.setText(announcement.getAnnouncementContext());
                    editTheTextButton.setVisibility(View.VISIBLE);
                    viewUploadedAssignments.setVisibility(View.INVISIBLE);
                    edit.setVisibility(View.INVISIBLE);
                    delete.setVisibility(View.INVISIBLE);
                }
            });

            editTheTextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    operationRef.child(key).child("announcementContext").setValue(editContext.getText().toString());
                    operationRef.child(key).child("publishedDate").setValue(getCurrentTime());
                    Toast.makeText(EditDeleteTeacherActivity.this,  "Announcement context is edited successfully.", Toast.LENGTH_SHORT).show();
                    announcement.setAnnouncementContext(editContext.getText().toString());
                    announcement.setDate(Calendar.getInstance().getTime());
                    Intent intent = new Intent(EditDeleteTeacherActivity.this, TeacherActivity.class);
                    Bundle b = new Bundle();
                    b.putString("courseID",courseID);
                    b.putSerializable("user", user);
                    b.putSerializable("teacher", teacher);
                    intent.putStringArrayListExtra("course", (ArrayList<String>) courses);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    operationRef.child(key).removeValue();
                    Toast.makeText(EditDeleteTeacherActivity.this,  "Announcement is deleted successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditDeleteTeacherActivity.this, TeacherActivity.class);
                    Bundle b = new Bundle();
                    b.putString("courseID",courseID);
                    b.putSerializable("user", user);
                    b.putSerializable("teacher", teacher);
                    intent.putStringArrayListExtra("course", (ArrayList<String>) courses);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
        }
        else{
            editNameText.setVisibility(View.INVISIBLE);
            editContext.setVisibility(View.INVISIBLE);
            editTheTextButton.setVisibility(View.INVISIBLE);
            viewUploadedAssignments.setVisibility(View.VISIBLE);
            assignment = (Assignment) getIntent().getSerializableExtra("assignment");
            context.setText(assignment.getAssignmentContext());
            key="assignment" + assignment.getAssignmentID();

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    operationRef=operationRef.child(key).getRef();
                    operationRef.removeValue();
                    Toast.makeText(EditDeleteTeacherActivity.this,  "Assignment is deleted successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditDeleteTeacherActivity.this, TeacherActivity.class);
                    Bundle b = new Bundle();
                    b.putString("courseID",courseID);
                    b.putSerializable("user", user);
                    b.putSerializable("teacher", teacher);
                    intent.putStringArrayListExtra("course", (ArrayList<String>) courses);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewUploadedAssignments.setVisibility(View.INVISIBLE);
                    uploadedAssignmentsListView.setVisibility(View.INVISIBLE);
                    edit.setVisibility(View.INVISIBLE);
                    delete.setVisibility(View.INVISIBLE);
                    editNameText.setVisibility(View.VISIBLE);
                    editNameText.setText(assignment.getAssingmentName());
                    editContext.setVisibility(View.VISIBLE);
                    editContext.setText(assignment.getAssignmentContext());
                    editTheTextButton.setVisibility(View.VISIBLE);
                    editTheTextButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            operationRef.child(key).child("assignmentName").setValue(editNameText.getText().toString());
                            operationRef.child(key).child("assignmentContext").setValue(editContext.getText().toString());
                            operationRef.child(key).child("publishedDate").setValue(getCurrentTime());
                            assignment.setAssingmentName(editNameText.getText().toString());
                            assignment.setAssignmentContext(editContext.getText().toString());
                            assignment.setDate(Calendar.getInstance().getTime());
                            Toast.makeText(EditDeleteTeacherActivity.this,  "Assignment context is edited successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditDeleteTeacherActivity.this, TeacherActivity.class);
                            Bundle b = new Bundle();
                            b.putString("courseID",courseID);
                            b.putSerializable("user", user);
                            b.putSerializable("teacher", teacher);
                            intent.putStringArrayListExtra("course", (ArrayList<String>) courses);
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                    });
                }
            });
            viewUploadedAssignments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    customAdapter=new CustomAdapter(EditDeleteTeacherActivity.this, R.layout.activity_rowcustom, (ArrayList<String>) uploadedAssignments);
                    uploadedAssignmentsListView.setAdapter(customAdapter);
                    if(visibility==false)
                    {
                        uploadedAssignmentsListView.setVisibility(View.VISIBLE);
                        visibility=true;
                    }
                    else
                    {
                        uploadedAssignmentsListView.setVisibility(View.INVISIBLE);
                        visibility=false;
                    }
                }
            });
        }
    }
    public void getData()
    {
        dataOperationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot cKeySnapshot : courseSnapshot.getChildren()) {
                        if (cKeySnapshot.getValue().toString().equals(courseID)) {
                            for (DataSnapshot assignmentSnapshot : courseSnapshot.child("assignments").getChildren()) {
                                if(assignmentSnapshot.getKey().equalsIgnoreCase(key)) {
                                    for (DataSnapshot assignKeySnapshot : assignmentSnapshot.child("Submitters").getChildren()) {
                                        for (DataSnapshot submitSnapshot : assignKeySnapshot.getChildren()) {
                                            if (submitSnapshot.getKey().equalsIgnoreCase("AssignmentContext")) {
                                                assignContext = submitSnapshot.getValue().toString();
                                            } else if (submitSnapshot.getKey().equalsIgnoreCase("Date")) {
                                                uploadDate = submitSnapshot.getValue().toString();
                                            } else if (submitSnapshot.getKey().equalsIgnoreCase("StudentID")) {
                                                whoUploaded = submitSnapshot.getValue().toString();
                                            }
                                        }
                                        uploadedAssignments.add("Student ID: " + whoUploaded + " \nUploaded Date: " + uploadDate + " \nSolution: " + assignContext + "\n");
                                    }
                                }
                            }
                            if(whichSelected == true){
                                operationRef=courseSnapshot.child("announcements").getRef();
                            }
                            else{
                                operationRef=courseSnapshot.child("assignments").getRef();
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
    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Turkey"));
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

}


