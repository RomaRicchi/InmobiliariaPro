package com.roma.inmobiliariapro.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.roma.inmobiliariapro.data.api.ApiService;
import com.roma.inmobiliariapro.data.api.RetrofitClient;
import com.roma.inmobiliariapro.data.model.response.Contrato;
import com.roma.inmobiliariapro.data.model.response.Inmueble;
import com.roma.inmobiliariapro.utils.SessionManager;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContratoRepository {
    private ApiService apiService;

    public ContratoRepository(SessionManager sessionManager) {
        this.apiService = RetrofitClient.getService(sessionManager);
    }

    public LiveData<List<Inmueble>> obtenerInmueblesAlquilados() {
        MutableLiveData<List<Inmueble>> data = new MutableLiveData<>();
        apiService.obtenerInmueblesAlquilados().enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<Contrato> obtenerContratoPorInmueble(int idInmueble) {
        MutableLiveData<Contrato> data = new MutableLiveData<>();
        apiService.obtenerContratoPorInmueble(idInmueble).enqueue(new Callback<Contrato>() {
            @Override
            public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Contrato> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
