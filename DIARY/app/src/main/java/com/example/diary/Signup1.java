package com.example.diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup1 extends AppCompatActivity {

    //declaring vatiables

    EditText Email,Password ;
    Button signUp;
    private FirebaseAuth firebaseAuth;
    TextView tvsignin;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);

        //getting referance to variables

        Email=(EditText)findViewById(R.id.enterEmail);
        Password=(EditText)findViewById(R.id.password);
        signUp=(Button)findViewById(R.id.sendPassword);
        tvsignin=(TextView)findViewById(R.id.tvSignin);
        loading=(ProgressBar)findViewById(R.id.loading);
        firebaseAuth=FirebaseAuth.getInstance();

        //on pressing textview go back to login activity

        tvsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        //on pressing signup register will start

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                String email=Email.getText().toString().trim();
                String password =Password.getText().toString().trim();
                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(Signup1.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    signUp.setVisibility(View.VISIBLE);
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(Signup1.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    signUp.setVisibility(View.VISIBLE);
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Signup1.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                loading.setVisibility(View.GONE);
                                signUp.setVisibility(View.VISIBLE);
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    Toast.makeText(Signup1.this,"USER REGISTERED",Toast.LENGTH_LONG).show();
                                    finish();
                                } else {

                                    Toast.makeText(Signup1.this,"AUTHENTICATON FAIL",Toast.LENGTH_LONG).show();

                                }

                            }
                        });

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent =new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    }
}
