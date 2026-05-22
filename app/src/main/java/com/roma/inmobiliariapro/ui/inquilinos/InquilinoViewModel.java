package com.roma.inmobiliariapro.ui.inquilinos;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.roma.inmobiliariapro.data.api.ApiService;
import com.roma.inmobiliariapro.data.api.RetrofitClient;
import com.roma.inmobiliariapro.data.model.response.Inmueble;
import com.roma.inmobiliariapro.utils.MessageManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquilinoViewModel extends AndroidViewModel {
    private final ApiService apiService;
    private final MutableLiveData<List<Inmueble>> inmueblesAlquiladosMutable = new MutableLiveData<>();
    public InquilinoViewModel(@NonNull Application application) {
        super(application);
        apiService = RetrofitClient.getService(application);
    }

    public LiveData<List<Inmueble>> getInmueblesAlquiladosMutable() { return inmueblesAlquiladosMutable; }

    public void getInmublesAlquilados() {
        Call<List<Inmueble>> call = apiService.obtenerInmueblesAlquilados();
        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    inmueblesAlquiladosMutable.postValue(response.body());
                } else {
                    Log.e("API - INMUEBLE", "Error en la respuesta del servidor: " + response.code());
                    MessageManager.sendMsgResponse(response.code(), "Inmueble");
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable throwable) {
                MessageManager.sendMsgResponse(0, "Inmueble");
                Log.e("API - INMUEBLE", throwable.getMessage(), throwable);
            }
        });
    }
}
