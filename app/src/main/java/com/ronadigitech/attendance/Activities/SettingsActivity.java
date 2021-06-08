package com.ronadigitech.attendance.Activities;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ronadigitech.attendance.Helpers.prefHelper;
import com.ronadigitech.attendance.Models.User;
import com.ronadigitech.attendance.R;
import com.ronadigitech.attendance.databinding.ActivitySettingsBinding;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private User user;
    private File fileDirectory;
    TextView nama_group, nama1, nama2, nama3, nama4, ket;
    ImageView flyer;
    CardView cardSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        binding.txtPageTitle.setText("Settings");
        binding.imgBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CardView cardSettings = findViewById(R.id.card_UserHistory);
        TextView nama_group = findViewById(R.id.txt_TitleGroup);
        TextView nama1 = findViewById(R.id.txt_name1);
        TextView nama2 = findViewById(R.id.txt_name2);
        TextView nama3 = findViewById(R.id.txt_name3);
        TextView nama4 = findViewById(R.id.txt_name4);
        TextView ket = findViewById(R.id.txt_long);





        setContentView(binding.getRoot());
    }
}
