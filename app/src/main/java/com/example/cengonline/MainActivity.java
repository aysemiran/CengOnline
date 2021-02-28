package com.example.cengonline;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference dataRef;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton ;
    private Button forgetButton;
    private User user;
    private Student student;
    private Teacher teacher;
    private List<String> courses;
    private String id, name, department, password, userName;
    private boolean userType;
    private int numberOfCourse;
    private String userLoginEmail,userLoginPassword;

    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ImageView myImageView = findViewById(R.id.logo);
        myImageView.setImageResource(R.drawable.logo);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        forgetButton = findViewById(R.id.forgotButton);
        dataRef = FirebaseDatabase.getInstance().getReference().child("User");
        user = new User();
        courses = new ArrayList<>();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userLoginEmail = usernameEditText.getText().toString().trim();
                userLoginPassword = passwordEditText.getText().toString().trim();
                checkUserData();

            }
        });

        forgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPassActivity.class);
                startActivity(intent);
            }
        });
    }

    public void checkUserData()
    {
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot.child("username").getValue().toString().equals(userLoginEmail)) {
                        if(snapshot.child("userType").getValue().toString().equals("false"))
                        {
                            user.setUsertype(false);
                            id = snapshot.child("ID").getValue().toString();
                            name = snapshot.child("name").getValue().toString();
                            department = snapshot.child("department").getValue().toString();
                            numberOfCourse = Integer.parseInt(snapshot.child("numberOfCourses").getValue().toString());
                            password = snapshot.child("password").getValue().toString();
                            userType = false;
                            userName = snapshot.child("username").getValue().toString();
                            student = new Student(id, name, numberOfCourse, userName, password, department, userType);
                            for(DataSnapshot courseSnapshot : snapshot.child("courses").getChildren()){
                                courses.add(courseSnapshot.getValue().toString());
                            }
                        }
                        else if(snapshot.child("userType").getValue().toString().equals("true"))
                        {
                            user.setUsertype(true);
                            id = snapshot.child("ID").getValue().toString();
                            name = snapshot.child("name").getValue().toString();
                            department = snapshot.child("department").getValue().toString();
                            numberOfCourse = Integer.parseInt(snapshot.child("numberOfCourses").getValue().toString());
                            password = snapshot.child("password").getValue().toString();
                            userType = true;
                            userName = snapshot.child("username").getValue().toString();
                            teacher = new Teacher(id, name, numberOfCourse, userName, password, department, userType);
                            for(DataSnapshot courseSnapshot : snapshot.child("courses").getChildren()){
                                courses.add(courseSnapshot.getValue().toString());
                            }

                        }
                        user.setUsername(snapshot.child("username").getValue().toString());
                        user.setPassword(snapshot.child("password").getValue().toString());
                        user.setDepartment(department);
                        user.setID(id);
                        user.setName(name);
                        user.setNumberofCourses(numberOfCourse);
                    }
                }
                if(user.getUsername()==null)
                {
                    Toast.makeText(MainActivity.this, "User is not found", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else if(user.getPassword().equals(userLoginPassword) && user.getUsername().equals(userLoginEmail) ){
                    if(user.isUsertype())
                    {
                        Intent sendUser = new Intent(MainActivity.this,TeacherCourseActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("teacher", teacher);
                        b.putSerializable("user",user);
                        sendUser.putStringArrayListExtra("course", (ArrayList<String>) courses);
                        sendUser.putExtras(b);
                        startActivity(sendUser);
                    }
                    else if(!user.isUsertype())
                    {
                        Intent sendUser = new Intent(MainActivity.this,StudentCourseActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("student", student);
                        b.putSerializable("user", user);
                        sendUser.putStringArrayListExtra("course", (ArrayList<String>) courses);
                        sendUser.putExtras(b);
                        startActivity(sendUser);
                    }
                    Toast.makeText(MainActivity.this,"Login Success",Toast.LENGTH_LONG).show();
                }
                else if(user.getUsername().equals(userLoginEmail) && !user.getPassword().equals(userLoginPassword)) {
                    Toast.makeText(MainActivity.this, "Password is incorrect", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}