package com.ronadigitech.attendance.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.ronadigitech.attendance.Models.User;

public class prefHelper {
    public static SharedPreferences.Editor editor;
    public static SharedPreferences sharedPreferences;
    public Context context;

    /* INIT VAR */
    private static final String SHAREDPREFNAME = "shared_preferences";
    int PRIVATE_MODE = 0;

    /* USER VAR */
    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String PHOTO = "photo";
    public static final String BIRTHDATE = "birthdate";
    public static final String GENDER = "gender";
    public static final String ADDRESS = "address";
    public static final String LEVEL = "level";
    public static final String PASSWORD = "password";
    public static final String LAST_CHECK = "last_check";

    /*STATE VAR*/
    public static final String IS_INSIDE = "is_inside";
    public static final String LAST_USER = "last_user";
    public static final String IS_ACTIVE = "is_active";

    public prefHelper(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHAREDPREFNAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public static void init(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(SHAREDPREFNAME, Activity.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
    }

    public static void setSessionData(String key, String value, String username) {
        editor.putString(key + "_" + username, value);
        editor.commit();
        System.out.println("[DEBUG]: Inserting record to key: `" + key + "_" + username + "` with inserted value: `" + value + "`");
    }

    public static String getLastUser() {
        String value = null;

        value = sharedPreferences.getString(LAST_USER, null);
        System.out.println("[DEBUG]: Fetching record from key: `" + LAST_USER + "` with stored value: `" + value + "`");

        return value;
    }

    public static void setLastUser(String value) {
        editor.putString(LAST_USER, value);
        editor.commit();
        System.out.println("[DEBUG]: Inserting record to key: `" + LAST_USER + "` with inserted value: `" + value + "`");
    }

    public static boolean checkSession(String username) {
        if(getSessionData("secret", username) != null) {
            return true;
        }
        else {
            return false;
        }
    }

    public static User getUser(String username) {
        User user = new User();
        user.setNama(getSessionData("name", username));
        user.setPassword(getSessionData("password", username));
        user.setId(getSessionData("id", username));
        user.setEmail(getSessionData("email", username));
        user.setNo_tlp(getSessionData("phone", username));
        user.setProfile_picture(getSessionData("photo", username));
        user.setDob(getSessionData("birthdate", username));
        user.setJenis_kelamin(getSessionData("gender", username));
        user.setAlamat(getSessionData("address", username));
        user.setLevel(getSessionData("level", username));
        user.setLast_check(getSessionData("last_check", username));

        return user;
    }

    public static String getSessionData(String key, String username) {
        String value = null;

        value = sharedPreferences.getString(key + "_" + username, null);
        System.out.println("[DEBUG]: Fetching record from key: `" + key + "_" + username + "` with stored value: `" + value + "`");

        return value;
    }

    public static void flushSession() {
        editor.clear();
        editor.commit();
    }
}
