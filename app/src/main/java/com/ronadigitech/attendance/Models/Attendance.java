package com.ronadigitech.attendance.Models;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Attendance {
    private String id;
    private String attendance_photo_1;
    private String attendance_photo_2;
    private String attendance_photo_3;
    private String attendance_photo_4;
    private String attendance_type;
    private String attendance_user;
    @ServerTimestamp
    private Date attendance_created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttendance_photo_1() {
        return attendance_photo_1;
    }

    public void setAttendance_photo_1(String attendance_photo_1) {
        this.attendance_photo_1 = attendance_photo_1;
    }

    public String getAttendance_photo_2() {
        return attendance_photo_2;
    }

    public void setAttendance_photo_2(String attendance_photo_2) {
        this.attendance_photo_2 = attendance_photo_2;
    }

    public String getAttendance_photo_3() {
        return attendance_photo_3;
    }

    public void setAttendance_photo_3(String attendance_photo_3) {
        this.attendance_photo_3 = attendance_photo_3;
    }

    public String getAttendance_photo_4() {
        return attendance_photo_4;
    }

    public void setAttendance_photo_4(String attendance_photo_4) {
        this.attendance_photo_4 = attendance_photo_4;
    }

    public String getAttendance_type() {
        return attendance_type;
    }

    public void setAttendance_type(String attendance_type) {
        this.attendance_type = attendance_type;
    }

    public String getAttendance_user() {
        return attendance_user;
    }

    public void setAttendance_user(String attendance_user) {
        this.attendance_user = attendance_user;
    }

    public Date getAttendance_created_at() {
        return attendance_created_at;
    }

    public void setAttendance_created_at(Date attendance_created_at) {
        this.attendance_created_at = attendance_created_at;
    }
}
