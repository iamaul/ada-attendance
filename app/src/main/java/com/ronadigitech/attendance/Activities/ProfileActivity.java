package com.ronadigitech.attendance.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.iceteck.silicompressorr.SiliCompressor;
import com.ronadigitech.attendance.Configs.Config;
import com.ronadigitech.attendance.Helpers.prefHelper;
import com.ronadigitech.attendance.Models.User;
import com.ronadigitech.attendance.R;
import com.ronadigitech.attendance.databinding.ActivityProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import es.dmoral.toasty.Toasty;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private File fileDirectory;
    private int photo_taken = 0;
    private int selectedGender;
    private String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.ronadigitech.attendance/files";
    private String selectedDate, SelectedStartDate;
    private User user;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://simple-attendance-b39b5.appspot.com/images");

    private ArrayAdapter<String> adapter;

    private String fileName = "no_image", pPath;
    private String fullUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefHelper.init(ProfileActivity.this);
        user = prefHelper.getUser(prefHelper.getLastUser());

        binding = ActivityProfileBinding.inflate(getLayoutInflater());

        fileDirectory = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Attendance");
        Log.d("[DEBUG]", fileDirectory.toString());
        fileDirectory.mkdirs();

        binding.txtPageTitle.setText("Profile User");
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

        setupContent();

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

        binding.imgTakePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 0);
            }
        });

        binding.btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = binding.inputFullname.getEditText().getText().toString().trim();
                String phone = binding.inputPhoneNumber.getEditText().getText().toString().trim();
                String password = binding.inputPassword.getEditText().getText().toString().trim();
                String passwordRepeat = binding.inputPasswordRepeat.getEditText().getText().toString().trim();
                String email = binding.inputEmail.getEditText().getText().toString().trim();
                String alamat = binding.inputAddress.getEditText().getText().toString().trim();

                boolean passwordPass = false;

                if(fullname.length() > 0 && phone.length() > 0 && selectedDate.length() > 0 && selectedGender >= 0) {
                    if(password.length() > 0 && passwordRepeat.length() > 0) {
                        if(password.equals(passwordRepeat)) {
                            passwordPass = true;
                        }
                        else {
                            Toasty.error(ProfileActivity.this, "Password tidak cocok!", Toasty.LENGTH_LONG).show();
                        }
                    }

                    if(passwordPass) {
                        updateProfile(fullname, email, alamat, phone, password, selectedDate, selectedGender);
                    }
                    else {
                        updateProfile(fullname, email, alamat, phone, null, selectedDate, selectedGender);
                    }
                }
                else {
                    Toasty.error(ProfileActivity.this, "Harap isi semua field!", Toasty.LENGTH_LONG).show();
                }
            }
        });

        setContentView(binding.getRoot());
    }

    private void setupContent() {
        binding.inputFullname.getEditText().setText(user.getNama());
        binding.inputPhoneNumber.getEditText().setText(user.getNo_tlp());
        binding.inputBornDate.getEditText().setText(user.getDob());
        binding.inputEmail.getEditText().setText(user.getEmail());
        binding.inputAddress.getEditText().setText(user.getAlamat());
//        binding.inputPassword.getEditText().setText(user.getPassword());
//        binding.inputPasswordRepeat.getEditText().setText(user.getPassword());
        String[] genderType = getResources().getStringArray(R.array.user_gender);
        binding.spnGenderType.setText(adapter.getItem(Integer.parseInt(user.getJenis_kelamin())), false);
        selectedGender = Integer.parseInt(user.getJenis_kelamin());
        selectedDate = user.getDob();
        if(!user.getProfile_picture().equals("")) {
            Glide.with(ProfileActivity.this)
                    .load(Config.generateUrl(user.getProfile_picture()))
                    .into(binding.imgTakePhotoButton);
        }
    }

    private void updateProfile(final String fullname, String email, String alamat, String phone, @Nullable String password, String selectedDate, final int selectedGender) {
        final ProgressDialog dialog = ProgressDialog.show(ProfileActivity.this,"",
                "Memuat, harap tunggu...", true);

        user.setNama(fullname);
        user.setEmail(email);
        user.setAlamat(alamat);
        user.setNo_tlp(phone);
        if(password != null) {
            user.setPassword(password);
        }
        user.setDob(selectedDate);
        user.setJenis_kelamin(String.valueOf(selectedGender));

        db.collection("users")
                .document(user.getId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        prefHelper.setSessionData(prefHelper.NAME, user.getNama(), user.getId());
                        prefHelper.setSessionData(prefHelper.EMAIL, user.getEmail(), user.getId());
                        prefHelper.setSessionData(prefHelper.PHONE, user.getNo_tlp(), user.getId());
                        prefHelper.setSessionData(prefHelper.BIRTHDATE, user.getDob(), user.getId());
                        prefHelper.setSessionData(prefHelper.GENDER, user.getJenis_kelamin(), user.getId());
                        prefHelper.setSessionData(prefHelper.ADDRESS, user.getAlamat(), user.getId());

                        if(password != null) {
                            prefHelper.setSessionData(prefHelper.PASSWORD, user.getPassword(), user.getId());
                        }

                        Toasty.success(ProfileActivity.this, "Berhasil memperbaharui data..", Toasty.LENGTH_LONG).show();
                        Intent i = new Intent(ProfileActivity.this, MainActivity.class);
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
                        Toasty.error(ProfileActivity.this, "Gagal memperbaharui data, silahkan hubungi tim teknis!", Toast.LENGTH_SHORT, true).show();
                    }
                });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = this.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            pPath = cursor.getString(columnIndex);
            cursor.close();

            String splittedFileName[] = pPath.split("/");
            fileName = splittedFileName[splittedFileName.length - 1];

            Glide.with(this)
                    .load(new File(pPath))
                    .into(binding.imgTakePhotoButton);
            Log.d("[DEBUG]", fileName);

            UUID uuid = UUID.randomUUID();
            String uuidAsString = uuid.toString();

            Log.d("[DEBUG]", "UUID: " + uuidAsString);

            final StorageReference childRef = storageRef.child(uuidAsString);
            Uri file = Uri.fromFile(new File(pPath));

            final ProgressDialog dialog = ProgressDialog.show(ProfileActivity.this, "",
                    "Updating image. Please wait...", true);

            childRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d("[DEBUG]", uri.toString());
                                    fullUrl = uri.toString();

                                    user.setProfile_picture(uuidAsString);

                                    db.collection("users")
                                            .document(user.getId())
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    prefHelper.setSessionData(prefHelper.PHOTO, uuidAsString, user.getId());
                                                    dialog.dismiss();
                                                    Toasty.success(ProfileActivity.this, "Berhasil mengubah foto profil..", Toasty.LENGTH_LONG).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("[DEBUG]", "Error adding document", e);
                                                    dialog.dismiss();
                                                    Toasty.error(ProfileActivity.this, "Gagal memperbaharui foto, silahkan hubungi tim teknis!", Toast.LENGTH_SHORT, true).show();
                                                }
                                            });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            dialog.dismiss();
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });
        }
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
