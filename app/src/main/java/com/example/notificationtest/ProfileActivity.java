package com.example.notificationtest;

import android.app.NotificationManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    LinearLayoutManager layoutManager;
    List<User>mUserList;
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayusers);
        recyclerView=findViewById(R.id.userlist_recycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mUserList=new ArrayList<>();




        mDatabase = FirebaseDatabase.getInstance().getReference();


        FirebaseApp.initializeApp(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();




        //using the topic option to send notification to the app users
        FirebaseMessaging.getInstance().subscribeToTopic("updates");

        //generates token - using token option to send notification to a specific device
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if(task.isSuccessful()){
                             // Get new Instance ID token
                             String token = task.getResult().getToken();
                             saveToken(token);
                            // textme.setText("Token: "+token);

                        }else {


                        }

                    }
                });





        seeUsers();


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1);

    }


    private void seeUsers(){
        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              /*  User user=dataSnapshot.getValue(User.class);
                dataSnapshot.exists();*/
              mUserList.clear();

                if(dataSnapshot.exists()){
                    Log.i("Beast","Values="+dataSnapshot.getChildrenCount());
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        User user= dataSnapshot1.getValue(User.class);
                        mUserList.add(user);
                        Log.i("Beast","Values="+user.getEmail());

                    }
                    userAdapter=new UserAdapter(ProfileActivity.this,mUserList);
                    userAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(userAdapter);

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    private void saveToken(String token) {

        String email=mAuth.getCurrentUser().getEmail();
        User user=new User(email,token);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users");

        databaseReference.child(mAuth.getCurrentUser().getUid()).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            Toast.makeText(getApplicationContext(),"Saved Successfully",Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(getApplicationContext(),""+task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                });



    }


    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()==null){
            Intent intent=new Intent(ProfileActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }
}
