package com.example.jinglaixue.MainTabs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.jinglaixue.JxInfo;
import com.example.jinglaixue.MainActivity;
import com.example.jinglaixue.R;
import com.example.jinglaixue.UserAccount;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.Timestamp;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainTab02 extends Fragment implements View.OnClickListener {

    private View thisView;
    private Button btn_add;
    private Button btn_minus;
    private ImageView fatcat_study;
    private ImageButton btn_jxinfo;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.activity_j_xmode,container,false);

        unitUI();
        return thisView;
    }




    private TextView tv_time;
    @SuppressLint("HandlerLeak")
    private Handler time_handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            tv_time.setText(msg.obj.toString());
        }
    };
    Button btn_study;
    Runnable timerun;

    public boolean isStudy = false;


    private void unitUI() {
        fatcat_study=thisView.findViewById(R.id.fatcat_study);
        btn_add = thisView.findViewById(R.id.btn_add);
        btn_minus = thisView.findViewById(R.id.btn_minus);
        tv_time = (TextView) thisView.findViewById(R.id.tv_time);
        btn_study =  (Button)thisView.findViewById(R.id.btn_beginstudy);
        btn_jxinfo = thisView.findViewById(R.id.btn_jxinfo);

        btn_add.setOnClickListener(this);
        btn_minus.setOnClickListener(this);
        btn_jxinfo.setOnClickListener(this);
        thisView.findViewById(R.id.btn_beginstudy).setOnClickListener(this);

        fatcat_study.setVisibility(View.INVISIBLE);

        timerun = new Runnable() {
            @Override
            public void run() {
                timing();
                time_handler.postDelayed(this,1000);
            }

        };
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            //加号剪号
            case R.id.btn_add:

                if(isStudy)
                    break;

                String timeStr = tv_time.getText().toString().trim();

                String[] splits = timeStr.split(":");

                int h = Integer.parseInt(splits[0]);
                int m = Integer.parseInt(splits[1]);
                int s = Integer.parseInt(splits[2]);

                if (h >= 2)
                    break;

                m = m+30;
                if(m>=60){
                    h = h+1;
                    m = m % 60;
                }
                timeStr = String.format("%02d:%02d:%02d",h,m,s);
                tv_time.setText(timeStr);

                break;
            case R.id.btn_minus:

                if(isStudy)
                    break;

                timeStr = tv_time.getText().toString().trim();

                splits = timeStr.split(":");

                h = Integer.parseInt(splits[0]);
                m = Integer.parseInt(splits[1]);
                s = Integer.parseInt(splits[2]);

                if(m <= 30 && h==0)
                    break;
                m = m-30;
                if(m<0){
                    h = h - 1;
                    if(h<2){
                        thisView.findViewById(R.id.btn_add).setVisibility(View.VISIBLE);
                    }
                    m = 60 + m;
                    if(h<0){
                        h = 0;
                        m = 0;
                    }

                }

                timeStr = String.format("%02d:%02d:%02d",h,m,s);
                tv_time.setText(timeStr);
                break;
            //开始静学
            case R.id.btn_beginstudy:

                if(!isStudy){
                    //开始学习
                    isStudy = true;
                    fatcat_study.setVisibility(View.VISIBLE);
                    btn_add.setVisibility(View.INVISIBLE);
                    btn_minus.setVisibility(View.INVISIBLE);
                    time_handler.postDelayed(timerun, 0);
                    btn_study.setText("停止静学");

                    //声明hanler处理回调
                    @SuppressLint("HandlerLeak") final Handler handler = new Handler(){
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            JSONObject json = JSON.parseObject(msg.obj.toString());
                            if(json.getString("code").equals("1")){
                                //Toast.makeText(getContext(),"成功静学!",Toast.LENGTH_SHORT).show();
                                UserAccount.shard().setTime_id(json.getString("time_id"));
                            }else {
                                Toast.makeText(getContext(),"静学失败!"+json.getString("msg"),Toast.LENGTH_SHORT).show();
                            }
                        }
                    };


                    //向服务器发送token 、set_time
                    String set_time = tv_time.getText().toString().trim();


                    OkHttpClient okHttpClient = new OkHttpClient();
                    HttpUrl urlBuilder = HttpUrl.parse(UserAccount.shard().hostaddr+"/users/beginjx").newBuilder()
                            .addQueryParameter("token", UserAccount.shard().getToken())
                            .addQueryParameter("set_time", set_time)
                            .build();

                    Request request = new Request.Builder()
                            .url(urlBuilder)
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

                            }
                        }
                    });

                }else {
                    //声明handler处理回调的
                    @SuppressLint("HandlerLeak" ) final Handler handler = new Handler(){
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            JSONObject json = JSON.parseObject(msg.obj.toString());
                            if(json.getString("code").equals("1")){

                                UserAccount.shard().setTime_id(json.getString("time_id"));

                                String rewardText = json.getString("rewardText");

                                //弹出对话框
                                new AlertDialog.Builder(getContext())
                                        .setTitle(rewardText)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).show();
                            }else {
                                //弹出对话框
                                new AlertDialog.Builder(getContext())
                                        .setTitle("全球有73%的人能够坚持完成!再接再厉")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).show();
                            }
                        }
                    };


                    //结束学习
                    new AlertDialog.Builder(getContext()).setTitle("真的忍心放弃静学吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //点击确定触发的事件
                                    isStudy=false;
                                    fatcat_study.setVisibility(View.INVISIBLE);
                                    btn_add.setVisibility(View.VISIBLE);
                                    btn_minus.setVisibility(View.VISIBLE);
                                    time_handler.removeCallbacks(timerun);
                                    btn_study.setText("开始静学");
                                    //重置计时器
                                    tv_time.setText("01:30:00");




                                    //请求接口
                                    OkHttpClient okHttpClient = new OkHttpClient();
                                    HttpUrl urlBuilder = HttpUrl.parse(UserAccount.shard().hostaddr+"/users/endjx").newBuilder()
                                            .addQueryParameter("token", UserAccount.shard().getToken())
                                            .addQueryParameter("time_id",UserAccount.shard().getTime_id())
                                            .build();

                                    Request request = new Request.Builder()
                                            .url(urlBuilder)
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

                                            }
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //点击取消触发的事件
                                }
                            }).show();


                }

                break;
            case R.id.btn_jxinfo:
                Intent intent = new Intent();
                intent.setClass(getContext(), JxInfo.class);
                startActivity(intent);
                break;
        }
    }

    //计时线程
    private void timing(){

        String timeStr = tv_time.getText().toString().trim();

        String[] splits = timeStr.split(":");

        int h = Integer.parseInt(splits[0]);
        int m = Integer.parseInt(splits[1]);
        int s = Integer.parseInt(splits[2]);

        s = s - 1;
        if (s < 0){
            s = 59;
            m = m - 1;
            if(m<0){
                m = 59 ;
                h = h - 1;
                if(h < 0){
                    h = 0;
                }
            }
        }


        timeStr = String.format("%02d:%02d:%02d",h,m,s);
        Message msg = new Message();
        msg.obj = timeStr;

        time_handler.sendMessage(msg);
    }
}
