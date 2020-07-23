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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    //declaring variables

    Button logIn ;
    EditText Email;
    EditText Password;
    Button register;
    private FirebaseAuth firebaseAuth;
    String uid;
    ProgressBar loading;
    TextView forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(MainActivity.this,ShowList.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }


        //getting referance to the element

        logIn=(Button)findViewById(R.id.login);
        register=(Button)findViewById(R.id.Register);
        Email=(EditText)findViewById(R.id.email) ;
        Password=(EditText)findViewById(R.id.password);
        loading=(ProgressBar)findViewById(R.id.loading);
        forgetPassword=(TextView)findViewById(R.id.forgetPassword);



        firebaseAuth=FirebaseAuth.getInstance();

        //creating login button

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);

                String email=Email.getText().toString().trim();
                String password =Password.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(MainActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    logIn.setVisibility(View.VISIBLE);
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(MainActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    logIn.setVisibility(View.VISIBLE);
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                loading.setVisibility(View.GONE);
                                logIn.setVisibility(View.VISIBLE);
                                if (task.isSuccessful()) {
                                    Intent intent1 =new Intent(getApplicationContext(),ShowList.class);
                                    startActivity(intent1);
                                    finish();
                                    Toast.makeText(MainActivity.this,"LOG IN SUCCESSFULL",Toast.LENGTH_LONG).show();


                                } else {
                                    Toast.makeText(MainActivity.this,"ENTER CORRECT EMAIL OR PASSWORD",Toast.LENGTH_LONG).show();

                                }

                            }
                        });


            }
        });

  //creating register button to open newactivity

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),Signup1.class);
                startActivity(intent);
                finish();

            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Forgetpassword1.class));
            }
        });





    }



}
