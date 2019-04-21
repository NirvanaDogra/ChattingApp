package com.example.chatting;

import android.content.Context;
import android.graphics.Color;
import android.os.Debug;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Firebase mRef;
    private Button sendData;
    private ListView listView, listView2;
    private TextView txtView;
    ArrayList<String> itemList;
    ArrayList<String> itemList2;
    private ArrayAdapter<String> adapter, adapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mAuth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);




        txtView =(TextView) findViewById(R.id.UserName);
        final String Uname="user"+ new Random().nextInt(100);
        txtView.setText(Uname);




        sendData=(Button)findViewById(R.id.send);

        listView = (ListView) findViewById(R.id.list_of_messages);
        listView2 = (ListView) findViewById(R.id.list_of_messages2);

        String[] items={"Apple","Banana","Clementine"};
        String[] items2={"Apple1","Bana1na","Clem2entine"};

        itemList=new ArrayList<String>(Arrays.asList(items));
        itemList2=new ArrayList<String>(Arrays.asList(items2));

        adapter=new ArrayAdapter<String>(this,R.layout.list_items,R.id.txtview,itemList);
        adapter2=new ArrayAdapter<String>(this,R.layout.list_items,R.id.inputView,itemList2);

        ListView listV=(ListView)findViewById(R.id.list_of_messages);
        ListView listV2=(ListView)findViewById(R.id.list_of_messages2);

        listV.setAdapter(adapter);
        listV2.setAdapter(adapter2);


        mRef=new Firebase("https://chatting-2e9b2.firebaseio.com/");



        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean change=false;
                for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){

                    Toast toast2=Toast.makeText(getApplicationContext(),(String)uniqueKeySnapshot.getKey()+change,Toast.LENGTH_SHORT);
                    toast2.show();
                    String a=(String)uniqueKeySnapshot.getKey();
                    Toast toast=Toast.makeText(getApplicationContext(),a,Toast.LENGTH_SHORT);
                    toast.show();
                    if((a.equals("User"))){
                        if(!(a.equals(Uname))){

                            change=true;
                        }

                    }
                    if(change && uniqueKeySnapshot.getKey().equals("Message")){
                        itemList2.add((String) uniqueKeySnapshot.getValue());
                        adapter2.notifyDataSetChanged();
                        change=false;

                    }
                }



            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText et = (EditText) findViewById(R.id.message);
                String str = et.getText().toString();

                itemList.add(str);
                adapter.notifyDataSetChanged();



                et.setText("");

                Firebase mRefChild=mRef.child("Message");
                mRefChild.setValue(str);

                Firebase mRefChild2=mRef.child("User");
                mRefChild2.setValue(Uname);

            }
        });

    }
    public void createTextView(String sText){
        LinearLayout ll=new LinearLayout(this);

        // Create TextView
        TextView product = new TextView(this);
        product.setText(sText);
        ll.addView(product);



    }
}