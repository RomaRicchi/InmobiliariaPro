package com.roma.inmobiliariapro.ui.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.roma.inmobiliariapro.MainActivity;
import com.roma.inmobiliariapro.R;
import com.roma.inmobiliariapro.databinding.ActivityLoginBinding;
import com.roma.inmobiliariapro.utils.MessageManager;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;
    public static final int REQUEST_CALL_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Forzar modo claro desde el inicio
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        setupListeners();

        loginViewModel.getIsLoading().observe(this, this::handleLoading);
        loginViewModel.getLoginSuccess().observe(this, this::handleLoginSuccess);
        loginViewModel.getShakeDetected().observe(this, this::handleShake);
        
        // Usamos MessageManager para todos los mensajes (errores de API, éxito, etc.)
        MessageManager.getUiMessageMutable().observe(this, uiMessage -> {
            if (uiMessage != null) {
                Toast.makeText(this, uiMessage.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            Log.d("PRUEBA", "CLICK OLVIDE CLAVE");
            String usuario = binding.etUsuario.getText().toString().trim();
            String clave = binding.etClave.getText().toString().trim();
            loginViewModel.login(usuario, clave);
        });

        binding.tvOlvideClave.setOnClickListener(v -> {
            String usuario = binding.etUsuario.getText().toString().trim();
            loginViewModel.resetearContrasena(usuario);
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
        binding.tvOlvideClave.setEnabled(!isLoading);
    }

    private void handleLoginSuccess(Boolean success) {
        if (Boolean.TRUE.equals(success)) {
            Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show();
            
            // Resetear estado para permitir futuros inicios de sesión si se vuelve a esta pantalla
            loginViewModel.resetLoginState();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
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
