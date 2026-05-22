package com.roma.inmobiliariapro.ui.inmuebles;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.roma.inmobiliariapro.data.api.ApiService;
import com.roma.inmobiliariapro.data.api.RetrofitClient;
import com.roma.inmobiliariapro.data.model.Status;
import com.roma.inmobiliariapro.data.model.UiMessage;
import com.roma.inmobiliariapro.data.model.response.Inmueble;
import com.roma.inmobiliariapro.utils.MessageManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleDetalleViewModel extends AndroidViewModel {
    private final ApiService apiService;
    private final MutableLiveData<Inmueble> inmuebleMutable = new MutableLiveData<>();
    private final MutableLiveData<Status> toggleEstadoState = new MutableLiveData<>(Status.IDLE);
    public InmuebleDetalleViewModel(@NonNull Application application) {
        super(application);
        apiService = RetrofitClient.getService(application);
    }

    public LiveData<Inmueble> getInmuebleMutable() { return inmuebleMutable; }

    public void setInmueble(Inmueble inmueble) { inmuebleMutable.setValue(inmueble); }

    public MutableLiveData<Status> getToggleEstadoState() { return toggleEstadoState; }

    public void toggleEstadoInmueble() {
        toggleEstadoState.setValue(Status.LOADING);

        Inmueble inmueble = inmuebleMutable.getValue();
        if (inmueble == null) return;

        inmueble.setEstado(!inmueble.isEstado());

        Call<Inmueble> call = apiService.actualizarInmueble(inmueble);
        call.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if(response.isSuccessful() && response.body() != null) {
                    inmuebleMutable.postValue(response.body());
                    toggleEstadoState.postValue(Status.SUCCESS);
                    MessageManager.send(new UiMessage("Inmueble", "Estado actualizado correctamente.", Status.SUCCESS));
                } else {
                    toggleEstadoState.postValue(Status.ERROR);
                    Log.e("API - INMUEBLE", "Error en la respuesta del servidor: " + response.code());
                    MessageManager.sendMsgResponse(response.code(), "Inmueble");
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable throwable) {
                toggleEstadoState.postValue(Status.ERROR);
                MessageManager.sendMsgResponse(0, "Inmueble");
                Log.e("API - INMUEBLE", throwable.getMessage(), throwable);
            }
        });
    }
}
