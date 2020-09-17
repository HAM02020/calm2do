package com.example.jinglaixue;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username;
    private EditText password;
    private Button btn_login;
    private Button btn_toRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        unitUI();
    }


    private void unitUI() {
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        btn_login  = findViewById(R.id.btn_login);
        btn_toRegister = findViewById(R.id.btn_toRegister);

        btn_login.setOnClickListener(this);
        btn_toRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:

                //获取用户名和密码
                String usernameStr = username.getText().toString().trim();
                String psdStr = password.getText().toString().trim();
                //检测是否输入了密码
                if (psdStr.equals("") || usernameStr.equals("")){
                    Toast.makeText(this,"请输入账号或密码!",Toast.LENGTH_SHORT).show();
                    break;
                }

                //创建一个handler处理回调
                @SuppressLint("HandlerLeak") final Handler handler = new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        JSONObject json = JSON.parseObject(msg.obj.toString());
                        String code = json.getString("code");
                        if(code.equals("1")){
                            String token = json.getString("token");
                            UserAccount.shard().setToken(token);
                            Log.i("woaini",UserAccount.shard().getToken());
                            Toast.makeText(getBaseContext(),"登陆成功！",Toast.LENGTH_SHORT).show();
                            //跳转界面
                            Intent intent = new Intent();
                            //防止按返回键回到登陆页面
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setClass(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getBaseContext(),"用户名或密码错误！",Toast.LENGTH_SHORT).show();
                        }

                    }
                };


                //请求后台服务器
                OkHttpClient okHttpClient = new OkHttpClient();
                final RequestBody requestBody =new FormBody.Builder()
                        .add("email",usernameStr)
                        .add("password",psdStr)
                        .build();
                Request request = new Request.Builder()
                        .url(UserAccount.shard().hostaddr+"/users/login")
                        .post(requestBody)
                        .build();
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
                            Log.i("woaini",msg.obj.toString());
                        }
                    }
                });
                break;
            case R.id.btn_toRegister:
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
