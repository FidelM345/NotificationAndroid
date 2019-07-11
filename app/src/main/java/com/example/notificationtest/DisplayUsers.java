package com.example.notificationtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DisplayUsers extends AppCompatActivity implements View.OnClickListener{
    Button send;
    EditText title,content;
    TextView email;
    Retrofit retrofit;
    Api api;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        email=findViewById(R.id.id_email);
        title=findViewById(R.id.id_title);
        content=findViewById(R.id.id_content);
        send=findViewById(R.id.id_send);
        Intent intent=getIntent();

        if(!TextUtils.isEmpty(intent.getStringExtra("email"))){

            email.setText(intent.getStringExtra("email"));

        }

        token=intent.getStringExtra("token");

        send.setOnClickListener(this);


       retrofit=new Retrofit.Builder()
               .baseUrl("https://thebeast-2fd61.firebaseapp.com/api/")
               .addConverterFactory(GsonConverterFactory.create())
               .build();

       api=retrofit.create(Api.class);

//        HashMap<String, String> paramMap = new HashMap<>();
//        paramMap.put("token", data);
//        paramMap.put("title", data);
//        paramMap.put("body", data);
//
//        RequestParams params = new RequestParams(paramMap);
//
//        AsyncHttpClient myClient = new AsyncHttpClient();


    }

    @Override
    public void onClick(View v) {

        String title1=title.getText().toString().trim();
        String content1=content.getText().toString().trim();
        final String url="https://thebeast-2fd61.firebaseapp.com/api/send";

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("token", token);
        paramMap.put("title", title1);
        paramMap.put("body", content1);

        RequestParams params = new RequestParams(paramMap);

        AsyncHttpClient myClient = new AsyncHttpClient();

        myClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.i("Beast","Success = "+statusCode);
                Toast.makeText(getApplicationContext(),"Message Sent Successfully",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("Beast","Error_Code = "+statusCode+" Error_Messo="+error.getLocalizedMessage());
                Toast.makeText(getApplicationContext(),"Message Failed to send",Toast.LENGTH_LONG).show();

            }
        });








//        if(!TextUtils.isEmpty(title1)&&!TextUtils.isEmpty(content1)){
//            Call<ResponseBody> call=api.sendNotification(token,title1,content1);
//
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                    try {
//                        Toast.makeText(getApplicationContext(),"successful "+response.body().string(),Toast.LENGTH_LONG).show();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                }
//            });
//
//        }else{
//            Toast.makeText(getApplicationContext(),"Please fill all the fields",Toast.LENGTH_LONG).show();
//        }

    }
}
