package com.ronadigitech.attendance.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.ronadigitech.attendance.Configs.Config;
import com.ronadigitech.attendance.Helpers.prefHelper;
import com.ronadigitech.attendance.Models.User;
import com.ronadigitech.attendance.databinding.ActivityHistorydetailBinding;
import com.ronadigitech.attendance.databinding.ActivityMainBinding;

import java.text.DateFormat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HistoryDetailActivity extends AppCompatActivity {

    ActivityHistorydetailBinding binding;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefHelper.init(HistoryDetailActivity.this);

        binding = ActivityHistorydetailBinding.inflate(getLayoutInflater());

        user = prefHelper.getUser(prefHelper.getLastUser());

        Intent i = getIntent();

        binding.txtPageTitle.setText("Detail Riwayat Aktivitas");
        binding.imgBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.txtTitleCard.setText("Hasil gambar pada saat " + i.getStringExtra("type"));
        binding.txtDateCard.setText(i.getStringExtra("time"));

        String pic1, pic2, pic3, pic4;
        pic1 = i.getStringExtra("pic1");
        pic2 = i.getStringExtra("pic2");
        pic3 = i.getStringExtra("pic3");
        pic4 = i.getStringExtra("pic4");

        if(pic1 != null) {
            Glide.with(HistoryDetailActivity.this)
                    .load(Config.generateUrl(pic1))
                    .into(binding.imgTakePhotoButtonOne);
        }
        else {
            binding.imgTakePhotoButtonOne.setVisibility(View.GONE);
        }

        if(pic2 != null) {
            Glide.with(HistoryDetailActivity.this)
                    .load(Config.generateUrl(pic2))
                    .into(binding.imgTakePhotoButtonTwo);
        }
        else {
            binding.imgTakePhotoButtonTwo.setVisibility(View.GONE);
        }

        if(pic3 != null) {
            Glide.with(HistoryDetailActivity.this)
                    .load(Config.generateUrl(pic3))
                    .into(binding.imgTakePhotoButtonThree);
        }
        else {
            binding.imgTakePhotoButtonThree.setVisibility(View.GONE);
        }

        if(pic4 != null) {
            Glide.with(HistoryDetailActivity.this)
                    .load(Config.generateUrl(pic4))
                    .into(binding.imgTakePhotoButtonFour);
        }
        else {
            binding.imgTakePhotoButtonFour.setVisibility(View.GONE);
        }

        setContentView(binding.getRoot());
    }
}
