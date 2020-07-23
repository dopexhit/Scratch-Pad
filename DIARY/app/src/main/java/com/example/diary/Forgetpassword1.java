package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgetpassword1 extends AppCompatActivity {
    EditText enterEmail;
    Button sendPassword;
    ProgressBar loading;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword1);
        enterEmail=(EditText)findViewById(R.id.enterEmail);
        sendPassword=(Button)findViewById(R.id.sendPassword);
        loading=(ProgressBar)findViewById(R.id.loading);

        firebaseAuth=FirebaseAuth.getInstance();
        sendPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                String email=enterEmail.getText().toString().trim();
                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(Forgetpassword1.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    return;
                }
                firebaseAuth.sendPasswordResetEmail(enterEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loading.setVisibility(View.GONE);

                        if(task.isSuccessful()){
                            Toast.makeText(Forgetpassword1.this,"LINK SENT TO YOUR EMAIL TO RESET PASSWORD",Toast.LENGTH_LONG).show();
                        }else
                        {
                            Toast.makeText(Forgetpassword1.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }
}
