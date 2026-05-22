package com.roma.inmobiliariapro.ui.contratos;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.roma.inmobiliariapro.data.api.ApiService;
import com.roma.inmobiliariapro.data.api.RetrofitClient;
import com.roma.inmobiliariapro.data.model.response.Contrato;
import com.roma.inmobiliariapro.data.model.response.Pago;
import com.roma.inmobiliariapro.utils.MessageManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagoViewModel extends AndroidViewModel {
    private final ApiService apiService;
    private final MutableLiveData<List<Pago>> pagosMutable = new MutableLiveData<>();
    public PagoViewModel(@NonNull Application application) {
        super(application);
        apiService = RetrofitClient.getService(application);
    }

    public LiveData<List<Pago>> getPagosMutable() {
        return pagosMutable;
    }

    public void getPagos(Contrato contrato) {
        Call<List<Pago>> call = apiService.obtenerPagosPorContrato(contrato.getId());
        call.enqueue(new Callback<List<Pago>>() {
            @Override
            public void onResponse(Call<List<Pago>> call, Response<List<Pago>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    pagosMutable.postValue(response.body());
                } else {
                    Log.e("API - CONTRATOINQUILINO", "Error en la respuesta del servidor: " + response.code());
                    MessageManager.sendMsgResponse(response.code(), "ContratoInquilino");
                }
            }

            @Override
            public void onFailure(Call<List<Pago>> call, Throwable throwable) {
                MessageManager.sendMsgResponse(0, "ContratoInquilino");
                Log.e("API - CONTRATOINQUILINO", throwable.getMessage(), throwable);
            }
        });
    }
}
