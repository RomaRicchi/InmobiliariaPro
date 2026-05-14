package com.roma.inmobiliariapro.ui.login;

import static android.content.Context.SENSOR_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import static androidx.core.content.ContextCompat.startActivity;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.roma.inmobiliariapro.MainActivity;
import com.roma.inmobiliariapro.R;
import com.roma.inmobiliariapro.data.api.ApiService;
import com.roma.inmobiliariapro.data.api.RetrofitClient;
import com.roma.inmobiliariapro.utils.SharedPreferesManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private ApiService apiService;
    private SharedPreferesManager sharedPreferesManager;

    // Sensor para agitar
    private final MutableLiveData<Boolean> shakeDetected =
            new MutableLiveData<>();
    private SensorManager sensorManager;
    private float acceleration;
    private float currentAcceleration;
    private float lastAcceleration;
    public static final int SHAKE_THRESHOLD = 12;

    public LoginViewModel(@NonNull Application application) {
        super(application);
       sharedPreferesManager = new SharedPreferesManager(application);
       apiService = RetrofitClient.getService(sharedPreferesManager);
       sensorManager = (SensorManager) application.getSystemService(SENSOR_SERVICE);
       acceleration = 10f;
       currentAcceleration = SensorManager.GRAVITY_EARTH;
       lastAcceleration = SensorManager.GRAVITY_EARTH;
    }

    public void login(String usuario, String clave) {
        if (usuario.isEmpty() || clave.isEmpty()) {
            errorMessage.setValue("Complete todos los campos");
            return;
        }

        isLoading.setValue(true);

        Call<String> call = apiService.login(usuario, clave);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful() && response.body() != null) {
                    String token = response.body();
                    sharedPreferesManager.saveToken(token);
                    //msg de bienvenida
                    isLoading.setValue(false);
                    loginSuccess.setValue(true);
                    Intent itt = new Intent(getApplication(), MainActivity.class);
                    itt.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    getApplication().startActivity(itt);
                } else {
                    errorMessage.setValue("Usuario o contraseña incorrectos");
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                isLoading.setValue(false);
                errorMessage.setValue("Error de conexión");
            }
        });
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getShakeDetected() {
        return shakeDetected;
    }

    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }
    
    public void setLoading(boolean loading) {
        isLoading.setValue(loading);
    }



    private final SensorEventListener sensorListener = new SensorEventListener() {
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
                    shakeDetected.setValue(true);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
            };

    public void startSensor() {
        List<Sensor> sensores = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (!sensores.isEmpty()) {
            sensorManager.registerListener(sensorListener, sensores.get(0), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void stopSensor() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(sensorListener);
        }
    }
}
