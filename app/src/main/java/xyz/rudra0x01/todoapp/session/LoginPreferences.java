package xyz.rudra0x01.todoapp.session;

import android.content.Context;
import android.content.SharedPreferences;
import xyz.rudra0x01.todoapp.DashboardActivity;

import static xyz.rudra0x01.todoapp.DashboardActivity.USERNAME_KEY;

public class LoginPreferences {
    private static final String PREF_NAME = "login_preferences";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EXPIRATION_TIME = "expiration_time";

    private static SharedPreferences sharedPreferences;

    public LoginPreferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setUsername(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    public void setExpirationTime(long expirationTime) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_EXPIRATION_TIME, expirationTime);
        editor.apply();
    }

    public long getExpirationTime() {
        return sharedPreferences.getLong(KEY_EXPIRATION_TIME, 0);
    }

    public boolean isLoggedIn() {
        long expirationTime = getExpirationTime();
        long currentTime = System.currentTimeMillis();
        if (expirationTime < currentTime) {
            // session has expired, clear the username and expiration time
            setUsername(null);
            setExpirationTime(0);
            return false;
        }
        return getUsername() != null && expirationTime > currentTime;
    }

    public static void logout(DashboardActivity dashboardActivity) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USERNAME_KEY);
        editor.apply();
    }
}