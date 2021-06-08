package com.ronadigitech.attendance.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.SiliCompressor;
import com.ronadigitech.attendance.Helpers.macroCollection;
import com.ronadigitech.attendance.Helpers.prefHelper;
import com.ronadigitech.attendance.Models.Attendance;
import com.ronadigitech.attendance.Models.User;
import com.ronadigitech.attendance.databinding.ActivityAttendanceBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import es.dmoral.toasty.Toasty;

public class AttendanceActivity extends AppCompatActivity {

    private ActivityAttendanceBinding binding;
    private File fileDirectory;
    private String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.ronadigitech.attendance/files";

    private File androidXpath;

    private Boolean isButtonPressed = false;

    private int photo_taking;
    private int photo_count = 0;
    private Boolean[] photo_taken = {false, false, false, false};
    private int[] photo_files = {0, 0, 0, 0};
    private String[] photo_url = {null, null, null, null};

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://simple-attendance-b39b5.appspot.com/images");

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAttendanceBinding.inflate(getLayoutInflater());

        prefHelper.init(AttendanceActivity.this);

        fileDirectory = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Attendance");
        Log.d("[DEBUG]", fileDirectory.toString());
        fileDirectory.mkdirs();

        binding.txtPageTitle.setText("Ambil Foto");
        binding.imgBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.imgTakePhotoButtonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo_taking = 1;
                Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File imageFile;
                if (photo_taken[photo_taking-1]) {
                    Log.d("[DEBUG]", "true using file " + photo_files[photo_taking-1] + " - " + photo_taken[photo_taking-1]);
                    imageFile = new File(fileDirectory, "/temp" + (photo_files[photo_taking-1]) + ".jpg");
                }
                else {
                    Log.d("[DEBUG]", "false using new " + photo_taken[photo_taking-1]);
                    imageFile = new File(fileDirectory, "/temp" + (photo_count + 1) + ".jpg");
                }
                Uri imageUri = FileProvider.getUriForFile(
                        AttendanceActivity.this,
                        "com.ronadigitech.attendance.provider",
                        imageFile);
                camIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(camIntent, 0);
            }
        });
        binding.imgTakePhotoButtonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo_taking = 2;
                Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File imageFile;
                if (photo_taken[photo_taking-1]) {
                    Log.d("[DEBUG]", "true using file " + photo_files[photo_taking-1] + " - " + photo_taken[photo_taking-1]);
                    imageFile = new File(fileDirectory, "/temp" + (photo_files[photo_taking-1]) + ".jpg");
                }
                else {
                    Log.d("[DEBUG]", "false using new " + photo_taken[photo_taking-1]);
                    imageFile = new File(fileDirectory, "/temp" + (photo_count + 1) + ".jpg");
                }
                Uri imageUri = FileProvider.getUriForFile(
                        AttendanceActivity.this,
                        "com.ronadigitech.attendance.provider",
                        imageFile);
                camIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(camIntent, 0);
            }
        });
        binding.imgTakePhotoButtonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo_taking = 3;
                Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File imageFile;
                if (photo_taken[photo_taking-1]) {
                    Log.d("[DEBUG]", "true using file " + photo_files[photo_taking-1] + " - " + photo_taken[photo_taking-1]);
                    imageFile = new File(fileDirectory, "/temp" + (photo_files[photo_taking-1]) + ".jpg");
                }
                else {
                    Log.d("[DEBUG]", "false using new " + photo_taken[photo_taking-1]);
                    imageFile = new File(fileDirectory, "/temp" + (photo_count + 1) + ".jpg");
                }
                Uri imageUri = FileProvider.getUriForFile(
                        AttendanceActivity.this,
                        "com.ronadigitech.attendance.provider",
                        imageFile);
                camIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(camIntent, 0);
            }
        });
        binding.imgTakePhotoButtonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo_taking = 4;
                Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File imageFile;
                if (photo_taken[photo_taking-1]) {
                    Log.d("[DEBUG]", "true using file " + photo_files[photo_taking-1] + " - " + photo_taken[photo_taking-1]);
                    imageFile = new File(fileDirectory, "/temp" + (photo_files[photo_taking-1]) + ".jpg");
                }
                else {
                    Log.d("[DEBUG]", "false using new " + photo_taken[photo_taking-1]);
                    imageFile = new File(fileDirectory, "/temp" + (photo_count + 1) + ".jpg");
                }
                Uri imageUri = FileProvider.getUriForFile(
                        AttendanceActivity.this,
                        "com.ronadigitech.attendance.provider",
                        imageFile);
                camIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(camIntent, 0);
            }
        });

        binding.btnAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isButtonPressed) {
                    isButtonPressed = true;
                    if (photo_count >= 1) {
                        Attendance attendance = new Attendance();

                        final ProgressDialog dialog = ProgressDialog.show(AttendanceActivity.this, "",
                                "Memuat, harap tunggu...", true);

                        File sendedFiles;
                        for (int i = 1; i <= photo_count; i++) {
                            File imageFile = new File(fileDirectory, "/temp" + i + ".jpg");
                            Uri imageUri = FileProvider.getUriForFile(
                                    AttendanceActivity.this,
                                    "com.ronadigitech.attendance.provider",
                                    imageFile);
                            String newFilePath = SiliCompressor.with(AttendanceActivity.this).compress(imageUri.toString(), new File(fileDirectory.toString()));

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                androidXpath = new File(getRealPathFromUri(AttendanceActivity.this, Uri.parse(newFilePath)));
                                sendedFiles = new File(getRealPathFromUri(AttendanceActivity.this, Uri.parse(newFilePath)));
                            }
                            else {
                                sendedFiles = new File(newFilePath);
                            }

                            UUID uuid = UUID.randomUUID();
                            String uuidAsString = uuid.toString();

                            photo_url[i-1] = uuidAsString;

                            StorageReference childRef = storageRef.child(uuidAsString);
                            Uri file = Uri.fromFile(sendedFiles);

                            int index = i;
                            Log.d("[DEBUG]", "Index: " + index);
                            childRef.putFile(file)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Log.d("[DEBUG]", uri.toString());
                                                }
                                            });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            dialog.dismiss();
                                            Log.d("[DEBUG]", exception.getMessage());
                                            Toasty.error(AttendanceActivity.this, exception.getMessage(), Toast.LENGTH_LONG, true).show();
                                        }
                                    });
                        }

                        User user = prefHelper.getUser(prefHelper.getLastUser());

                        String attendanceType;
                        if(user.getLast_check().equals("in")) {
                            attendanceType = "out";
                        }
                        else {
                            attendanceType = "in";
                        }

                        user.setLast_check(attendanceType);
                        prefHelper.setSessionData(prefHelper.LAST_CHECK, attendanceType, user.getId());

                        Log.d("[DEBUG]", photo_url.toString());

                        attendance.setAttendance_photo_1(photo_url[0]);
                        attendance.setAttendance_photo_2(photo_url[1]);
                        attendance.setAttendance_photo_3(photo_url[2]);
                        attendance.setAttendance_photo_4(photo_url[3]);
                        attendance.setAttendance_user(user.getId());
                        attendance.setAttendance_type(attendanceType);

                        db.collection("attendance")
                                .add(attendance)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        dialog.dismiss();
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                            String[] splitted = androidXpath.toString().split("/");
                                            String[] folderTargetRaw = Arrays.copyOf(splitted, splitted.length - 1);
                                            String folderTarget = TextUtils.join("/", folderTargetRaw);
                                            macroCollection.deleteRecursive(new File(folderTarget));
                                        }
                                        macroCollection.deleteRecursive(new File(fileDirectory.toString()));

                                        db.collection("users")
                                                .document(user.getId())
                                                .set(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toasty.success(AttendanceActivity.this, "Semua data berhasil dikirim ke server, terimakasih telah melakukan absensi..", Toasty.LENGTH_LONG).show();
                                                        Intent i = new Intent(AttendanceActivity.this, MainActivity.class);
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
                                                        Toasty.error(AttendanceActivity.this, "Gagal melakukan absensi, silahkan hubungi tim teknis!", Toast.LENGTH_SHORT, true).show();
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("[DEBUG]", "Error adding document", e);
                                        dialog.dismiss();
                                        Toasty.error(AttendanceActivity.this, "Gagal melakukan absensi, silahkan hubungi tim teknis!", Toast.LENGTH_SHORT, true).show();
                                    }
                                });
                        isButtonPressed = false;
                    }
                    else {
                        isButtonPressed = false;
                        Toasty.error(AttendanceActivity.this, "Harap ambil minimal 1 foto sebelum meyerahkan berkas untuk absen!", Toasty.LENGTH_LONG).show();
                    }
                }
                else {
                    Toasty.info(AttendanceActivity.this, "Sedang melakukan pengiriman ke server, mohon tunggu..", Toasty.LENGTH_LONG).show();
                }
            }
        });

        setContentView(binding.getRoot());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            try {
                Bitmap cameraBitmap;
//                cameraBitmap = BitmapFactory.decodeFile(fileDirectory + "/temp" + (photo_count+1) + ".jpg");
                if (!photo_taken[photo_taking-1]) {
                    cameraBitmap = getResizedBitmap(90, 90, fileDirectory + "/temp" + (photo_count+1) + ".jpg");
                    Log.d("[DEBUG]", "not taken - /temp" + (photo_count+1) + ".jpg");
                }
                else {
                    cameraBitmap = getResizedBitmap(90, 90, fileDirectory + "/temp" + (photo_files[photo_taking-1]) + ".jpg");
                    Log.d("[DEBUG]", "taken - /temp" + (photo_files[photo_taking-1]) + ".jpg");
                }
                Bitmap.createBitmap(cameraBitmap);
                switch (photo_taking) {
                    case 1: {
                        binding.imgTakePhotoButtonOne.setImageBitmap(cameraBitmap);
                        break;
                    }
                    case 2: {
                        binding.imgTakePhotoButtonTwo.setImageBitmap(cameraBitmap);
                        break;
                    }
                    case 3: {
                        binding.imgTakePhotoButtonThree.setImageBitmap(cameraBitmap);
                        break;
                    }
                    case 4: {
                        binding.imgTakePhotoButtonFour.setImageBitmap(cameraBitmap);
                        break;
                    }
                    default: {
                        break;
                    }
                }

                if (!photo_taken[photo_taking-1]) {
                    Log.d("[DEBUG]", "Not true");
                    photo_taken[photo_taking-1] = true;
                    Log.d("[DEBUG]", "Setting files");
                    photo_files[photo_taking-1] = (photo_count+1);
                    Log.d("[DEBUG]", "After set: " + photo_files[photo_taking-1]);
                    if(photo_count < 4) {
                        Log.d("[DEBUG]", "Under 4");
                        photo_count++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap getResizedBitmap(int targetW, int targetH,  String imagePath) {

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        //inJustDecodeBounds = true <-- will not load the bitmap into memory
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        return(bitmap);
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
