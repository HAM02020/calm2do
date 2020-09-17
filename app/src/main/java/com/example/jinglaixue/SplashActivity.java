package com.example.jinglaixue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

public class SplashActivity extends AppCompatActivity {
    private RelativeLayout rlSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        rlSplash = (RelativeLayout) findViewById(R.id.rl_splash);

        startAnim();
    }

    /**
     * 启动动画
     */
    private void startAnim() {
        // 渐变动画,从完全透明到完全不透明
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        // 持续时间 2 秒
        alpha.setDuration(2000);
        // 动画结束后，保持动画状态
        alpha.setFillAfter(true);

        // 设置动画监听器
        alpha.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            // 动画结束时回调此方法
            @Override
            public void onAnimationEnd(Animation animation) {
                // 跳转到下一个页面
                jumpNextPage();
            }
        });

        // 启动动画
        rlSplash.startAnimation(alpha);
    }

    /**
     * 跳转到下一个页面
     */
    private void jumpNextPage() {
//        startActivity(new Intent(SplashActivity.this, GuideActivity.class));
//        finish();

        // 判断之前有没有展示过功能引导
//        boolean guideShowed = PrefUtils.getBoolean(SplashActivity.this,
//                PrefUtils.GUIDE_SHOWED, false);
        boolean isFirstIn = false;
        final SharedPreferences sharedPreferences = getSharedPreferences("is_first_in_data",MODE_PRIVATE);
        isFirstIn = sharedPreferences.getBoolean("isFirstIn",true);

        if (isFirstIn) {
            // 跳转到功能引导页
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
        } else {
            // 跳转到主页面
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }

        finish();
    }
}
