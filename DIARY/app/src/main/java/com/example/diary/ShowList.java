package com.example.diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;



public class ShowList extends AppCompatActivity implements TextWatcher{

    //Variable declaration
    ListView listView;
    FloatingActionButton newText;
    FirebaseDatabase database;
    DatabaseReference Ref;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    String uid;
    EditText rsearch;
    FloatingActionButton searchButton;
    Button logOut;
    ArrayList<Content> listnewsData = new ArrayList<Content>();
    MyCustomAdapter myadapter;
    Content content;

    //overiding onCreate Method

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        content = new Content();

        //finding element by id

        listView = (ListView) findViewById(R.id.listView);
        newText = (FloatingActionButton) findViewById(R.id.nextText);
        rsearch=(EditText)findViewById(R.id.search);
        searchButton=(FloatingActionButton)findViewById(R.id.searchButton);
        logOut=(Button)findViewById(R.id.logOut);

        //firebase referance

        database = FirebaseDatabase.getInstance();

        //getting uid of paricular user

        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();;

        //search content

        rsearch.addTextChangedListener(this);

        //Toast.makeText(ShowList.this,""+uid,Toast.LENGTH_LONG).show();

        //referance to content
        Ref = database.getReference("Content");

        //fetching element from database and adding to listview

        Ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    content = ds.getValue(Content.class);
                    String title1=content.getTitle();
                    String text1=content.getText();
                    String datetime=content.getDateandtime();
                    String edate=content.getEdate();
                    listnewsData.add(new Content(title1,text1,datetime,edate));
                }
                myadapter=new MyCustomAdapter(listnewsData);
                listView.setAdapter(myadapter);

                //for sorting list by created date

                Collections.sort(listnewsData,new CustomComparator());
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        //creating object of adapter

        myadapter = new MyCustomAdapter(listnewsData);

        //on clicking on + button start new activity editor

        newText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 =new Intent(getApplicationContext(),editor.class);
                intent1.putExtra("contact",uid);
                startActivity(intent1);
                finish();

            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent2 =new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent2);
                finish();
            }
        });
    }


    //for implementation of TextWatcher

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
     this.myadapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    //creating custom adapter

    public class MyCustomAdapter extends BaseAdapter implements Filterable {
            public ArrayList<Content> listnewsDataAdpater ;

            //for search feature

            public ArrayList<Content> ListnewsDataAdpaterFull;
            CustomFilter cs;

            public MyCustomAdapter(ArrayList<Content>  listnewsDataAdpater) {
                this.listnewsDataAdpater=listnewsDataAdpater;

                this.ListnewsDataAdpaterFull=new ArrayList<>(listnewsDataAdpater);
            }


            @Override
            public int getCount() {
                return listnewsDataAdpater.size();
            }

            @Override
            public String getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
               return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {

                //connecting itemview with list element

                LayoutInflater mInflater = getLayoutInflater();
                View myView = mInflater.inflate(R.layout.itemsview, null);

                final   Content s = listnewsDataAdpater.get(position);

                TextView tvTitle=( TextView)myView.findViewById(R.id.tvtitle);
                tvTitle.setText(s.getTitle());
                TextView datetime=(TextView)myView.findViewById(R.id.datetime);
                datetime.setText("CREATED ON : "+s.getDateandtime());
                if(s.getEdate()!=null)

                {
                TextView edate=(TextView)myView.findViewById(R.id.edate);
                edate.setText("EDITED ON : "+s.getEdate());}


                //on click on title goto tv activity

              tvTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent(getBaseContext(),tv.class);
                        intent.putExtra("msg1",s.getTitle());
                        intent.putExtra("msg2",s.getText());
                        intent.putExtra("contact",uid);
                        startActivity(intent);
                        finish();



                    }
                });

                return myView;
            }

            //overiding filter

            @Override
            public Filter getFilter() {
                if(cs==null)
                {
                    cs=new CustomFilter();
                }
                return cs;
            }

            class CustomFilter extends Filter
            {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results =new FilterResults();
                    if(constraint!=null && constraint.length()>0)
                    {
                        constraint=constraint.toString().toUpperCase();
                    ArrayList<Content> filters=new ArrayList<>();
                    for(int i=0;i<ListnewsDataAdpaterFull.size();i++) {
                        if (ListnewsDataAdpaterFull.get(i).getTitle().toUpperCase().contains(constraint))
                        {
                            Content content = new Content(ListnewsDataAdpaterFull.get(i).getTitle(), ListnewsDataAdpaterFull.get(i).getText(), ListnewsDataAdpaterFull.get(i).getDateandtime(), ListnewsDataAdpaterFull.get(i).getEdate());
                            filters.add(content);
                        }
                    }
                        results.count=filters.size();
                        results.values=filters;
                    }else
                    {
                        results.count=ListnewsDataAdpaterFull.size();
                        results.values=ListnewsDataAdpaterFull;
                    }

                        return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    listnewsDataAdpater=(ArrayList<Content>)results.values;
                    notifyDataSetChanged();

                }
            }
        }

     //overriding back button of phone

    private class CustomComparator implements Comparator<Content> {

        @Override
        public int compare(Content o1, Content o2) {
            return o2.getDateandtime().compareTo(o1.getDateandtime());
        }

    }

}








