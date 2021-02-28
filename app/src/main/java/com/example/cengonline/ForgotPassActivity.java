package com.example.cengonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText userEmail;
    private Button buttonEmail;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        userEmail=(EditText)findViewById(R.id.userResetPass);
        buttonEmail=(Button)findViewById(R.id.buttonReset);
        buttonEmail.setOnClickListener(this);
        firebaseAuth=FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        if(view == buttonEmail){
            firebaseAuth.sendPasswordResetEmail(userEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(ForgotPassActivity.this,"Your password has been sent to your mail.",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(ForgotPassActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
