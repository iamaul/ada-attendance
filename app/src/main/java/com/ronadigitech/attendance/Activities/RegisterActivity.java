package com.ronadigitech.attendance.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ronadigitech.attendance.Helpers.prefHelper;
import com.ronadigitech.attendance.Models.User;
import com.ronadigitech.attendance.R;
import com.ronadigitech.attendance.databinding.ActivityRegisterBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private int selectedGender;
    private String selectedDate, SelectedStartDate;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());

        binding.txtPageTitle.setText("Pendaftaran Akun");
        binding.imgBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new ArrayAdapter<String>(
                this,
                R.layout.list_item,
                getResources().getStringArray(R.array.user_gender));
        binding.spnGenderType.setAdapter(adapter);
        binding.spnGenderType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = position;
                Log.d("[DEBUG]", "Selected: " + parent.getItemAtPosition(position).toString() + "(" + position + ")");
            }
        });

        binding.inputBornDate.getEditText().setInputType(InputType.TYPE_NULL);
        binding.inputBornDate.getEditText().setKeyListener(null);
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Pilih tanggal lahir");
        final MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();
        binding.inputBornDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("[DEBUG]", "Show datepicker!");
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        Date date = new Date(selection);

                        Log.d("[DEBUG]", simpleFormat.format(date));
                        binding.inputBornDate.getEditText().setText(simpleFormat.format(date));
                        binding.inputBornDate.getEditText().setFocusable(false);
                        selectedDate = simpleFormat.format(date);
                    }
                });

        binding.btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = binding.inputFullname.getEditText().getText().toString().trim();
                String phone = binding.inputPhoneNumber.getEditText().getText().toString().trim();
                String password = binding.inputPassword.getEditText().getText().toString().trim();
                String passwordRepeat = binding.inputPasswordRepeat.getEditText().getText().toString().trim();
                String email = binding.inputEmail.getEditText().getText().toString().trim();
                String alamat = binding.inputAddress.getEditText().getText().toString().trim();

                boolean passwordPass = false;

                if(fullname.length() > 0 && phone.length() > 0 && selectedDate.length() > 0 && selectedGender >= 0 && password.length() > 0 && passwordRepeat.length() > 0) {
                    if(password.equals(passwordRepeat)) {
                        passwordPass = true;
                    }
                    else {
                        Toasty.error(RegisterActivity.this, "Password tidak cocok!", Toasty.LENGTH_LONG).show();
                    }

                    if(passwordPass) {
                        registerAccount(fullname, email, alamat, phone, password, selectedDate, selectedGender);
                    }
                }
                else {
                    Toasty.error(RegisterActivity.this, "Harap isi semua field!", Toasty.LENGTH_LONG).show();
                }
            }
        });

        setContentView(binding.getRoot());
    }

    private void registerAccount(final String fullname, String email, String alamat, String phone, String password, String selectedDate, final int selectedGender) {
        final ProgressDialog dialog = ProgressDialog.show(RegisterActivity.this,"",
                "Mencoba mendaftarkan akun, harap tunggu...", true);

        User user = new User();

        user.setNama(fullname);
        user.setEmail(email);
        user.setAlamat(alamat);
        user.setNo_tlp(phone);
        user.setPassword(password);
        user.setDob(selectedDate);
        user.setJenis_kelamin(String.valueOf(selectedGender));
        user.setLevel("0");
        user.setLast_check("out");
        user.setProfile_picture("");

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        dialog.dismiss();
                        Toasty.success(RegisterActivity.this, "Berhasil mendaftarkan akun baru..", Toasty.LENGTH_LONG).show();
                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(i);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("[DEBUG]", "Error adding document", e);
                        dialog.dismiss();
                        Toasty.error(RegisterActivity.this, "Gagal melakukan pendaftaran akun, silahkan hubungi tim teknis!", Toast.LENGTH_SHORT, true).show();
                    }
                });
    }
}
