package com.example.diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class editor extends AppCompatActivity {

    //creating variables

    EditText ed , ed2;
    TextView textview1,textview2;
    FloatingActionButton bu1;
    FirebaseDatabase database;
    DatabaseReference Ref ;
    Content content;
    String uid ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getting referance to xml element

        setContentView(R.layout.activity_editor);
        ed = (EditText) findViewById(R.id.ed11);
        ed2=(EditText)findViewById(R.id.ed12);
        textview1=(TextView) findViewById(R.id.textView1);
        textview2=(TextView) findViewById(R.id.size);
        bu1=(FloatingActionButton)findViewById(R.id.searchButton);



        database = FirebaseDatabase.getInstance();
        Ref= database.getReference("Content") ;
        content=new Content();
        bu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title=ed2.getText().toString();
                String text= ed.getText().toString();
                content.setDateandtime();
                content.setText(text);
                content.setTitle(Title);
                uid = getIntent().getStringExtra("contact");

                //store data using push

                DatabaseReference Myref=Ref.child(uid).push();
                Myref.setValue(content);
                Intent intent =new Intent(getApplicationContext(),ShowList.class);
                startActivity(intent);
                finish();
            }
        });



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(getApplicationContext(),ShowList.class);
        startActivity(intent);
        finish();
    }
}
