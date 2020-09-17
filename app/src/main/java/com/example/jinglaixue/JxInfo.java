package com.example.jinglaixue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class JxInfo extends AppCompatActivity {

    private int finish_count;
    private int fail_count;
    private Double finish_duration;
    private List<FailTimes> failTimesList;

    private TextView tv_finish_count;
    private TextView tv_finish_duration;
    private TextView tv_fail_count;
    private TextView tv_fail_info;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jx_info);

        tv_finish_count = findViewById(R.id.tv_finsh_count);
        tv_finish_duration = findViewById(R.id.tv_finsh_duration);
        tv_fail_count = findViewById(R.id.tv_fail_count);
        tv_fail_info = findViewById(R.id.tv_fail_info);



        //声明handler处理网络数据回调
        @SuppressLint("HandlerLeak") final Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                JSONObject json = JSON.parseObject(msg.obj.toString());
                if(json.getInteger("code")!=1){
                    Toast.makeText(getBaseContext(),"出现错误！",Toast.LENGTH_LONG).show();
                    return;
                }
                finish_count = json.getInteger("finish_count");
                fail_count = json.getInteger("fail_count");
                finish_duration = json.getDouble("finish_duration");


                JSONArray jsonArray = json.getJSONArray("fail_times");
                failTimesList = jsonArray.toJavaList(FailTimes.class);

                //把数据传给视图
                tv_finish_count.setText("静学成功的次数为："+finish_count+"次");
                tv_finish_duration.setText("静学成功的时长为："+secondToTime(finish_duration.longValue()));
                tv_fail_count.setText("静学失败的次数为："+fail_count+"次");

                FailTimes failTimes = failTimesList.get(0);
                failTimes.formatData();

                tv_fail_info.setText("你在"+failTimes.getTime()+"，静学了"+failTimes.getDurationStr()+"就放弃了");
                for(FailTimes fail : failTimesList){
                    fail.formatData();
                    String d = fail.getDurationStr();
                    String s = fail.getSet_timeStr();
                }

            }
        };




        //发起网络请求
        OkHttpClient okHttpClient = new OkHttpClient();
        HttpUrl urlBuilder = HttpUrl.parse(UserAccount.shard().hostaddr+"/users/jxinfo").newBuilder()
                .addQueryParameter("token", UserAccount.shard().getToken())
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
                    Message msg = new Message();
                    msg.obj = response.body().string();
                    handler.sendMessage(msg);
            }
        });


    }
    /**
     * 返回日时分秒
     * @param second
     * @return
     */
    private String secondToTime(long second) {
        long days = second / 86400;//转换天数
        second = second % 86400;//剩余秒数
        long hours = second / 3600;//转换小时数
        second = second % 3600;//剩余秒数
        long minutes = second / 60;//转换分钟
        second = second % 60;//剩余秒数
        if (0 < days){
            return days + "天，"+hours+"小时，"+minutes+"分，"+second+"秒";
        }else if (hours == 0){
            return minutes+"分，"+second+"秒";
        }else if(minutes == 0){
            return second+"秒";
        }else {
            return hours+"小时，"+minutes+"分，"+second+"秒";
        }
    }



}
