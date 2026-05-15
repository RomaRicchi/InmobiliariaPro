package com.roma.inmobiliariapro.ui.login;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.roma.inmobiliariapro.MainActivity;
import com.roma.inmobiliariapro.R;
import com.roma.inmobiliariapro.databinding.ActivityLoginBinding;
import com.roma.inmobiliariapro.ui.BaseActivity;

public class LoginActivity extends BaseActivity {
    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;
    public static final int REQUEST_CALL_PERMISSION = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (sharedPreferesManager.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        setupListeners();

        loginViewModel.getIsLoading().observe(this, this::handleLoading);
        loginViewModel.getLoginSuccess().observe(this, this::handleLoginSuccess);
        loginViewModel.getShakeDetected().observe(this, this::handleShake);
        loginViewModel.getErrorMessage().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            String usuario = binding.etUsuario.getText().toString().trim();
            String clave = binding.etClave.getText().toString().trim();
            loginViewModel.login(usuario, clave);
        });
    }

    private void makeCall() {
        String phoneNumber = getString(R.string.inmobiliaria_telefono);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
            return;
        }
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall();
            } else {
                Toast.makeText(this, "Permiso de llamada denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleLoading(Boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.btnLogin.setEnabled(!isLoading);
    }

    private void handleLoginSuccess(Boolean success) {
        if (Boolean.TRUE.equals(success)) {
            Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void handleShake(Boolean detected) {
        if (Boolean.TRUE.equals(detected)) {
            makeCall();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginViewModel.startSensor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        loginViewModel.stopSensor();
    }
}
