package com.roma.inmobiliariapro.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.roma.inmobiliariapro.data.api.ApiService;
import com.roma.inmobiliariapro.data.api.RetrofitClient;
import com.roma.inmobiliariapro.data.model.request.UpdatePasswordRequest;
import com.roma.inmobiliariapro.data.model.response.Propietario;
import com.roma.inmobiliariapro.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropietarioRepository {
    private ApiService apiService;

    public PropietarioRepository(SessionManager sessionManager) {
        this.apiService = RetrofitClient.getService(sessionManager);
    }

    public LiveData<Propietario> obtenerPerfil() {
        MutableLiveData<Propietario> data = new MutableLiveData<>();
        apiService.obtenerPerfil().enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<Boolean> actualizarPerfil(Propietario propietario) {
        MutableLiveData<Boolean> success = new MutableLiveData<>();
        apiService.actualizarPerfil(propietario).enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                success.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                success.setValue(false);
            }
        });
        return success;
    }

    public LiveData<Boolean> cambiarPassword(String actual, String nueva) {
        MutableLiveData<Boolean> success = new MutableLiveData<>();
        UpdatePasswordRequest request = new UpdatePasswordRequest(actual, nueva);
        apiService.cambiarContrasena(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                success.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("PropietarioRepository", "Error al cambiar la contraseña", t);
                success.setValue(false);
            }
        });
        return success;
    }
}
