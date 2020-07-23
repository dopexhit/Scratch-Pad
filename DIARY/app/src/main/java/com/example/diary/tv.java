package com.example.diary;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class tv extends AppCompatActivity {

    //creating array list of text color and text size

    String[] color=new String[]{"BLACK","BLUE","RED","GREEN","GREY"};
    String[] size=new String[]{"20","25","30","35","40"};

    //declaration of array

    Spinner textColor;
    Spinner textSize;
    EditText tvtitle;
    EditText tvtext;
    FloatingActionButton delete;
    private TextToSpeech mTTS;
    FloatingActionButton update;
    FloatingActionButton speak;
    FloatingActionButton share;
    Content content;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv);

        //referaance to speak button

        final Animation animation= AnimationUtils.loadAnimation(tv.this,R.anim.fadeout) ;
        speak=(FloatingActionButton)findViewById(R.id.speak);


        //Speak text

        mTTS=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                   int result= mTTS.setLanguage(Locale.ENGLISH);
                   if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED)
                   {
                       Log.e("TTS","Language not supported");

                   }
                   else
                   {
                       speak.setEnabled(true);
                   }
                }else
                {
                    Log.e("TTS","Initialisation Fail");
                }

            }
        });



        textSize=(Spinner)findViewById(R.id.textSize);
        tvtitle=(EditText) findViewById(R.id.tvtitle);
        tvtext=(EditText) findViewById(R.id.tvtext);
        delete=(FloatingActionButton) findViewById(R.id.delete);
        update=(FloatingActionButton) findViewById(R.id.update);
        share=(FloatingActionButton)findViewById(R.id.share);
        Bundle b=getIntent().getExtras();
        String title1=b.getString("msg1");
        String text1=b.getString("msg2");
        tvtitle.setText(title1);
        tvtext.setText(text1);

        //on pressing speak button

        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Textspeak();
            }
        });

        //spinner to change color of text

        textColor=(Spinner)findViewById(R.id.textColor);
        ArrayAdapter<String> adapter1 =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,color);
        textColor.setAdapter(adapter1);
        textColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                { tvtitle.setTextColor(Color.BLACK);
                   tvtext.setTextColor(Color.BLACK);}
                if(position==1)
                { tvtitle.setTextColor(Color.BLUE);
                    tvtext.setTextColor(Color.BLUE);}
                if(position==2)
                { tvtitle.setTextColor(Color.RED);
                    tvtext.setTextColor(Color.RED);}
                if(position==3)
                { tvtitle.setTextColor(Color.GREEN);
                    tvtext.setTextColor(Color.GREEN);}
                if(position==4)
                { tvtitle.setTextColor(Color.GRAY);
                    tvtext.setTextColor(Color.GRAY);}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //spinner to change text size

        textSize=(Spinner)findViewById(R.id.textSize);
        ArrayAdapter<String> adapter2 =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,size);
        textSize.setAdapter(adapter2);
        textSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {tvtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                    tvtext.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);}
                if(position==1)
                {tvtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                    tvtext.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);}
                if(position==2)
                {tvtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                    tvtext.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);}
                if(position==3)
                {tvtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,35);
                    tvtext.setTextSize(TypedValue.COMPLEX_UNIT_SP,35);}
                if(position==4)
                {tvtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
                    tvtext.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);}

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //to delete from firebase

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b=getIntent().getExtras();
                String title1=b.getString("msg1");
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                uid = getIntent().getStringExtra("contact");
                Query applesQuery = ref.child("Content").child(uid).orderByChild("title").equalTo(title1);

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //  Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
                Intent intent1=new Intent(getApplicationContext(),ShowList.class);
                startActivity(intent1);
                finish();
            }
        });

        //to update to firebase

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b=getIntent().getExtras();
                String title1=b.getString("msg1");
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                String value = getIntent().getStringExtra("contact");
                uid = getIntent().getStringExtra("contact");
                Query query = rootRef.child("Content").child(uid).orderByChild("title").equalTo(title1);
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {

                            String Title=tvtitle.getText().toString();
                            String text= tvtext.getText().toString();
                            ds.child("title").getRef().setValue(Title);
                            ds.child("text").getRef().setValue(text);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy  HH:mm:ss ");
                            String eDateandTime = sdf.format(new Date());
                            ds.child("edate").getRef().setValue(eDateandTime);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                query.addListenerForSingleValueEvent(eventListener);
                Intent intent1=new Intent(getApplicationContext(),ShowList.class);
                startActivity(intent1);
                finish();

            }
        });


        //to share on different platforms

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b=getIntent().getExtras();
                String title1=b.getString("msg1");
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                uid = getIntent().getStringExtra("contact");
                Query applesQuery = ref.child("Content").child(uid).orderByChild("title").equalTo(title1);
                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren()) {
                            Content c =ds.getValue(Content.class);
                            String sharebody= "TITLE :"+c.getTitle()+"\n"+"TEXT :"+ c.getText()+"\n"+"CREATED ON :"+c.getDateandtime()+"\n"+"EDITED ON :"+c.getEdate()+"\n";
                            Intent myintent=new Intent(Intent.ACTION_SEND);
                            myintent.setType("text/plain");

                            String sharesub = "MY DIARY CONTENT";
                            myintent.putExtra(Intent.EXTRA_SUBJECT,sharesub);
                            myintent.putExtra(Intent.EXTRA_TEXT,sharebody);
                            startActivity(Intent.createChooser(myintent,"SHARE"));}
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
        });
    }

    //creating Textspeak function


    private  void Textspeak()
    {
        String text=tvtext.getText().toString();
        mTTS.setPitch((float)0.85);
        mTTS.setSpeechRate((float) 0.85);
        mTTS.speak(text,TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    protected void onDestroy() {
        if(mTTS!=null){
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), ShowList.class);
        startActivity(intent);
        finish();
    }
}
