package com.roma.inmobiliariapro;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.roma.inmobiliariapro.data.api.ApiService;
import com.roma.inmobiliariapro.data.api.RetrofitClient;
import com.roma.inmobiliariapro.data.model.response.Propietario;
import com.roma.inmobiliariapro.utils.MessageManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {
    private final ApiService apiService;
    private final MutableLiveData<Propietario> propietarioMutable = new MutableLiveData<>();
    public MainViewModel(@NonNull Application application) {
        super(application);
        apiService = RetrofitClient.getService(application);
    }

    public LiveData<Propietario> getPropietarioMutable() {
        return propietarioMutable;
    }

    public void getPropietario() {
        Call<Propietario> call = apiService.obtenerPerfil();
        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if(response.isSuccessful() && response.body() != null) {
                    propietarioMutable.postValue(response.body());
                }else {
                    Log.e("API - PROPIETARIO", "Error en la respuesta del servidor: " + response.code());
                    MessageManager.sendMsgResponse(response.code(), "Propietario");
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable throwable) {
                MessageManager.sendMsgResponse(0, "Propietario");
                Log.e("API - PROPIETARIO", throwable.getMessage(), throwable);
            }
        });
    }
}
