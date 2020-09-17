package com.example.jinglaixue;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ed_username;
    private EditText ed_email;
    private EditText ed_password;
    private EditText ed_password2;
    private Button btn_register;
    private TextView tv_login;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUI();
    }

    private void initUI() {
        ed_username = findViewById(R.id.ed_username);
        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        ed_password2 = findViewById(R.id.ed_password2);
        btn_register = findViewById(R.id.btn_register);
        tv_login = findViewById(R.id.tv_loginbtn);

        btn_register.setOnClickListener(this);
        tv_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //登陆
            case R.id.tv_loginbtn:
                Intent intent=new Intent();
                intent.setClass(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_register:
                //获取编辑框中的账户密码信息
                String email = ed_email.getText().toString().trim();
                String username = ed_username.getText().toString().trim();
                String password = ed_password.getText().toString().trim();
                String password2 = ed_password2.getText().toString().trim();
                //检测密码是否一致
                if(!password.equals(password2))
                {
                    Toast.makeText(this,"两次密码不一致,请重新输入。",Toast.LENGTH_SHORT).show();
                    break;
                }

                //发起request请求
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody =new FormBody.Builder()
                        .add("email",email)
                        .add("username",username)
                        .add("password",password)
                        .build();
                Request request = new Request.Builder()
                        .url(UserAccount.shard().hostaddr+"/users/register")
                        .post(requestBody)
                        .build();

                @SuppressLint("HandlerLeak") final Handler handler = new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        String repsonseStr = msg.obj.toString();
                        JSONObject responseJson = JSON.parseObject(repsonseStr);
                        //注册成功
                        if(responseJson.get("msg").equals("success")){
                            Toast.makeText(getBaseContext(),"注册成功！请登陆。",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent();
                            intent.setClass(getApplicationContext(),LoginActivity.class);
                            startActivity(intent);
                        }else {
                            //注册失败
                            Toast.makeText(getBaseContext(),"用户名已存在",Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if(response.isSuccessful()){
                            Message msg = new Message();
                            msg.obj = response.body().string();
                            handler.sendMessage(msg);

                        }

                    }
                });





                        //获取response 信息（是否注册成功）

                break;
        }
    }


}
