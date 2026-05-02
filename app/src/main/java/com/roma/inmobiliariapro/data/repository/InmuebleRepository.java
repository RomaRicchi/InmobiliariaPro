package com.roma.inmobiliariapro.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.roma.inmobiliariapro.data.api.ApiService;
import com.roma.inmobiliariapro.data.api.RetrofitClient;
import com.roma.inmobiliariapro.data.model.Inmueble;
import com.roma.inmobiliariapro.utils.SessionManager;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleRepository {
    private ApiService apiService;

    public InmuebleRepository(SessionManager sessionManager) {
        this.apiService = RetrofitClient.getService(sessionManager);
    }

    public LiveData<List<Inmueble>> obtenerInmuebles() {
        MutableLiveData<List<Inmueble>> data = new MutableLiveData<>();
        apiService.obtenerInmuebles().enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful() && response.body() != null) {
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
}
