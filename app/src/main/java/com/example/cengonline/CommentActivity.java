package com.example.cengonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class CommentActivity extends AppCompatActivity {

    private DatabaseReference dataOperationRef, operationRef;
    private User user;
    private String post;
    private String postId;
    private Date date;
    private String courseID;
    private String commentContext, whoShared;
    private String streamKey;
    private HashMap<String, String> comments;
    private ListView commentsList;
    private EditText newCommentText;
    private TextView selectedPost;
    private Button sendCommentButton;
    private List<String> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        commentsList = findViewById(R.id.commentList);
        newCommentText = findViewById(R.id.newCommentText);
        selectedPost = findViewById(R.id.selectedPost);
        sendCommentButton = findViewById(R.id.addCommentButton);
        user = (User) getIntent().getSerializableExtra("user");
        post = (String) getIntent().getStringExtra("selectedPost");
        postId = getIntent().getStringExtra("postId");
        courseID = getIntent().getStringExtra("courseID");
        dataOperationRef = FirebaseDatabase.getInstance().getReference().child("Courses");
        operationRef = FirebaseDatabase.getInstance().getReference().child("Courses");
        commentList = new ArrayList<>();
        comments = new HashMap<String, String>();
        selectedPost.setText(post);
        getData();

        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = newCommentText.getText().toString();
                operationRef.child(streamKey).child("comments").child("comment" + (commentList.size()+1)).child("whoShared").setValue(user.getUsername());
                operationRef.child(streamKey).child("comments").child("comment" + (commentList.size()+1)).child("commentContext").setValue(comment);
                operationRef.child(streamKey).child("comments").child("comment" + (commentList.size()+1)).child("streamDate").setValue(getCurrentTime());
                Intent intent = new Intent(CommentActivity.this, CommentActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("user", user);
                b.putString("courseID", courseID);
                b.putString("selectedPost", post);
                b.putString("postId", postId);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    public void getData(){
        dataOperationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot cKeySnapshot : courseSnapshot.getChildren()) {
                        if (cKeySnapshot.getValue().toString().equals(courseID)) {
                            for (DataSnapshot streamSnapshot : courseSnapshot.child("stream").getChildren()) {
                                operationRef = courseSnapshot.child("stream").getRef();
                                streamKey = "stream" + postId;
                                if(streamSnapshot.getKey().toString().equalsIgnoreCase(streamKey)){
                                    for(DataSnapshot commentsSnapshot : streamSnapshot.getChildren()){
                                        for(DataSnapshot commentSnapshot : commentsSnapshot.getChildren()) {
                                            for(DataSnapshot commentKeySnapshot : commentSnapshot.getChildren()) {
                                                if (commentKeySnapshot.getKey().equalsIgnoreCase("commentContext")) {
                                                    commentContext = commentKeySnapshot.getValue().toString();
                                                }
                                                else if (commentKeySnapshot.getKey().equalsIgnoreCase("whoShared")) {
                                                    whoShared = commentKeySnapshot.getValue().toString();
                                                }
                                                else if(commentKeySnapshot.getKey().equalsIgnoreCase("streamDate")){
                                                    String dtStart = commentKeySnapshot.getValue().toString();
                                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                    try {
                                                        date = format.parse(dtStart);
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                            comments.put(whoShared, commentContext);
                                            String newdateFormat=parseDate(date.toString());
                                            commentList.add(whoShared + " - " + newdateFormat + "\n" + commentContext);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Collections.reverse(commentList);
                CustomAdapter customAdapter = new CustomAdapter(CommentActivity.this, R.layout.activity_rowcustom, (ArrayList<String>) commentList);
                commentsList.setAdapter(customAdapter);
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
    public String parseDate(String date)
    {
        String newDateFormat="";
        String year=date.substring(29);
        String words[]=date.split(" ");
        newDateFormat=(words[3]+" - "+words[2]+" "+words[1]+year+" - "+words[0]);
        return newDateFormat;
    }
}
