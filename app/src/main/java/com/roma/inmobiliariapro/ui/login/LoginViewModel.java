package com.roma.inmobiliariapro.ui.login;

import static android.content.Context.SENSOR_SERVICE;

import android.app.Application;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.roma.inmobiliariapro.data.api.ApiService;
import com.roma.inmobiliariapro.data.api.RetrofitClient;
import com.roma.inmobiliariapro.data.model.Status;
import com.roma.inmobiliariapro.data.model.UiMessage;
import com.roma.inmobiliariapro.utils.MessageManager;
import com.roma.inmobiliariapro.utils.SharedPreferesManager;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    private static final String TAG = "LoginViewModel";
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final ApiService apiService;
    private final SharedPreferesManager sharedPreferesManager;

    // Sensor para agitar
    private final MutableLiveData<Boolean> shakeDetected = new MutableLiveData<>();
    private SensorManager sensorManager;
    private float acceleration;
    private float currentAcceleration;
    private float lastAcceleration;
    public static final int SHAKE_THRESHOLD = 12;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        sharedPreferesManager = SharedPreferesManager.getInstance(application);
        apiService = RetrofitClient.getService(application);
        sensorManager = (SensorManager) application.getSystemService(SENSOR_SERVICE);
        acceleration = 10f;
        currentAcceleration = SensorManager.GRAVITY_EARTH;
        lastAcceleration = SensorManager.GRAVITY_EARTH;
    }

    public void login(String usuario, String clave) {
        if (usuario.isEmpty() || clave.isEmpty()) {
            errorMessage.setValue("Usuario y contraseña son obligatorios.");
            MessageManager.send(new UiMessage("Login", "Usuario y contraseña son obligatorios.", Status.WARNING));
            return;
        }

        isLoading.setValue(true);
        Call<String> call = apiService.login(usuario, clave);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                isLoading.postValue(false);

                Log.d(TAG, "LOGIN HTTP = " + response.code());
                Log.d(TAG, "LOGIN SUCCESS = " + response.isSuccessful());

                if (response.body() != null) {
                    Log.d(TAG, "LOGIN BODY = " + response.body());
                }

                if(response.isSuccessful() && response.body() != null) {
                    String token = response.body();
                    sharedPreferesManager.saveToken(token);
                    loginSuccess.postValue(true);
                } else {
                    try {
                        if(response.errorBody() != null){
                            Log.e(TAG,
                                    "LOGIN ERROR BODY = "
                                            + response.errorBody().string());
                        }
                    } catch (Exception e){
                        Log.e(TAG,"Error leyendo errorBody",e);
                    }

                    String error = "Credenciales inválidas o error de servidor.";

                    errorMessage.postValue(error);
                    MessageManager.sendMsgResponse(response.code(), "Login");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG,"LOGIN FAILURE",t);
                isLoading.postValue(false);
                errorMessage.postValue("Error de conexión.");
                MessageManager.send(new UiMessage("Login", "Error de conexión.", Status.ERROR));
            }
        });
    }

    public void resetearContrasena(String email) {
        if (email.isEmpty()) {
            errorMessage.setValue("Ingrese su correo electrónico en el campo de usuario.");
            MessageManager.send(new UiMessage("Recuperación", "Ingrese su correo electrónico en el campo de usuario.", Status.WARNING));
            return;
        }

        Log.d(TAG, "Solicitando reseteo de contraseña para: " + email);
        isLoading.setValue(true);
        apiService.resetearContrasena(email).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                isLoading.postValue(false);
                if (response.isSuccessful()) {
                    String msg = response.body() != null ? response.body() : "Se ha enviado un correo para resetear su clave.";
                    Log.d(TAG, "Éxito: " + msg);
                    MessageManager.send(new UiMessage("Recuperación", msg, Status.SUCCESS));
                } else {
                    Log.e(TAG, "Error en respuesta: " + response.code());
                    String errorMsg = "No se pudo procesar la solicitud.";
                    try {
                        if (response.errorBody() != null) {
                            String serverError = response.errorBody().string();
                            if (!serverError.isEmpty()) errorMsg = serverError;
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error leyendo errorBody", e);
                    }
                    errorMessage.postValue(errorMsg);
                    MessageManager.send(new UiMessage("Recuperación", errorMsg, Status.ERROR));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                isLoading.postValue(false);
                Log.e(TAG, "Fallo de conexión: " + t.getMessage());
                errorMessage.postValue("Error de conexión.");
                MessageManager.send(new UiMessage("Recuperación", "Error de conexión.", Status.ERROR));
            }
        });
    }

    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<Boolean> getLoginSuccess() { return loginSuccess; }
    public LiveData<Boolean> getShakeDetected() { return shakeDetected; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

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
