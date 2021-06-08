package com.ronadigitech.attendance.Activities;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ronadigitech.attendance.Configs.Config;
import com.ronadigitech.attendance.Helpers.prefHelper;
import com.ronadigitech.attendance.Models.User;
import com.ronadigitech.attendance.R;
import com.ronadigitech.attendance.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import android.app.DatePickerDialog;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private User user;
    SimpleDateFormat tglFormatter, waktuFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefHelper.init(MainActivity.this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        user = prefHelper.getUser(prefHelper.getLastUser());

        if(user.getLast_check().equals("in")) {
            binding.txtScanAbsensi.setText("Absen Keluar");
        }
        else {
            binding.txtScanAbsensi.setText("Absen Masuk");
        }

        binding.txtUserName.setText(user.getNama());
        if(!user.getProfile_picture().equals("")) {
            Glide.with(MainActivity.this)
                    .load(Config.generateUrl(user.getProfile_picture()))
                    .into(binding.imgUserProfilePhoto);
        }

        Calendar c = Calendar.getInstance();
        tglFormatter = new SimpleDateFormat("dd-MM-YYYY", Locale.US);
        String formattedDate = tglFormatter.format(c.getTime());
        Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
        binding.txtTanggal.setText(formattedDate);

        waktuFormatter = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = waktuFormatter.format(c.getTime());
        Toast.makeText(this, formattedTime, Toast.LENGTH_SHORT).show();
        binding.txtWaktu.setText(formattedTime);

//        binding.cardUserInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
//                startActivity(i);
//
//                Log.d("[DEBUG]", "Click card");
//            }
//        });
        binding.imgUserProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);

                Log.d("[DEBUG]", "Click profile photo");
            }
        });
        binding.linearCardUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);

                Log.d("[DEBUG]", "Click linear card");
            }
        });

        binding.linearBtnAbsensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AttendanceActivity.class);
                startActivity(i);

                Log.d("[DEBUG]", "Click linear absen");
            }
        });
        binding.imgAbsensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AttendanceActivity.class);
                startActivity(i);

                Log.d("[DEBUG]", "Click fab absen");
            }
        });

        binding.linearBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefHelper.setSessionData(prefHelper.IS_ACTIVE, "not active", user.getId());
                Toasty.success(MainActivity.this, "Anda berhasil keluar dari aplikasi..", Toasty.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
            }
        });
        binding.imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefHelper.setSessionData(prefHelper.IS_ACTIVE, "not active", user.getId());
                Toasty.success(MainActivity.this, "Anda berhasil keluar dari aplikasi..", Toasty.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
            }
        });

        binding.linearBtnRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(i);
            }
        });
        binding.riwayatAbsensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(i);
            }
        });

        binding.linearProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        binding.imageProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        binding.linearSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });

        binding.imageSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });

        setContentView(binding.getRoot());
    }
}