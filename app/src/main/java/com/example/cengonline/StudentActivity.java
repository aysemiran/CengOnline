package com.example.cengonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class StudentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    private DatabaseReference dataOperationRef, assignmentOperationRef;
    private ListView listView;
    private ListView mainListView;
    private Button homeButton;
    private Button logout;
    private String courseID, id="",name="",context="",cName="",dtStart ;
    private Date date;
    private Student student;
    private Courses course;
    private TextView nameCourse;
    private TextView selectedAssignment;
    private TextView submitText;
    private EditText newAssignmentText;
    private Button uploadNewAssignment;
    private Button addNewAssignment;
    private List<String> assignmentsList;
    private List<String> announcementsList;
    private List<Announcement> allAnnouncements;
    private Announcement announcement;
    private List<Assignment> allAssignments;
    private Assignment assignment;
    private int numberOfSubmit=0, newPosition;
    private String idOfAssignment="";
    private List<String> mainlist;
    private ArrayList<String> idList;
    private List<String> courses;
    private CustomAdapter customAdapter;
    private User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        mainlist = new ArrayList<>();
        idList = new ArrayList<>();
        courses = new ArrayList<>();
        allAnnouncements = new ArrayList<>();
        allAssignments = new ArrayList<>();
        announcementsList = new ArrayList<>();
        assignmentsList = new ArrayList<>();
        homeButton = findViewById(R.id.homeButton);
        logout = findViewById(R.id.logoutButton);
        nameCourse = findViewById(R.id.nameCourse);
        uploadNewAssignment = findViewById(R.id.uploadNewAssignment);
        selectedAssignment = findViewById(R.id.selectedAssignment);
        submitText = findViewById(R.id.submitText);
        newAssignmentText = findViewById(R.id.newAssignmentText);
        addNewAssignment = findViewById(R.id.sendAssignButton);
        dataOperationRef = FirebaseDatabase.getInstance().getReference().child("Courses");
        assignmentOperationRef = FirebaseDatabase.getInstance().getReference().child("Courses");
        student = (Student) getIntent().getSerializableExtra("student");
        courseID = getIntent().getStringExtra("courseID");
        cName= getIntent().getStringExtra("courseInformation");
        courses = getIntent().getStringArrayListExtra("course");
        user = (User) getIntent().getSerializableExtra("user");
        nameCourse.setText(cName);
        getData();
        listView = findViewById(R.id.assignList);
        mainListView = findViewById(R.id.mainList);

        Spinner spinner = findViewById(R.id.courseSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.coursepinner,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentActivity.this, StudentCourseActivity.class);
                Bundle b = new Bundle();
                b.putString("courseID",courseID);
                b.putSerializable("student", student);
                b.putSerializable("user",user);
                intent.putStringArrayListExtra("course", (ArrayList<String>) courses);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                String selectedItem = mainListView.getItemAtPosition(position).toString();
                Character key = selectedItem.charAt(1);

                if(key == 's'){
                    for(int j = 0; j < allAssignments.size(); j++){
                        if(allAssignments.get(j).getAssignmentID().equalsIgnoreCase(idList.get(position))){
                            selectedAssignment.setText(assignmentsList.get(j));
                            idOfAssignment=allAssignments.get(j).getAssignmentID();
                            newPosition=j;
                            break;
                        }
                    }
                    mainListView.setVisibility(View.INVISIBLE);
                    uploadNewAssignment.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                    selectedAssignment.setVisibility(View.VISIBLE);
                    uploadNewAssignment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            uploadNewAssignment.setVisibility(View.INVISIBLE);
                            submitText.setVisibility(View.VISIBLE);
                            newAssignmentText.setVisibility(View.VISIBLE);
                            addNewAssignment.setVisibility(View.VISIBLE);
                            addNewAssignment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    assignmentOperationRef= assignmentOperationRef.child("assignment"+idOfAssignment).child("Submitters").getRef();
                                    assignmentOperationRef.child("Submit"+(allAssignments.get(newPosition).getNumberOfSubmitters()+1)).child("StudentID").setValue(student.getID());
                                    assignmentOperationRef.child("Submit"+(allAssignments.get(newPosition).getNumberOfSubmitters()+1)).child("AssignmentContext").setValue(newAssignmentText.getText().toString());
                                    assignmentOperationRef.child("Submit"+(allAssignments.get(newPosition).getNumberOfSubmitters()+1)).child("Date").setValue(getCurrentTime());
                                    allAssignments.get(newPosition).setNumberOfSubmitters(allAssignments.get(newPosition).getNumberOfSubmitters()+1);
                                    Toast.makeText(StudentActivity.this,  "You have submitted your work!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(StudentActivity.this, StudentActivity.class);
                                    Bundle b = new Bundle();
                                    b.putString("courseID",courseID);
                                    b.putSerializable("student", student);
                                    intent.putExtras(b);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        switch (text) {
            case "Assignments":
                mainListView.setVisibility(View.INVISIBLE);
                customAdapter = new CustomAdapter(StudentActivity.this, R.layout.activity_rowcustom, (ArrayList<String>) assignmentsList);
                listView.setAdapter(customAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        uploadNewAssignment.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.INVISIBLE);
                        selectedAssignment.setText(assignmentsList.get(position));
                        selectedAssignment.setVisibility(View.VISIBLE);
                        uploadNewAssignment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                uploadNewAssignment.setVisibility(View.INVISIBLE);
                                submitText.setVisibility(View.VISIBLE);
                                newAssignmentText.setVisibility(View.VISIBLE);
                                addNewAssignment.setVisibility(View.VISIBLE);
                                addNewAssignment.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        assignmentOperationRef= assignmentOperationRef.child("assignment"+(allAssignments.get(position).getAssignmentID())).child("Submitters").getRef();
                                        assignmentOperationRef.child("Submit"+(allAssignments.get(position).getNumberOfSubmitters()+1)).child("StudentID").setValue(student.getID());
                                        assignmentOperationRef.child("Submit"+(allAssignments.get(position).getNumberOfSubmitters()+1)).child("AssignmentContext").setValue(newAssignmentText.getText().toString());
                                        assignmentOperationRef.child("Submit"+(allAssignments.get(position).getNumberOfSubmitters()+1)).child("Date").setValue(getCurrentTime());
                                        allAssignments.get(position).setNumberOfSubmitters(allAssignments.get(position).getNumberOfSubmitters()+1);
                                        Toast.makeText(StudentActivity.this,  "You have submitted your work!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(StudentActivity.this, StudentActivity.class);
                                        Bundle b = new Bundle();
                                        b.putSerializable("student", student);
                                        b.putString("courseID",courseID);
                                        b.putSerializable("user",user);
                                        intent.putStringArrayListExtra("course", (ArrayList<String>) courses);
                                        intent.putExtras(b);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }
                });
                Toast.makeText(parent.getContext(), text + " Screen", Toast.LENGTH_SHORT).show();
                break;
            case "Announcements":
                mainListView.setVisibility(View.INVISIBLE);
                submitText.setVisibility(View.INVISIBLE);
                selectedAssignment.setVisibility(View.INVISIBLE);
                uploadNewAssignment.setVisibility(View.INVISIBLE);
                newAssignmentText.setVisibility(View.INVISIBLE);
                addNewAssignment.setVisibility(View.INVISIBLE);
                customAdapter = new CustomAdapter(StudentActivity.this, R.layout.activity_rowcustom, (ArrayList<String>) announcementsList);
                listView.setAdapter(customAdapter);
                listView.setVisibility(View.VISIBLE);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String selectedFromList = (String) listView.getItemAtPosition(i);
                        Intent sendUser = new Intent(StudentActivity.this,StudentActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("student", student);
                        b.putString("courseID",courseID);
                        b.putSerializable("user",user);
                        sendUser.putStringArrayListExtra("course", (ArrayList<String>) courses);
                        startActivity(sendUser);
                    }
                });
                Toast.makeText(parent.getContext(), text + " Screen", Toast.LENGTH_SHORT).show();
                break;
            case "Stream":
                Intent streamGo = new Intent(StudentActivity.this, StreamActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("student", student);
                b.putString("courseID", courseID);
                b.putBoolean("type", false);
                streamGo.putExtras(b);
                startActivity(streamGo);
                break;
            case "Main":
                Intent mainGo = new Intent(StudentActivity.this, StudentActivity.class);
                Bundle mbundle = new Bundle();
                mbundle.putSerializable("student", student);
                mbundle.putString("courseID", courseID);
                mbundle.putSerializable("user",user);
                mainGo.putStringArrayListExtra("course", (ArrayList<String>) courses);
                mainGo.putExtras(mbundle);
                startActivity(mainGo);
                break;
            case "Message":
                Intent messageGo = new Intent(StudentActivity.this, MessageActivity.class);
                Bundle meBundle = new Bundle();
                meBundle.putSerializable("user", user);
                meBundle.putString("courseID", courseID);
                meBundle.putSerializable("student",student);
                messageGo.putStringArrayListExtra("course", (ArrayList<String>) courses);
                messageGo.putExtras(meBundle);
                startActivity(messageGo);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
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
                                assignmentOperationRef=courseSnapshot.child("assignments").getRef();
                                for (DataSnapshot assignKeySnapshot : assignmentSnapshot.getChildren()) {
                                    if(assignKeySnapshot.getKey().equalsIgnoreCase("assignmentID")) {
                                        id=assignKeySnapshot.getValue().toString();
                                    }
                                    else if(assignKeySnapshot.getKey().equalsIgnoreCase("assignmentName")) {
                                        name=assignKeySnapshot.getValue().toString();
                                    }
                                    else if(assignKeySnapshot.getKey().equalsIgnoreCase("assignmentContext")) {
                                        context=assignKeySnapshot.getValue().toString();
                                    }
                                    else if(assignKeySnapshot.getKey().equalsIgnoreCase("publishedDate")){
                                        date=getCurrentDate(assignKeySnapshot.getValue().toString());
                                    }
                                    else if(assignKeySnapshot.getKey().equalsIgnoreCase("Submitters"))
                                    {
                                        for (DataSnapshot submitKeySnapshot : assignKeySnapshot.getChildren()) {
                                            numberOfSubmit++;
                                        }

                                    }
                                }
                                assignment = new Assignment(id, name, context, date,numberOfSubmit);
                                numberOfSubmit = 0;
                                allAssignments.add(assignment);
                            }
                            Collections.sort(allAssignments, new Comparator<Assignment>() {
                                public int compare(Assignment a1, Assignment a2) {
                                    return a2.getDate().compareTo(a1.getDate());
                                }
                            });

                            for(int i = 0; i < allAssignments.size(); i++){
                                assignmentsList.add("Assignment    " + " " + parseDate(allAssignments.get(i).getDate().toString()) + "\n" + allAssignments.get(i).getAssingmentName() +"\n"+ allAssignments.get(i).getAssignmentContext());
                            }
                            for (DataSnapshot announcementSnapshot : courseSnapshot.child("announcements").getChildren()) {
                                for (DataSnapshot announcementKeySnapshot : announcementSnapshot.getChildren()) {
                                    if (announcementKeySnapshot.getKey().equalsIgnoreCase("announcementID")) {
                                        id=announcementKeySnapshot.getValue().toString();
                                    }
                                    else if(announcementKeySnapshot.getKey().equalsIgnoreCase("announcementContext"))
                                    {
                                        context=announcementKeySnapshot.getValue().toString();
                                    }
                                    else if(announcementKeySnapshot.getKey().equalsIgnoreCase("publishedDate")){
                                        date=getCurrentDate(announcementKeySnapshot.getValue().toString());
                                    }
                                }
                                announcement = new Announcement(id, context, date);
                                allAnnouncements.add(announcement);
                            }
                            Collections.sort(allAnnouncements, new Comparator<Announcement>() {
                                public int compare(Announcement a1, Announcement a2) {
                                    return a2.getDate().compareTo(a1.getDate());
                                }
                            });

                            for(int i = 0; i < allAnnouncements.size(); i++){
                                announcementsList.add("Announcement " + " " + parseDate(allAnnouncements.get(i).getDate().toString()) +"\n"+ allAnnouncements.get(i).getAnnouncementContext());
                            }

                            int n1 = allAnnouncements.size();
                            int n2 = allAssignments.size();
                            int i = 0, j = 0;

                            while (i < n1 && j < n2) {
                                if (allAnnouncements.get(i).getDate().after(allAssignments.get(j).getDate())){
                                    mainlist.add("Announcement " + " " + parseDate(allAnnouncements.get(i).getDate().toString()) +"\n"+ allAnnouncements.get(i).getAnnouncementContext());
                                    idList.add(allAnnouncements.get(i++).getAnnouncementID());
                                }
                                else {
                                    mainlist.add("Assignment    " + " " + parseDate(allAssignments.get(j).getDate().toString()) + "\n" + allAssignments.get(j).getAssingmentName() +"\n"+ allAssignments.get(j).getAssignmentContext());
                                    idList.add(allAssignments.get(j++).getAssignmentID());
                                }
                            }
                            while (i < n1) {
                                mainlist.add("Announcement " + " " + parseDate(allAnnouncements.get(i).getDate().toString()) +"\n"+ allAnnouncements.get(i).getAnnouncementContext());
                                idList.add(allAnnouncements.get(i++).getAnnouncementID());
                            }
                            while (j < n2) {
                                mainlist.add("Assignment    " + " " + parseDate(allAssignments.get(j).getDate().toString()) + "\n" + allAssignments.get(j).getAssingmentName() +"\n"+ allAssignments.get(j).getAssignmentContext());
                                idList.add(allAssignments.get(j++).getAssignmentID());
                            }
                        }
                        customAdapter = new CustomAdapter(StudentActivity.this, R.layout.activity_rowcustom, (ArrayList<String>) mainlist);
                        mainListView.setAdapter(customAdapter);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public Date getCurrentDate(String currentDate)
    {
        Date date=new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = format.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public String parseDate(String date)
    {
        String newDateFormat="";
        String year=date.substring(29);
        String words[]=date.split(" ");
        newDateFormat=(words[3]+" - "+words[2]+" "+words[1]+year+" - "+words[0]);
        return newDateFormat;
    }
    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Turkey"));
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }
}