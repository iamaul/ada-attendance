package com.ronadigitech.attendance.Configs;

public class Config {
    public static final String FILES_URL = "https://firebasestorage.googleapis.com/v0/b/simple-attendance-b39b5.appspot.com/o/images%2F";

    public static String generateUrl(String uuid) {
        return FILES_URL + uuid + "?alt=media";
    }
}
