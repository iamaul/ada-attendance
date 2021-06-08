package com.ronadigitech.attendance.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.ronadigitech.attendance.Adapters.HistoryAdapter;
import com.ronadigitech.attendance.Configs.Config;
import com.ronadigitech.attendance.Helpers.prefHelper;
import com.ronadigitech.attendance.Models.Attendance;
import com.ronadigitech.attendance.Models.User;
import com.ronadigitech.attendance.databinding.ActivityHistoryBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class HistoryActivity extends AppCompatActivity {

    private ActivityHistoryBinding binding;
    private ArrayList<Attendance> attendanceArrayList = new ArrayList<Attendance>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefHelper.init(HistoryActivity.this);

        binding = ActivityHistoryBinding.inflate(getLayoutInflater());

        user = prefHelper.getUser(prefHelper.getLastUser());

        setupContent();

        binding.txtPageTitle.setText("Riwayat Aktivitas");
        binding.imgBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setContentView(binding.getRoot());
    }

    private void setupContent() {
        final ProgressDialog dialog = ProgressDialog.show(HistoryActivity.this,"",
                "Memuat, harap tunggu...", true);

        db.collection("attendance")
                .orderBy("attendance_created_at", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        dialog.dismiss();

                        if (task.isSuccessful()) {
                            if(task.getResult().isEmpty()){
                                Log.d("[DEBUG]", "kosong");
                                Toasty.error(HistoryActivity.this, "Riwayat aktivitas masih kosong!", Toast.LENGTH_SHORT, true).show();
                            }
                            else {
                                attendanceArrayList = new ArrayList<Attendance>();
                                for (DocumentSnapshot document : task.getResult()) {
                                    Attendance attendance = document.toObject(Attendance.class);
                                    attendance.setId(document.getId());
                                    if(attendance.getAttendance_user().equals(user.getId())) {
                                        attendanceArrayList.add(attendance);
                                    }
                                }

                                HistoryAdapter historyAdapter = new HistoryAdapter(HistoryActivity.this, attendanceArrayList);
                                historyAdapter.notifyDataSetChanged();
                                binding.lvHistory.setAdapter(historyAdapter);
                            }
                        } else {
                            dialog.dismiss();
                            Log.d("[DEBUG]}", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
