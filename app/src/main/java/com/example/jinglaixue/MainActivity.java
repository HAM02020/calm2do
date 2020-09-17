package com.example.jinglaixue;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.jinglaixue.MainTabs.MainTab01;
import com.example.jinglaixue.MainTabs.MainTab02;
import com.example.jinglaixue.MainTabs.MainTab03;
import com.example.jinglaixue.MainTabs.MainTab04;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity  implements View.OnClickListener {


    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();

    private RadioGroup rg_group;

    MainTab01 tab01;
    MainTab02 tab02;
    MainTab03 tab03;
    MainTab04 tab04;


    @SuppressLint("HandlerLeak")
    public Handler homePressHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            new AlertDialog.Builder(getBaseContext()).setTitle("真的要退出吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
    };
    //自定义的广播接收者
    private HomeWatcherReceiver mHomeKeyReceiver = null;

    //注册广播接收者，监听Home键
    private void registerHomeKeyReceiver(Context context) {
        Log.i("LOG_TAG", "registerHomeKeyReceiver");
        mHomeKeyReceiver = new HomeWatcherReceiver(homePressHandler);
        IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.registerReceiver(mHomeKeyReceiver, homeFilter);
    }

    //取消监听广播接收者
    private void unregisterHomeKeyReceiver(Context context) {
        Log.i("woaini", "unregisterHomeKeyReceiver");
        if (null != mHomeKeyReceiver) {
            context.unregisterReceiver(mHomeKeyReceiver);
        }
    }



    @Override
    public void onBackPressed() {
        String text = "";
        if(tab02.isStudy)
            text = "确认放弃静学吗？";
        else
            text = "确认退出吗？";
        new AlertDialog.Builder(this)
                .setTitle(text)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterHomeKeyReceiver(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_main);
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        registerHomeKeyReceiver(this);
        initView();



        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        {

            @Override
            public int getCount()
            {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int arg0)
            {
                return mFragments.get(arg0);
            }
        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {

            private int currentIndex;

            @Override
            public void onPageSelected(int position)
            {
                resetTabBtn();
                switch (position)
                {
                    case 0:
                            rg_group.setBackgroundColor(Color.parseColor("#ffe471"));
                        break;
                    case 1:
                            rg_group.setBackgroundColor(Color.parseColor("#34b78a"));
                        break;
                    case 2:
                            rg_group.setBackgroundColor(Color.parseColor("#34b78a"));
                        break;
                    case 3:
                            rg_group.setBackgroundColor(Color.parseColor("#34b78a"));
                        break;
                }

                currentIndex = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {

            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {
        }
        });

        }

protected void resetTabBtn()
        {
//        ((ImageButton) mTabBtnHome.findViewById(R.id.btn_tab_bottom_weixin))
//                .setImageResource(R.drawable.tab_weixin_normal);
//        ((ImageButton) mTabBtnJX.findViewById(R.id.btn_tab_bottom_friend))
//                .setImageResource(R.drawable.tab_find_frd_normal);
//        ((ImageButton) mTabBtnFirend.findViewById(R.id.btn_tab_bottom_contact))
//                .setImageResource(R.drawable.tab_address_normal);
//        ((ImageButton) mTabBtnProfile.findViewById(R.id.btn_tab_bottom_setting))
//                .setImageResource(R.drawable.tab_settings_normal);
        }

private void initView()
        {
        rg_group = findViewById(R.id.rg_group);

        findViewById(R.id.rb_home).setOnClickListener(this);
        findViewById(R.id.rb_JX).setOnClickListener(this);
        findViewById(R.id.rb_friend).setOnClickListener(this);
        findViewById(R.id.rb_profile).setOnClickListener(this);


        tab01 = new MainTab01();
        tab02 = new MainTab02();
        tab03 = new MainTab03();
        tab04 = new MainTab04();
        mFragments.add(tab01);
        mFragments.add(tab02);
        mFragments.add(tab03);
        mFragments.add(tab04);


        }

@Override
public void onClick(View v) {
        switch (v.getId()){
        case R.id.rb_home:
        mViewPager.setCurrentItem(0);
        break;
        case R.id.rb_JX:
        mViewPager.setCurrentItem(1);
        break;
        case R.id.rb_friend:
        mViewPager.setCurrentItem(2);
        break;
        case R.id.rb_profile:
        mViewPager.setCurrentItem(3);
        break;
        }

        }
        }