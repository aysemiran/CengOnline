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

public class TeacherActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private DatabaseReference dataOperationRef, operationRef;
    private String courseID, id="",name="",context="",cName="", dtStart ;
    private Date date;
    private Teacher teacher;
    private ListView listView;
    private ListView mainListView;
    private TextView nameCourse;
    private EditText newNameText;
    private EditText newContextText;
    private List<String> assignmentsList;
    private List<String> announcementsList;
    private Button homeButton;
    private Button addNewButton;
    private Button addButton;
    private Button logout;
    private  List<Announcement> allAnnouncements;
    private Announcement announcement;
    private List<Assignment> allAssignments;
    private Assignment assignment;
    private boolean isAnnouncement;
    private static final String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
    private List<String> mainlist;
    private ArrayList<String> idList;
    private CustomAdapter customAdapter;
    private List<Integer> assignmentIdList;
    private List<Integer> announcementIdList;
    private List<String> courses;
    private User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        courses = new ArrayList<>();
        allAnnouncements = new ArrayList<>();
        allAssignments = new ArrayList<>();
        assignmentsList=new ArrayList<>();
        announcementsList=new ArrayList<>();
        mainlist = new ArrayList<>();
        idList = new ArrayList<>();
        assignmentIdList = new ArrayList<>();
        announcementIdList = new ArrayList<>();
        dataOperationRef=FirebaseDatabase.getInstance().getReference().child("Courses");
        operationRef=FirebaseDatabase.getInstance().getReference().child("Courses");
        teacher = (Teacher) getIntent().getSerializableExtra("teacher");
        courseID = (String) getIntent().getStringExtra("courseID");
        cName= (String) getIntent().getStringExtra("courseInformation");
        user = (User) getIntent().getSerializableExtra("user");
        courses = getIntent().getStringArrayListExtra("course");
        homeButton = findViewById(R.id.homeButton);
        logout = findViewById(R.id.logoutButton);
        addNewButton = findViewById(R.id.addNewButton);
        addButton = findViewById(R.id.addButton);
        newNameText = findViewById(R.id.newNameText);
        newContextText = findViewById(R.id.newContextText);
        nameCourse = findViewById(R.id.nameCourse);
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
                Intent intent = new Intent(TeacherActivity.this, TeacherCourseActivity.class);
                Bundle b = new Bundle();
                b.putString("courseID",courseID);
                b.putSerializable("teacher", teacher);
                b.putSerializable("user", user);
                intent.putStringArrayListExtra("course", (ArrayList<String>) courses);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = mainListView.getItemAtPosition(i).toString();
                Character key = selectedItem.charAt(1);
                Intent sendItem = new Intent(TeacherActivity.this, EditDeleteTeacherActivity.class);
                Bundle b = new Bundle();
                if(key == 's'){
                    isAnnouncement = false;
                    for(int j = 0; j < allAssignments.size(); j++){
                        if(allAssignments.get(j).getAssignmentID().equalsIgnoreCase(idList.get(i))){
                            b.putSerializable("assignment", allAssignments.get(j));
                            break;
                        }
                    }
                }
                else if(key == 'n'){
                    isAnnouncement = true;
                    for(int j = 0; j < allAnnouncements.size(); j++){
                        if(allAnnouncements.get(j).getAnnouncementID().equalsIgnoreCase(idList.get(i))){
                            b.putSerializable("announcement", allAnnouncements.get(j));
                            break;
                        }
                    }
                }
                b.putBoolean("selected", isAnnouncement);
                b.putString("courseID",courseID);
                b.putSerializable("user", user);
                b.putSerializable("teacher", teacher);
                sendItem.putStringArrayListExtra("course", (ArrayList<String>) courses);
                sendItem.putExtras(b);
                startActivity(sendItem);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final String text = parent.getItemAtPosition(position).toString();
        addButton.setVisibility(View.INVISIBLE);
        newContextText.setVisibility(View.INVISIBLE);
        addButton.setVisibility(View.INVISIBLE);
        newNameText.setVisibility(View.INVISIBLE);
        newContextText.setVisibility(View.INVISIBLE);
        switch (text) {
            case "Assignments":
                mainListView.setVisibility(View.INVISIBLE);
                isAnnouncement = false;
                customAdapter = new CustomAdapter(TeacherActivity.this, R.layout.activity_rowcustom, (ArrayList<String>) assignmentsList);
                listView.setAdapter(customAdapter);
                listView.setVisibility(View.VISIBLE);
                addNewButton.setVisibility(View.VISIBLE);
                addNewButton.setText("ADD ASSIGNMENT");
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent sendAss = new Intent(TeacherActivity.this, EditDeleteTeacherActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("assignment", allAssignments.get(i));
                        b.putBoolean("selected", isAnnouncement);
                        b.putString("courseID",courseID);
                        b.putSerializable("user", user);
                        b.putSerializable("teacher", teacher);
                        sendAss.putStringArrayListExtra("course", (ArrayList<String>) courses);
                        sendAss.putExtras(b);
                        startActivity(sendAss);
                    }
                });
                addNewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listView.setVisibility(View.INVISIBLE);
                        addNewButton.setVisibility(View.INVISIBLE);
                        addButton.setVisibility(View.VISIBLE);
                        addButton.setText("ADD ASSIGNMENT");
                        newNameText.setVisibility(View.VISIBLE);
                        newContextText.setVisibility(View.VISIBLE);
                        addButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String key;
                                int id;
                                if(assignmentIdList.size()==0)
                                {
                                    key="assignment"+(1);
                                    id=1;
                                }
                                else
                                {
                                    key="assignment"+(assignmentIdList.get(0)+1);
                                    id=assignmentIdList.get(0)+1;
                                }
                                operationRef=operationRef.child("assignments").getRef();
                                operationRef.child(key).child("assignmentID").setValue(id);
                                operationRef.child(key).child("assignmentName").setValue(newNameText.getText().toString());
                                operationRef.child(key).child("assignmentContext").setValue(newContextText.getText().toString());
                                operationRef.child(key).child("publishedDate").setValue(getCurrentTime());
                                assignment=new Assignment(Integer.toString(id),newNameText.getText().toString(),newContextText.getText().toString(),getCurrentDate(getCurrentTime()),0);
                                allAssignments.add(assignment);
                                Toast.makeText(TeacherActivity.this,  "New assignment is here!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(TeacherActivity.this, TeacherActivity.class);
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

                Toast.makeText(parent.getContext(), text + " Screen", Toast.LENGTH_SHORT).show();
                break;
            case "Announcements":
                mainListView.setVisibility(View.INVISIBLE);
                isAnnouncement = true;
                customAdapter = new CustomAdapter(TeacherActivity.this, R.layout.activity_rowcustom, (ArrayList<String>) announcementsList);
                listView.setAdapter(customAdapter);
                listView.setVisibility(View.VISIBLE);
                addNewButton.setVisibility(View.VISIBLE);
                addNewButton.setVisibility(View.VISIBLE);
                addNewButton.setText("ADD ANNOUNCEMENT");

                addNewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listView.setVisibility(View.INVISIBLE);
                        addNewButton.setVisibility(View.INVISIBLE);
                        addButton.setVisibility(View.VISIBLE);
                        addButton.setText("ADD ANNOUNCEMENT");
                        newNameText.setVisibility(View.VISIBLE);
                        addButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String key;
                                int id;
                                if(announcementIdList.size()==0)
                                {
                                    key="announcement"+(1);
                                    id=1;
                                }
                                else
                                {
                                    key="announcement"+(announcementIdList.get(0)+1);
                                    id=announcementIdList.get(0)+1;
                                }
                                operationRef = operationRef.child("announcements").getRef();
                                operationRef.child(key).child("announcementContext").setValue(newNameText.getText().toString());
                                operationRef.child(key).child("announcementID").setValue(id);
                                operationRef.child(key).child("publishedDate").setValue(getCurrentTime());
                                announcement= new Announcement(Integer.toString(id),newNameText.getText().toString(),getCurrentDate(getCurrentTime()));
                                allAnnouncements.add(announcement);
                                Toast.makeText(TeacherActivity.this,  "New announcement is here!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(TeacherActivity.this, TeacherActivity.class);
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
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent sendAnno = new Intent(TeacherActivity.this, EditDeleteTeacherActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("announcement", allAnnouncements.get(i));
                        b.putBoolean("selected", isAnnouncement);
                        b.putString("courseID",courseID);
                        b.putSerializable("user", user);
                        b.putSerializable("teacher", teacher);
                        sendAnno.putStringArrayListExtra("course", (ArrayList<String>) courses);
                        sendAnno.putExtras(b);
                        startActivity(sendAnno);
                    }
                });
                Toast.makeText(parent.getContext(), text + " Screen", Toast.LENGTH_SHORT).show();
                break;
            case "Stream":
                Intent streamGo = new Intent(TeacherActivity.this, StreamActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("teacher", teacher);
                b.putString("courseID", courseID);
                b.putBoolean("type", true);
                streamGo.putExtras(b);
                startActivity(streamGo);
                break;
            case "Main":
                Intent mainGo = new Intent(TeacherActivity.this, TeacherActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("teacher", teacher);
                mBundle.putString("courseID", courseID);
                mBundle.putSerializable("user", user);
                mainGo.putStringArrayListExtra("course", (ArrayList<String>) courses);
                mainGo.putExtras(mBundle);
                startActivity(mainGo);
                break;
            case "Message":
                Intent messageGo = new Intent(TeacherActivity.this, MessageActivity.class);
                Bundle meBundle = new Bundle();
                meBundle.putSerializable("teacher", teacher);
                meBundle.putSerializable("user", user);
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
                            operationRef = courseSnapshot.getRef();
                            for (DataSnapshot assignmentSnapshot : courseSnapshot.child("assignments").getChildren()) {
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
                                    else if(assignKeySnapshot.getKey().equalsIgnoreCase("publishedDate"))
                                    {
                                        date=getCurrentDate(assignKeySnapshot.getValue().toString());
                                    }
                                }
                                assignment = new Assignment(id, name, context, date);
                                allAssignments.add(assignment);
                                if(allAssignments.size()!=0)
                                    assignmentIdList.add(Integer.parseInt(assignment.getAssignmentID()));

                            }

                            Collections.sort(allAssignments, new Comparator<Assignment>() {
                                public int compare(Assignment a1, Assignment a2) {
                                    return a2.getDate().compareTo(a1.getDate());
                                }
                            });


                            Collections.sort(assignmentIdList);
                            Collections.reverse(assignmentIdList);


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
                                if(allAnnouncements.size()!=0)
                                    announcementIdList.add(Integer.parseInt(announcement.getAnnouncementID()));
                            }

                            Collections.sort(allAnnouncements, new Comparator<Announcement>() {
                                public int compare(Announcement a1, Announcement a2) {
                                    return a2.getDate().compareTo(a1.getDate());
                                }
                            });

                            Collections.sort(announcementIdList);
                            Collections.reverse(announcementIdList);

                            for(int i = 0; i < allAnnouncements.size(); i++){
                                announcementsList.add("Announcement " + " " + parseDate(allAnnouncements.get(i).getDate().toString()) +"\n"+ allAnnouncements.get(i).getAnnouncementContext());
                            }

                            int n1 = allAnnouncements.size();
                            int n2 = allAssignments.size();
                            int i = 0, j = 0;
                            Date date;

                            while (i < n1 && j < n2) {
                                if (allAnnouncements.get(i).getDate().after(allAssignments.get(j).getDate())){
                                    date = allAnnouncements.get(i).getDate();
                                    parseDate(date.toString());
                                    mainlist.add("Announcement " + " " + parseDate(allAnnouncements.get(i).getDate().toString()) +"\n"+ allAnnouncements.get(i).getAnnouncementContext());
                                    idList.add(allAnnouncements.get(i++).getAnnouncementID());
                                }
                                else {
                                    date = allAssignments.get(j).getDate();
                                    parseDate(date.toString());
                                    mainlist.add("Assignment    " + " " + parseDate(allAssignments.get(j).getDate().toString()) + "\n" + allAssignments.get(j).getAssingmentName() +"\n"+ allAssignments.get(j).getAssignmentContext());
                                    idList.add(allAssignments.get(j++).getAssignmentID());
                                }
                            }
                            while (i < n1) {
                                date = allAnnouncements.get(i).getDate();
                                parseDate(date.toString());
                                mainlist.add("Announcement " + " " + parseDate(allAnnouncements.get(i).getDate().toString()) +"\n"+ allAnnouncements.get(i).getAnnouncementContext());
                                idList.add(allAnnouncements.get(i++).getAnnouncementID());
                            }
                            while (j < n2) {
                                date = allAssignments.get(j).getDate();
                                parseDate(date.toString());
                                mainlist.add("Assignment    " + " " + parseDate(allAssignments.get(j).getDate().toString()) + "\n" + allAssignments.get(j).getAssingmentName() +"\n"+ allAssignments.get(j).getAssignmentContext());
                                idList.add(allAssignments.get(j++).getAssignmentID());
                            }
                        }
                        customAdapter = new CustomAdapter(TeacherActivity.this, R.layout.activity_rowcustom, (ArrayList<String>) mainlist);
                        mainListView.setAdapter(customAdapter);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_1);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Turkey"));
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

    public String parseDate(String date)
    {
        String newDateFormat="";
        String year=date.substring(29);
        String words[]=date.split(" ");
        newDateFormat=(words[3]+" - "+words[2]+" "+words[1]+year+" - "+words[0]);
        return newDateFormat;
    }
    public Date getCurrentDate(String currentDate)
    {
        Date date=new Date();
        dtStart = currentDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = format.parse(dtStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}