package com.roma.inmobiliariapro.ui.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.roma.inmobiliariapro.R;
import com.roma.inmobiliariapro.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements SensorEventListener {
    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;

    // Sensor para agitar
    private SensorManager sensorManager;
    private float acceleration;
    private float currentAcceleration;
    private float lastAcceleration;
    private static final int SHAKE_THRESHOLD = 12;
    private static final int REQUEST_CALL_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        setupListeners();
        setupSensors();

        loginViewModel.getIsLoading().observe(this, boo -> {
            if(!boo) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnLogin.setEnabled(true);
            }
        });
    }

    private void setupListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            String usuario = binding.etUsuario.getText().toString().trim();
            String clave = binding.etClave.getText().toString().trim();

            if (usuario.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                performLogin(usuario, clave);
            }
        });
    }

    private void setupSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        acceleration = 10f;
        currentAcceleration = SensorManager.GRAVITY_EARTH;
        lastAcceleration = SensorManager.GRAVITY_EARTH;
    }

    private void performLogin(String usuario, String clave) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnLogin.setEnabled(false);

        loginViewModel.login(usuario, clave);

//        loginViewModel.login(usuario, clave).observe(this, token -> {
//            binding.progressBar.setVisibility(View.GONE);
//            binding.btnLogin.setEnabled(true);
//
//            if (token != null) {
//                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(this, MainActivity.class));
//                finish();
//            } else {
//                Toast.makeText(this, "Error de autenticación", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        lastAcceleration = currentAcceleration;
        currentAcceleration = (float) Math.sqrt(x * x + y * y + z * z);
        float delta = currentAcceleration - lastAcceleration;
        acceleration = acceleration * 0.9f + delta;

        if (acceleration > SHAKE_THRESHOLD) {
            makeCall();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private void makeCall() {
        String phoneNumber = getString(R.string.inmobiliaria_telefono);
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
            return;
        }
        startActivity(callIntent);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }
}
