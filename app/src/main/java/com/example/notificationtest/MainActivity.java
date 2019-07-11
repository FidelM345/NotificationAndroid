package com.example.notificationtest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {
    //1.Notification channel
    //2.Notification Builder
    //3.Notification manager

    private  static  final String Channel_ID="simplified_coding";
    private  static  final String Channel_Name="simplified coding";
    private  static  final String Channel_Desc="simplified coding notifications";

    EditText textme,password;
    Button login;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayNotificationOne();



        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

       /* initial startup of your app, the FCM SDK generates a registration token for the client app instance.
        If you want to target single devices or create device groups,
        you'll need to access this token by extending FirebaseMessagingService and overriding onNewToken.*/

        textme=findViewById(R.id.id_email);
        password=findViewById(R.id.id_password);
        progressBar=findViewById(R.id.id_progress);
        progressBar.setVisibility(View.INVISIBLE);

        findViewById(R.id.id_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email=textme.getText().toString().trim();
                final String password1=password.getText().toString().trim();

                progressBar.setVisibility(View.VISIBLE);

               validationChecks(email,password1);


                mAuth.createUserWithEmailAndPassword(email, password1)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Success", "createUserWithEmail:success");


                                    FirebaseUser user = mAuth.getCurrentUser();

                                    profileActivity();

                                } else {


                                    if(task.getException()instanceof FirebaseAuthUserCollisionException){


                                        userLogin(email,password1);
                                    }

                                   /* // If sign in fails, display a message to the user.
                                    Log.w("Error", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
*/
                                }

                            }
                        });




            }
        });






      /*  findViewById(R.id.buttonNotify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayNotification();
            }
        });*/
    }

    private void validationChecks( String email, String password1) {
        if(TextUtils.isEmpty(email)){
            textme.setError("Email Requried");
            textme.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(password1)){
            password.setError("Password Required");
            password.requestFocus();
            return;
        }

        if(password1.length()<6){

            password.setError("Password must be at least 4 characters long");
            password.requestFocus();
            return;

        }
    }


    private void profileActivity(){
        Intent intent=new Intent(MainActivity.this,ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void userLogin(String email,String password){

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent=new Intent(MainActivity.this,ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }else{

                   Toast.makeText(getApplicationContext(),""+task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void displayNotificationOne() {
        //display notification from android 8 (api level 26) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Channel_ID, Channel_Name, importance);
            channel.setDescription(Channel_Desc);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this



            Notification notification=new NotificationCompat.Builder(this,Channel_ID)
                    .setSmallIcon(R.mipmap.car_icon)
//                    .setContentTitle(title)
//                    .setContentText(body)
//                    .setContentIntent(pendingIntent)
//                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            Intent intent=new Intent(MainActivity.this,ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }

    private void displayNotification(){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Channel_ID)
                .setSmallIcon(R.drawable.ic_mail)
                .setContentTitle("Am working")
                .setContentText("My First Notification")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);



        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1,builder.build());

    }
}
