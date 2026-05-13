package com.roma.inmobiliariapro.ui.viewsModels;

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
import com.roma.inmobiliariapro.utils.MessageManager;
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
    private ApiService apiService;
    private final MutableLiveData<List<Inmueble>> inmueblesMutable = new MutableLiveData<>();
    private final MutableLiveData<List<Inmueble>> inmueblesAlquiladosMutable = new MutableLiveData<>();
    private final MutableLiveData<Inmueble> inmuebleMutable = new MutableLiveData<>();
    private final MutableLiveData<Status> toggleEstadoState = new MutableLiveData<>(Status.IDLE);
    private final MutableLiveData<Status> createInmuebleState = new MutableLiveData<>(Status.IDLE);
    public InmuebleViewModel(@NonNull Application application) {
        super(application);
        SharedPreferesManager sharedPreferesManager = new SharedPreferesManager(application);
        apiService = RetrofitClient.getService(sharedPreferesManager);
    }

    public LiveData<Inmueble> getInmuebleMutable() { return inmuebleMutable; }
    public LiveData<List<Inmueble>> getInmueblesMutable() { return inmueblesMutable; }
    public LiveData<List<Inmueble>> getInmueblesAlquiladosMutable() { return inmueblesAlquiladosMutable; }
    public LiveData<Status> getToggleEstadoState() { return toggleEstadoState; }
    public LiveData<Status> getCreateInmuebleState() { return createInmuebleState; }

    public void getInmuebles() {
        Call<List<Inmueble>> call = apiService.obtenerInmuebles();
        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    inmueblesMutable.postValue(response.body());
                } else {
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

    public void getInmublesAlquilados() {
        Call<List<Inmueble>> call = apiService.obtenerInmueblesAlquilados();
        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    inmueblesAlquiladosMutable.postValue(response.body());
                } else {
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

    public void toggleEstadoInmueble() {
        toggleEstadoState.setValue(Status.LOADING);

        Inmueble inmueble = inmuebleMutable.getValue();
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

    public void crearInmueble(
            File imageFile,
            String direccion,
            String uso,
            String tipo,
            String ambientes,
            String superficie,
            String precio,
            boolean disponible
    ) {
        if(imageFile == null) {
            createInmuebleState.setValue(Status.WARNING);
            MessageManager.send(new UiMessage("Inmueble", "La foto es requerida.", Status.WARNING));
            return;
        }

        if(direccion == null || direccion.isEmpty()) {
            createInmuebleState.setValue(Status.WARNING);
            MessageManager.send(new UiMessage("Inmueble", "El campo Dirección es obligatorio.", Status.WARNING));
            return;
        }

        if(uso == null || uso.isEmpty()) {
            createInmuebleState.setValue(Status.WARNING);
            MessageManager.send(new UiMessage("Inmueble", "El campo Uso es obligatorio.", Status.WARNING));
            return;
        }

        if(tipo == null || tipo.isEmpty()) {
            createInmuebleState.setValue(Status.WARNING);
            MessageManager.send(new UiMessage("Inmueble", "El campo Tipo es obligatorio.", Status.WARNING));
            return;
        }

        if(ambientes == null || ambientes.isEmpty()) {
            createInmuebleState.setValue(Status.WARNING);
            MessageManager.send(new UiMessage("Inmueble", "El campo Ambientes es obligatorio.", Status.WARNING));
            return;
        }

        if(superficie == null || superficie.isEmpty()) {
            createInmuebleState.setValue(Status.WARNING);
            MessageManager.send(new UiMessage("Inmueble", "El campo Superficie es obligatorio.", Status.WARNING));
            return;
        }

        if(precio == null || precio.isEmpty()) {
            createInmuebleState.setValue(Status.WARNING);
            MessageManager.send(new UiMessage("Inmueble", "El campo Precio es obligatorio.", Status.WARNING));
            return;
        }

        int numAmbientes = Integer.parseInt(ambientes);
        int numSuperficie = Integer.parseInt(superficie);
        double numPrecio = Double.parseDouble(precio);

        // validar valores válidos
        if(numAmbientes <= 0) {
            createInmuebleState.setValue(Status.WARNING);
            MessageManager.send(new UiMessage("Inmueble", "Ambientes debe ser mayor a 0.", Status.WARNING));
            return;
        }

        if(numSuperficie <= 0) {
            createInmuebleState.setValue(Status.WARNING);
            MessageManager.send(new UiMessage("Inmueble", "Superficie debe ser mayor a 0.", Status.WARNING));
            return;
        }

        if(numPrecio <= 0) {
            createInmuebleState.setValue(Status.WARNING);
            MessageManager.send(new UiMessage("Inmueble", "Precio debe ser mayor a 0.", Status.WARNING));
            return;
        }

        RequestBody requestFile = RequestBody.create(imageFile, MediaType.get("image/*"));
        MultipartBody.Part imagen = MultipartBody.Part.createFormData(
                "imagen",
                imageFile.getName(),
                requestFile
        );

        Inmueble inmueble = new Inmueble();
        inmueble.setDireccion(direccion);
        inmueble.setUso(uso);
        inmueble.setTipo(tipo);
        inmueble.setAmbientes(numAmbientes);
        inmueble.setSuperficie(numSuperficie);
        inmueble.setPrecio(numPrecio);
        inmueble.setEstado(disponible);


        Gson gson = new Gson();
        String inmuebleJson = gson.toJson(inmueble);

        RequestBody inmuebleBody = RequestBody.create(
                inmuebleJson,
                MediaType.get("application/json")
        );

        createInmuebleState.setValue(Status.LOADING);
        Call<Inmueble> call = apiService.cargarInmueble(imagen, inmuebleBody);
        call.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<Inmueble> actual = inmueblesMutable.getValue() != null ? new ArrayList<>(inmueblesMutable.getValue()) : new ArrayList<>();
                    actual.add(response.body());
                    inmueblesMutable.postValue(actual);
                    createInmuebleState.postValue(Status.SUCCESS);
                    MessageManager.send(new UiMessage("Inmueble", "Inmueble creado correctamente.", Status.SUCCESS));
                } else {
                    createInmuebleState.postValue(Status.ERROR);
                    MessageManager.sendMsgResponse(response.code(), "Inmueble");
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable throwable) {
                createInmuebleState.postValue(Status.ERROR);
                MessageManager.sendMsgResponse(0, "Inmueble");
                Log.e("API - INMUEBLE", throwable.getMessage(), throwable);
            }
        });
    }

    public void setInmueble(Inmueble inmueble) { inmuebleMutable.setValue(inmueble); }

    public void validarImage(Uri imageUri, Exception e) {
        if(imageUri == null) {
            MessageManager.send(new UiMessage("Inmueble", "La foto es requerida.", Status.WARNING));
            return;
        }

        if(e != null) {
            MessageManager.send(new UiMessage("Inmueble", "Ocurrió un error, inténtalo más tarde.", Status.WARNING));
            Log.e("API - INMUEBLE", e.getMessage(), e);
        }
    }
}
