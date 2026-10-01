package com.shareplate.myapplicationexp9;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_LOGGED_IN_USERNAME = "loggedInUsername";
    private static final String KEY_LOGGED_IN_PASSWORD = "loggedInPassword";
    private static final String KEY_LOGIN_TIME = "loginTime";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(String username, String password) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_LOGGED_IN_USERNAME, username);
        editor.putString(KEY_LOGGED_IN_PASSWORD, password);

        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        editor.putString(KEY_LOGIN_TIME, currentTime);

        editor.apply();
    }

    public void saveDraftCredentials(String username, String password) {
        editor.putString(KEY_LOGGED_IN_USERNAME, username);
        editor.putString(KEY_LOGGED_IN_PASSWORD, password);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getSavedUsername() {
        return pref.getString(KEY_LOGGED_IN_USERNAME, "");
    }

    public String getSavedPassword() {
        return pref.getString(KEY_LOGGED_IN_PASSWORD, "");
    }

    public String getLoginTime() {
        return pref.getString(KEY_LOGIN_TIME, "N/A");
    }

    public void logoutUser() {
        editor.clear();
        editor.apply();
    }
}