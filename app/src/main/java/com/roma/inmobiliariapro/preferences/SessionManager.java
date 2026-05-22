package com.roma.inmobiliariapro.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SessionManager {
    private static SessionManager instance;
    private final MutableLiveData<Boolean> sessionExpired = new MutableLiveData<>();
    private SharedPreferences prefs;

    private SessionManager(Context context) {
        prefs = context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE);
    }

    public static synchronized SessionManager getInstance(Context context) {
        if(instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }

        return instance;
    }

    public void saveToken(String token) {
        prefs.edit().putString("token", token).apply();
    }

    public String getToken() {
        return prefs.getString("token", null);
    }

    public LiveData<Boolean> getSessionExpired() {
        return sessionExpired;
    }

    public void logout() {
        prefs.edit().clear().apply();
        sessionExpired.postValue(true);
    }

    public void clearSession() {
        prefs.edit().clear().apply();
    }
}
