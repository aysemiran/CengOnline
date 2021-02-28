package com.example.cengonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class StreamActivity extends AppCompatActivity {

    private DatabaseReference dataOperationRef, operationRef, streamRef, newStreamRef;
    private User user;
    private Stream stream;
    private Teacher teacher;
    private Student student;
    private Boolean isTeacher;
    private String teacherUsername;
    private Date date;
    public static final String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
    private String id="",context="";
    private String courseID;
    private Button newPostButton;
    private TextView streamInfo;
    private TextView streamHead;
    private ListView postList;
    private Button addSeeCommentsButton;
    private Button editButton;
    private Button deleteButton;
    private Button sendEdit;
    private List<Stream> allStreams=new ArrayList<>();
    private List<String> streamsList=new ArrayList<>();
    private List<Integer> streamIdList = new ArrayList<>();
    private EditText newPostText;
    private TextView selectedPost;
    private CustomAdapter customAdapter;
    private String selectedFromList,postID;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        newPostButton = findViewById(R.id.newPostButton);
        addSeeCommentsButton = findViewById(R.id.commentButton);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        sendEdit = findViewById(R.id.sendEditButton);
        newPostText = findViewById(R.id.newPostText);
        streamInfo = findViewById(R.id.streamInfo);
        streamHead = findViewById(R.id.streamHeadText);
        selectedPost = findViewById(R.id.selectedPost);
        postList = findViewById(R.id.postList);
        teacher = (Teacher) getIntent().getSerializableExtra("teacher");
        student = (Student) getIntent().getSerializableExtra("student");
        courseID = getIntent().getStringExtra("courseID");
        isTeacher = (Boolean) getIntent().getSerializableExtra("type");
        dataOperationRef = FirebaseDatabase.getInstance().getReference().child("Courses");
        operationRef = FirebaseDatabase.getInstance().getReference().child("Courses");
        streamRef = FirebaseDatabase.getInstance().getReference().child("Courses");
        newStreamRef = FirebaseDatabase.getInstance().getReference().child("Courses");

        getData();

        if(isTeacher == true) {
            newPostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id;
                    if(streamIdList.size()==0)
                    {
                        key="stream"+(1);
                        id = 1;
                        operationRef = newStreamRef.child("stream");
                    }
                    else
                    {
                        key="stream"+(streamIdList.get(0)+1);
                        id = streamIdList.get(0)+1;
                    }
                    date=getCurrentDate(getCurrentTime());
                    stream = new Stream(Integer.toString(id), newPostText.getText().toString(), date, teacher.getUsername());
                    operationRef.child(key).child("streamID").setValue(id);
                    operationRef.child(key).child("streamContext").setValue(newPostText.getText().toString());
                    operationRef.child(key).child("streamDate").setValue(getCurrentTime());
                    operationRef.child(key).child("whoShared").setValue(teacher.getUsername().toString());
                    allStreams.add(stream);
                    Toast.makeText(StreamActivity.this, "Post is send.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(StreamActivity.this, StreamActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("teacher", teacher);
                    b.putString("courseID", courseID);
                    b.putBoolean("type", true);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
        }
        else if(isTeacher == false){
            streamHead.setText("Hey " + student.getName() + "\n"+ "Welcome to Stream");
            newPostButton.setVisibility(View.INVISIBLE);
            streamInfo.setVisibility(View.INVISIBLE);
            newPostText.setVisibility(View.INVISIBLE);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) postList.getLayoutParams();
            params.topMargin = 200;
        }

        postList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                selectedFromList = (String) postList.getItemAtPosition(i);
                postID = allStreams.get(i).getPostID();
                key = "stream" + postID;
                postList.setVisibility(View.INVISIBLE);
                newPostButton.setVisibility(View.INVISIBLE);
                streamInfo.setVisibility(View.INVISIBLE);
                newPostText.setVisibility(View.INVISIBLE);
                streamHead.setVisibility(View.INVISIBLE);
                addSeeCommentsButton.setVisibility(View.VISIBLE);
                addSeeCommentsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(StreamActivity.this, CommentActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("user", user);
                        b.putString("selectedPost", selectedFromList);
                        b.putString("postId", postID);
                        b.putString("courseID", courseID);
                        b.putSerializable("stream", allStreams.get(i));
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });
                selectedPost.setText(postList.getItemAtPosition(i).toString());
                selectedPost.setVisibility(View.VISIBLE);
                if(isTeacher == true){
                    user = teacher;
                    editButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);
                    selectedFromList = (String) postList.getItemAtPosition(i);
                    postID = allStreams.get(i).getPostID();
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newPostText.setVisibility(View.VISIBLE);
                            deleteButton.setVisibility(View.INVISIBLE);
                            addSeeCommentsButton.setVisibility(View.INVISIBLE);
                            editButton.setVisibility(View.INVISIBLE);
                            sendEdit.setVisibility(View.VISIBLE);
                            sendEdit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    streamRef.child(key).child("streamContext").setValue(newPostText.getText().toString());
                                    streamRef.child(key).child("streamDate").setValue(getCurrentTime());
                                    Toast.makeText(StreamActivity.this,  "Post context is edited successfully.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(StreamActivity.this, StreamActivity.class);
                                    Bundle b = new Bundle();
                                    b.putSerializable("teacher", teacher);
                                    b.putBoolean("type", true);
                                    b.putString("courseID",courseID);
                                    intent.putExtras(b);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            streamRef=streamRef.child(key).getRef();
                            streamRef.removeValue();
                            Toast.makeText(StreamActivity.this,  "Post is deleted successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(StreamActivity.this, StreamActivity.class);
                            Bundle b = new Bundle();
                            b.putSerializable("teacher", teacher);
                            b.putBoolean("type", true);
                            b.putString("courseID",courseID);
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                    });
                }
                else{
                    user = student;
                }
            }
        });
    }

    public void getData()
    {
        dataOperationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot cKeySnapshot : courseSnapshot.getChildren()) {
                        if (cKeySnapshot.getValue().toString().equals(courseID)) {
                            newStreamRef = courseSnapshot.getRef();
                            for (DataSnapshot streamSnapshot : courseSnapshot.child("stream").getChildren()) {
                                operationRef = courseSnapshot.child("stream").getRef();
                                streamRef = courseSnapshot.child("stream").getRef();
                                for (DataSnapshot streamKeySnapshot : streamSnapshot.getChildren()) {
                                    if (streamKeySnapshot.getKey().equalsIgnoreCase("streamID")) {
                                        id=streamKeySnapshot.getValue().toString();
                                    }
                                    else if(streamKeySnapshot.getKey().equalsIgnoreCase("streamContext"))
                                    {
                                        context=streamKeySnapshot.getValue().toString();
                                    }
                                    else if(streamKeySnapshot.getKey().equalsIgnoreCase("streamDate")){
                                        date=getCurrentDate(streamKeySnapshot.getValue().toString());
                                    }
                                    else if(streamKeySnapshot.getKey().equalsIgnoreCase("whoShared"))
                                    {
                                        teacherUsername=streamKeySnapshot.getValue().toString();
                                    }
                                }
                                stream = new Stream(id, context, date, teacherUsername);
                                allStreams.add(stream);
                                if(allStreams.size()!=0)
                                    streamIdList.add(Integer.parseInt(stream.getPostID()));
                            }

                            Collections.sort(streamIdList);
                            Collections.reverse(streamIdList);

                            Collections.sort(allStreams, new Comparator<Stream>() {
                                public int compare(Stream a1, Stream a2) {
                                    return a2.getDate().compareTo(a1.getDate());
                                }
                            });
                            for(int i = 0; i < allStreams.size(); i++){
                                String date = parseDate(allStreams.get(i).getDate().toString());
                                String areyou = allStreams.get(i).getTeacherUsername();
                                if(isTeacher)
                                {
                                    if(teacher.getUsername().equalsIgnoreCase(allStreams.get(i).getTeacherUsername()))
                                    {
                                        areyou = "You";
                                    }
                                }
                                streamsList.add(areyou+ " shared a post \n" + date + "\n" + allStreams.get(i).getPostContext());
                            }
                            customAdapter = new CustomAdapter(StreamActivity.this, R.layout.activity_rowcustom, (ArrayList<String>) streamsList);
                            postList.setAdapter(customAdapter);
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
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_1);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Turkey"));
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
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
}

