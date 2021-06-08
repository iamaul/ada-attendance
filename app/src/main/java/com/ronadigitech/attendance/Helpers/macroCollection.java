package com.ronadigitech.attendance.Helpers;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class macroCollection {
    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            Log.d("[DEBUG]", "is directory: " + fileOrDirectory.toString());
            for (File child : fileOrDirectory.listFiles()) {
                Log.d("[DEBUG]", "is file: " + child.toString());
                deleteRecursive(child);
            }
        }
        else {
            fileOrDirectory.delete();
        }
    }
}
