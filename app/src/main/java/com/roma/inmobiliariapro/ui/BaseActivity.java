package com.roma.inmobiliariapro.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.roma.inmobiliariapro.data.api.RetrofitClient;
import com.roma.inmobiliariapro.ui.login.LoginActivity;
import com.roma.inmobiliariapro.utils.SharedPreferesManager;

public abstract class BaseActivity extends AppCompatActivity {
    protected SharedPreferesManager sharedPreferesManager;

    private final BroadcastReceiver unauthorizedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (RetrofitClient.ACTION_UNAUTHORIZED.equals(intent.getAction())) {
                redirectToLogin();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPreferesManager = new SharedPreferesManager(this);
        if (sharedPreferesManager.isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(RetrofitClient.ACTION_UNAUTHORIZED);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(unauthorizedReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(unauthorizedReceiver, filter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(unauthorizedReceiver);
    }

    protected void redirectToLogin() {
        sharedPreferesManager.clearSession();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
