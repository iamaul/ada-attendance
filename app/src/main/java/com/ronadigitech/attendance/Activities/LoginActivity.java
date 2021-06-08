package com.ronadigitech.attendance.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.ronadigitech.attendance.Helpers.prefHelper;
import com.ronadigitech.attendance.Models.User;
import com.ronadigitech.attendance.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Boolean isButtonPressed = false;

    int PERMISSION_ALL = 1;

    String[] PERMISSIONS = {
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefHelper.init(LoginActivity.this);

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        if(prefHelper.getLastUser() != null) {
            Log.d("[DEBUG]", "not null " + prefHelper.getLastUser());
            if(prefHelper.getSessionData(prefHelper.IS_ACTIVE, prefHelper.getLastUser()).equals("active")) {
                User user = prefHelper.getUser(prefHelper.getLastUser());
                String password = user.getPassword();
                String username = user.getEmail();
                login(username, password);
            }
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.inputUsername.getEditText().getText().toString().trim();
                String password = binding.inputPassword.getEditText().getText().toString().trim();

                if(username.length() > 0 && password.length() > 0) {
                    login(username, password);
                }
                else {
                    Toasty.error(LoginActivity.this, "Harap masukan email dan password!", Toasty.LENGTH_LONG).show();
                }
            }
        });

        binding.btnRegister.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        });

        setContentView(binding.getRoot());
    }

    private void login(String username, String password) {
        if(!isButtonPressed) {
            isButtonPressed = true;
            final ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "",
                    "Memuat, harap tunggu...", true);

            db.collection("users")
                    .whereEqualTo("email", username)
                    .whereEqualTo("password", password)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            isButtonPressed = false;
                            dialog.dismiss();

                            if (task.isSuccessful()) {
                                Log.d("[DEBUG]", "sukses");
                                if (task.getResult().isEmpty()) {
                                    Log.d("[DEBUG]", "kosong");
                                    Toasty.error(LoginActivity.this, "Login gagal, silahkan cek kembali identitas anda!", Toast.LENGTH_SHORT, true).show();
                                } else {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.exists()) {
                                            Log.d("[DEBUG]", document.getId() + " => " + document.getData());
                                            User user = document.toObject(User.class);
                                            Log.d("[DEBUG]", "exist");

                                            //Setup prefHelper saat baru login, assign semua var user kedalam shared preferences
                                            prefHelper.setSessionData(prefHelper.ID, document.getId(), document.getId());
                                            prefHelper.setSessionData(prefHelper.NAME, user.getNama(), document.getId());
                                            prefHelper.setSessionData(prefHelper.EMAIL, user.getEmail(), document.getId());
                                            prefHelper.setSessionData(prefHelper.PHONE, user.getNo_tlp(), document.getId());
                                            prefHelper.setSessionData(prefHelper.PHOTO, user.getProfile_picture(), document.getId());
                                            prefHelper.setSessionData(prefHelper.BIRTHDATE, user.getDob(), document.getId());
                                            prefHelper.setSessionData(prefHelper.GENDER, user.getJenis_kelamin(), document.getId());
                                            prefHelper.setSessionData(prefHelper.ADDRESS, user.getAlamat(), document.getId());
                                            prefHelper.setSessionData(prefHelper.LEVEL, user.getLevel(), document.getId());
                                            prefHelper.setSessionData(prefHelper.PASSWORD, user.getPassword(), document.getId());
                                            prefHelper.setSessionData(prefHelper.LAST_CHECK, user.getLast_check(), document.getId());

                                            prefHelper.setSessionData(prefHelper.IS_ACTIVE, "active", document.getId());
                                            prefHelper.setLastUser(document.getId());

                                            Toasty.success(LoginActivity.this, "Login success!", Toast.LENGTH_SHORT, true).show();

                                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(i);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            finish();
                                        } else {
                                            Toasty.error(LoginActivity.this, "Login gagal, silahkan cek kembali identitas anda!", Toast.LENGTH_SHORT, true).show();
                                        }
                                    }
                                }
                            } else {
                                Toasty.error(LoginActivity.this, "Login gagal dikarenakan sistem, harap hubungi tim teknis!", Toast.LENGTH_SHORT, true).show();
                                Log.d("[DEBUG]", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
