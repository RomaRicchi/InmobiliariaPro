package com.roma.inmobiliariapro.ui.viewsModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.roma.inmobiliariapro.data.api.ApiService;
import com.roma.inmobiliariapro.data.api.RetrofitClient;
import com.roma.inmobiliariapro.data.model.response.Contrato;
import com.roma.inmobiliariapro.data.model.response.Inmueble;
import com.roma.inmobiliariapro.data.model.response.Pago;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContratoInquilinoViewModel extends AndroidViewModel {
    private ApiService apiService;
    private MutableLiveData<Contrato> contratoMutable = new MutableLiveData<>();
    private MutableLiveData<List<Pago>> pagosMutable = new MutableLiveData<>();
    public ContratoInquilinoViewModel(@NonNull Application application) {
        super(application);
        apiService = RetrofitClient.getService(application);
    }

    public LiveData<Contrato> getContratoMutable() {
        return contratoMutable;
    }

    public LiveData<List<Pago>> getPagosMutable() {
        return pagosMutable;
    }

    public void getContrato(Inmueble inmueble, boolean obtenerPagos) {
        Call<Contrato> call = apiService.obtenerContratoPorInmueble(inmueble.getId());
        call.enqueue(new Callback<Contrato>() {
            @Override
            public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                if(response.isSuccessful() && response.body() != null) {
                    contratoMutable.postValue(response.body());
                    if(obtenerPagos) getPagos(response.body().getId());
                }
            }

            @Override
            public void onFailure(Call<Contrato> call, Throwable throwable) {

            }
        });
    }

    private void getPagos(int contratoId) {
        Call<List<Pago>> call = apiService.obtenerPagosPorContrato(contratoId);

        call.enqueue(new Callback<List<Pago>>() {
            @Override
            public void onResponse(Call<List<Pago>> call, Response<List<Pago>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    pagosMutable.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Pago>> call, Throwable throwable) {

            }
        });
    }

    public void forceRefreshPagos() {
        pagosMutable.setValue(pagosMutable.getValue());
    }
}
