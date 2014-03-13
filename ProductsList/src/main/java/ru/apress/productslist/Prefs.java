package ru.apress.productslist;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by shushper on 13.03.14.
 */
public class Prefs {
    private final static String PREFS_KEY_FILE_WAS_DOWNLOADED = "file_was_downloaded";
    private final static String PREFS_FILE_PATH = "file_path";

    public static SharedPreferences getPrefs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        return prefs;
    }

    public static boolean wasFileDownloaded() {
        return  getPrefs().getBoolean(PREFS_KEY_FILE_WAS_DOWNLOADED, false);
    }

    public static void setFileWasDownloaded(boolean b) {
        getPrefs().edit().putBoolean(PREFS_KEY_FILE_WAS_DOWNLOADED, b).commit();
    }

    public static String getFilePath() {
        return  getPrefs().getString(PREFS_FILE_PATH, null);
    }

    public static void setFilePath(String path) {
        getPrefs().edit().putString(PREFS_FILE_PATH, path).commit();
    }

}
