package com.roma.inmobiliariapro.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingManager {
    private SharedPreferences prefs;
    public SettingManager(Context context) {
        prefs = context.getSharedPreferences("setting_prefs", Context.MODE_PRIVATE);
    }

    public void setDarkMode(boolean enabled) {
        prefs.edit().putBoolean("dark_mode", enabled).apply();
    }

    public boolean isDarkMode() {
        return prefs.getBoolean("dark_mode", false);
    }
}
