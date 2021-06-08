package com.ronadigitech.attendance.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ronadigitech.attendance.R;
import com.ronadigitech.attendance.databinding.ActivitySplashBinding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private Animation frombottom, fromtop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());

        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        fromtop = AnimationUtils.loadAnimation(this, R.anim.fromtop);

        binding.imgFlyer.setAnimation(fromtop);
        binding.imgAppLogo.setAnimation(fromtop);
        binding.txtAppText.setAnimation(frombottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        }, 2000);

        setContentView(binding.getRoot());
    }
}
