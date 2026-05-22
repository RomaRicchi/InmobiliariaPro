package com.roma.inmobiliariapro.ui.inmuebles;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.roma.inmobiliariapro.data.api.ApiService;
import com.roma.inmobiliariapro.data.api.RetrofitClient;
import com.roma.inmobiliariapro.data.model.Status;
import com.roma.inmobiliariapro.data.model.UiMessage;
import com.roma.inmobiliariapro.data.model.response.Inmueble;
import com.roma.inmobiliariapro.utils.FieldValidation;
import com.roma.inmobiliariapro.utils.MessageManager;
import com.roma.inmobiliariapro.utils.SafeExecutor;
import com.roma.inmobiliariapro.utils.SharedPreferesManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleViewModel extends AndroidViewModel {
    private final ApiService apiService;
    private final MutableLiveData<List<Inmueble>> inmueblesMutable = new MutableLiveData<>();
    private final MutableLiveData<List<Inmueble>> inmueblesAlquiladosMutable = new MutableLiveData<>();
    public InmuebleViewModel(@NonNull Application application) {
        super(application);
        apiService = RetrofitClient.getService(application);
    }

    public LiveData<List<Inmueble>> getInmueblesMutable() { return inmueblesMutable; }
    public LiveData<List<Inmueble>> getInmueblesAlquiladosMutable() { return inmueblesAlquiladosMutable; }

    public void getInmuebles() {
        Call<List<Inmueble>> call = apiService.obtenerInmuebles();
        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    inmueblesMutable.postValue(response.body());
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
