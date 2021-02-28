package com.example.cengonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MessageActivity  extends AppCompatActivity  {

    private static final String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss" ;
    private DatabaseReference dbOperationReference,messageRef,receiverRef, inboxRef;
    private  Message message;
    private ListView inboxList;
    private TextView typeText;
    private EditText mailText;
    private EditText newMessageText;
    private Button backButton;
    private Button sendMessageButton;
    private Button newMessageButton;
    private Button inboxButton;
    private Date date;
    private User user;
    private User receiver;
    private User sender;
    private List<Message> messages;
    private String receiverEmail="";
    private String id, name, department, password, userName;
    private boolean isRead;
    private String context, dtStart, senderUsername;
    private boolean userType;
    private int senderInboxSize;
    private List<Message> allMessages;
    private List<String> messagesList;
    private CustomAdapter customAdapter;
    private TextView messageContent;
    private Button home;
    private User onlineUser;
    private List<String> courses;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        courses = new ArrayList<>();
        messages = new ArrayList<>();
        allMessages = new ArrayList<>();
        messagesList = new ArrayList<>();
        dbOperationReference = FirebaseDatabase.getInstance().getReference().child("User");
        messageRef = FirebaseDatabase.getInstance().getReference().child("User");
        inboxRef = FirebaseDatabase.getInstance().getReference().child("User");
        receiverRef = FirebaseDatabase.getInstance().getReference();
        user = (User) getIntent().getSerializableExtra("user");

        if(user.isUsertype() == true){
            onlineUser = (Teacher) getIntent().getSerializableExtra("teacher");
        }
        else if(user.isUsertype() == false){
            onlineUser = (Student) getIntent().getSerializableExtra("student");
        }
        courses = getIntent().getStringArrayListExtra("course");

        typeText = findViewById(R.id.typeMessageText);
        mailText = findViewById(R.id.mailText);
        newMessageText = findViewById(R.id.newMessageText);
        backButton = findViewById(R.id.backButton);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        newMessageButton = findViewById(R.id.newMessageButton);
        messageContent = findViewById(R.id.messageContent);
        inboxList = findViewById(R.id.messagesList);
        home = findViewById(R.id.home);


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.isUsertype()==true){
                    Intent intent = new Intent(MessageActivity.this, TeacherCourseActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("user", user);
                    b.putSerializable("teacher", onlineUser);
                    intent.putStringArrayListExtra("course", (ArrayList<String>) courses);
                    intent.putExtras(b);
                    startActivity(intent);
                }
                if(user.isUsertype()==false){
                    Intent intent = new Intent(MessageActivity.this, StudentCourseActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("user", user);
                    b.putSerializable("student", onlineUser);
                    intent.putStringArrayListExtra("course", (ArrayList<String>) courses);
                    intent.putExtras(b);
                    startActivity(intent);
                }

            }
        });

        newMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inboxList.setVisibility(View.INVISIBLE);
                newMessageButton.setVisibility(View.INVISIBLE);
                backButton.setVisibility(View.VISIBLE);
                typeText.setVisibility(View.VISIBLE);
                mailText.setVisibility(View.VISIBLE);
                newMessageText.setVisibility(View.VISIBLE);
                sendMessageButton.setVisibility(View.VISIBLE);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MessageActivity.this, MessageActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("user", user);
                        b.putSerializable("teacher", onlineUser);
                        b.putSerializable("student", onlineUser);
                        intent.putStringArrayListExtra("course", (ArrayList<String>) courses);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });
                sendMessageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        inboxList.setVisibility(View.INVISIBLE);
                        receiverEmail = mailText.getText().toString();
                        sendMessage();
                    }
                });
            }
        });

        inboxList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                messageContent.setVisibility(View.VISIBLE);
                messageContent.setText(allMessages.get(i).getMessage());
                messageContent.setTextColor(0xff000000);
                inboxList.setVisibility(View.INVISIBLE);
                newMessageButton.setVisibility(View.INVISIBLE);
                backButton.setVisibility(View.VISIBLE);
                typeText.setVisibility(View.VISIBLE);
                newMessageText.setVisibility(View.VISIBLE);
                sendMessageButton.setVisibility(View.VISIBLE);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MessageActivity.this, MessageActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("user", user);
                        b.putSerializable("teacher", onlineUser);
                        b.putSerializable("student", onlineUser);
                        intent.putStringArrayListExtra("course", (ArrayList<String>) courses);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });
                sendMessageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        receiverEmail = allMessages.get(i).getSender().getUsername();
                        sendMessage();
                    }
                });
            }
        });

        messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if (userSnapshot.child("username").getValue().toString().equals(user.getUsername())) {
                        for (DataSnapshot messagesSnapshot : userSnapshot.child("messages").getChildren()){
                            for(DataSnapshot messageSnapshot : messagesSnapshot.getChildren()){
                                if(messageSnapshot.getKey().equalsIgnoreCase("messageContext")){
                                    context = messageSnapshot.getValue().toString();
                                }
                                else if(messageSnapshot.getKey().equalsIgnoreCase("date")){
                                    date=getCurrentDate(messageSnapshot.getValue().toString());
                                }
                                else if(messageSnapshot.getKey().equalsIgnoreCase("sender")){
                                    senderUsername = messageSnapshot.getValue().toString();
                                    for (DataSnapshot senderSnapshot : dataSnapshot.getChildren()) {
                                        if (senderSnapshot.child("username").getValue().toString().equals(senderUsername)) {
                                            id = senderSnapshot.child("ID").getValue().toString();
                                            name = senderSnapshot.child("name").getValue().toString();
                                            department = senderSnapshot.child("department").getValue().toString();
                                            password = senderSnapshot.child("password").getValue().toString();
                                            userName = senderSnapshot.child("username").getValue().toString();
                                            sender = new User(id, name, department, password, userName);
                                        }
                                    }
                                }
                                else if(messageSnapshot.getKey().equalsIgnoreCase("isRead")){
                                    isRead = Boolean.valueOf(messageSnapshot.getValue().toString());
                                }
                            }
                            message = new Message(sender, user, date, context, isRead);
                            allMessages.add(message);
                        }

                        Collections.sort(allMessages, new Comparator<Message>() {
                            public int compare(Message m1, Message m2) {
                                return m2.getDate().compareTo(m1.getDate());
                            }
                        });

                        for(int i = 0; i < allMessages.size(); i++){
                            String date=parseDate(allMessages.get(i).getDate().toString());
                            messagesList.add("Message \nFrom: " + allMessages.get(i).getSender().getUsername()+
                                    "\nTo: "+allMessages.get(i).getReceiver().getUsername()+
                                    "\nDate: "+date+
                                    "\nMessage: "+allMessages.get(i).getMessage());
                        }
                    }
                }
                customAdapter = new CustomAdapter(MessageActivity.this, R.layout.activity_rowcustom, (ArrayList<String>) messagesList){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent){
                        View view = super.getView(position, convertView, parent);
                        ViewGroup.LayoutParams params = view.getLayoutParams();
                        params.height = 250;
                        view.setLayoutParams(params);
                        return view;
                    }
                };
                inboxList.setAdapter(customAdapter);
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
        dtStart = currentDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = format.parse(dtStart);
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
    public void sendMessage(){
        dbOperationReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot userKeySnapshot : userSnapshot.getChildren()) {
                        if (userKeySnapshot.getValue().toString().equals(receiverEmail)) {
                            receiverRef = userSnapshot.child("messages").getRef();
                            id = userSnapshot.child("ID").getValue().toString();
                            name = userSnapshot.child("name").getValue().toString();
                            department = userSnapshot.child("department").getValue().toString();
                            password = userSnapshot.child("password").getValue().toString();
                            userName = userSnapshot.child("username").getValue().toString();
                            receiver = new User(id, userName, department, password, userName);
                            senderInboxSize = (int) userSnapshot.child("messages").getChildrenCount();
                           System.out.println("mel" + senderInboxSize);
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                date = format.parse(getCurrentTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            message=new Message(user,receiver,date,newMessageText.getText().toString(),false);
                            receiverRef.child("message"+(senderInboxSize + 1)).child("date").setValue(getCurrentTime());
                            receiverRef.child("message"+(senderInboxSize + 1)).child("isRead").setValue(false);
                            receiverRef.child("message"+(senderInboxSize + 1)).child("messageContext").setValue(newMessageText.getText().toString());
                            receiverRef.child("message"+(senderInboxSize + 1)).child("receiver").setValue(receiver.getUsername());
                            receiverRef.child("message"+(senderInboxSize + 1)).child("sender").setValue(user.getUsername());
                            allMessages.add(message);
                            Toast.makeText(MessageActivity.this,  "Your message has been sent.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MessageActivity.this, MessageActivity.class);
                            Bundle b = new Bundle();
                            b.putSerializable("user", user);
                            b.putSerializable("teacher", onlineUser);
                            b.putSerializable("student", onlineUser);
                            intent.putStringArrayListExtra("course", (ArrayList<String>) courses);
                            intent.putExtras(b);
                            startActivity(intent);
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
