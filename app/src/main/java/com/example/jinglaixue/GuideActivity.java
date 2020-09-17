package com.example.jinglaixue;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现首次启动的引导页面
 */
public class GuideActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{
    private ViewPager viewPager;
    private ArrayList<View> views=new ArrayList<>();
    private ImageView[] imageViews;
    private int currentIndex;
    private Button mBtnStart;
//    private PagerTabStrip pagerTabStrip;
//    private  String[] titles={"1","2","3"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initViews();
        initPoint();
    }
    private void initViews(){
//        pagerTabStrip=findViewById(R.id.pagertab);
//        pagerTabStrip.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
//        pagerTabStrip.setTabIndicatorColor(getResources().getColor(android.R.color.holo_blue_dark));

        mBtnStart = (Button) findViewById(R.id.btn_start);
        mBtnStart.setVisibility(View.INVISIBLE);
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_start:
                        Intent intent=new Intent();
                        intent.setClass(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });


        viewPager=findViewById(R.id.viewpager);
        views.add(getLayoutInflater().inflate(R.layout.guide1,null));
        views.add(getLayoutInflater().inflate(R.layout.guide2,null));
        views.add(getLayoutInflater().inflate(R.layout.guide3,null));

        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(new MyPagerAdapter());
    }

    private void initPoint(){
        LinearLayout point_layout=findViewById(R.id.point_layout);
        imageViews =new ImageView[views.size()];
        for(int i=0;i<imageViews.length;i++) {
            imageViews[i] = (ImageView) point_layout.getChildAt(i);
        }

        currentIndex=0;
        imageViews[currentIndex].setImageResource(R.drawable.shape_dot_brown);
    }

    private void setCurrentPoint(int position){
        if(currentIndex<0||currentIndex==position||currentIndex>imageViews.length-1){
            return;
        }
        imageViews[currentIndex].setImageResource(R.drawable.shape_dot_gray);
        imageViews[position].setImageResource(R.drawable.shape_dot_brown);
        currentIndex=position;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        Toast.makeText(this,"page---"+position,Toast.LENGTH_SHORT).show();
        setCurrentPoint(position);
        if (position == 2) {
            mBtnStart.setVisibility(View.VISIBLE);
        } else {
            mBtnStart.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class MyPagerAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return views.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View v=views.get(position);
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

//        @Nullable
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return titles[position];
//        }
    }
}