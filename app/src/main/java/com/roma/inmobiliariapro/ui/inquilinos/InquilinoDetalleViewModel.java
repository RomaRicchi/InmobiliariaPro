package com.roma.inmobiliariapro.ui.inquilinos;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.roma.inmobiliariapro.data.api.ApiService;
import com.roma.inmobiliariapro.data.api.RetrofitClient;
import com.roma.inmobiliariapro.data.model.response.Contrato;
import com.roma.inmobiliariapro.data.model.response.Inmueble;
import com.roma.inmobiliariapro.utils.MessageManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquilinoDetalleViewModel extends AndroidViewModel {
    private final ApiService apiService;
    private MutableLiveData<Contrato> contratoMutable = new MutableLiveData<>();
    public InquilinoDetalleViewModel(@NonNull Application application) {
        super(application);
        apiService = RetrofitClient.getService(application);
    }

    public LiveData<Contrato> getContratoMutable() {
        return contratoMutable;
    }

    public void getContrato(Inmueble inmueble) {
        Call<Contrato> call = apiService.obtenerContratoPorInmueble(inmueble.getId());
        call.enqueue(new Callback<Contrato>() {
            @Override
            public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                if(response.isSuccessful() && response.body() != null) {
                    contratoMutable.postValue(response.body());
                } else {
                    Log.d("API - CONTRATOINQUILINO", "Error en la respuesta del servidor: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Contrato> call, Throwable throwable) {
                MessageManager.sendMsgResponse(0, "ContratoInquilino");
                Log.e("API - CONTRATOINQUILINO", throwable.getMessage(), throwable);
            }
        });
    }
}
