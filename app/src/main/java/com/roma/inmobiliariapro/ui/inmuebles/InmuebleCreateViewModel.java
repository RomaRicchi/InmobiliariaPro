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
import com.roma.inmobiliariapro.utils.FileUtil;
import com.roma.inmobiliariapro.utils.MessageManager;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleCreateViewModel extends AndroidViewModel {
    private final ApiService apiService;
    private final MutableLiveData<FieldValidation> createInmuebleState = new MutableLiveData<>();
    private final MutableLiveData<Uri> uriImageMutable = new MutableLiveData<>();
    public InmuebleCreateViewModel(@NonNull Application application) {
        super(application);
        apiService = RetrofitClient.getService(application);
    }

    public LiveData<FieldValidation> getCreateInmuebleState() { return createInmuebleState; }

    public void setUriImage(Uri uriImage) { uriImageMutable.setValue(uriImage); }

    public void crearInmueble(
            String direccion,
            String uso,
            String tipo,
            String ambientes,
            String superficie,
            String precio,
            boolean disponible
    ) {
        if(uriImageMutable.getValue() == null) {
            MessageManager.send(new UiMessage("Inmueble", "La foto es requerida.", Status.WARNING));
            return;
        }

        if(direccion == null || direccion.isEmpty()) {
            createInmuebleState.postValue(new FieldValidation("direccion", Status.WARNING));
            return;
        }

        if (uso == null || uso.isEmpty()) {
            createInmuebleState.postValue(new FieldValidation("uso", Status.WARNING));
            return;
        }

        if (tipo == null || tipo.isEmpty()) {
            createInmuebleState.postValue(new FieldValidation("tipo", Status.WARNING));
            return;
        }

        if (ambientes == null || ambientes.isEmpty()) {
            createInmuebleState.postValue(new FieldValidation("ambientes", Status.WARNING));
            return;
        }

        if (superficie == null || superficie.isEmpty()) {
            createInmuebleState.postValue(new FieldValidation("superficie", Status.WARNING));
            return;
        }

        if (precio == null || precio.isEmpty()) {
            createInmuebleState.postValue(new FieldValidation("precio", Status.WARNING));
            return;
        }

        int numAmbientes;
        int numSuperficie;
        double numPrecio;
        File imageFile;

        try {
            numAmbientes = Integer.parseInt(ambientes);
            numSuperficie = Integer.parseInt(superficie);
            numPrecio = Double.parseDouble(precio);
        } catch (NumberFormatException e) {
            Log.e("API - INMUEBLE", "Formato de número inválido.", e);
            MessageManager.send(new UiMessage("Inmueble", "Formato de número inválido.", Status.WARNING));
            return;
        }

        try {
            imageFile = FileUtil.from(getApplication().getApplicationContext(), uriImageMutable.getValue());
        } catch (Exception e) {
            Log.e("API - INMUEBLE", "Formato de foto inválido.", e);
            MessageManager.send(new UiMessage("Inmueble", "Formato de foto inválido.", Status.WARNING));
            return;
        }

        if (numAmbientes <= 0) {
            createInmuebleState.postValue(new FieldValidation("ambientesNumero", Status.WARNING));
            MessageManager.send(new UiMessage("Inmueble", "El campo ambientes deben ser mayor a 0.", Status.WARNING));
            return;
        }

        if (numSuperficie <= 0) {
            createInmuebleState.postValue(new FieldValidation("superficieNumero", Status.WARNING));
            MessageManager.send(new UiMessage("Inmueble", "El campo superficie deben ser mayor a 0.", Status.WARNING));
            return;
        }

        if (numPrecio <= 0) {
            createInmuebleState.postValue(new FieldValidation("precioNumero", Status.WARNING));
            MessageManager.send(new UiMessage("Inmueble", "El campo precio deben ser mayor a 0.", Status.WARNING));
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

        createInmuebleState.setValue(new FieldValidation("", Status.LOADING));
        MessageManager.send(new UiMessage("Inmueble", "Subiendo inmueble...", Status.LOADING));

        Call<Inmueble> call = apiService.cargarInmueble(imagen, inmuebleBody);
        call.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if(response.isSuccessful() && response.body() != null) {
                    createInmuebleState.postValue(new FieldValidation("", Status.SUCCESS));
                    MessageManager.send(new UiMessage("Inmueble", "Inmueble creado correctamente.", Status.SUCCESS));
                } else {
                    createInmuebleState.postValue(new FieldValidation("", Status.ERROR));
                    Log.e("API - INMUEBLE", "Error en la respuesta del servidor: " + response.code());
                    MessageManager.sendMsgResponse(response.code(), "Inmueble");
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable throwable) {
                createInmuebleState.postValue(new FieldValidation("", Status.ERROR));
                MessageManager.sendMsgResponse(0, "Inmueble");
                Log.e("API - INMUEBLE", throwable.getMessage(), throwable);
            }
        });
    }

    public void resetCreateState() {
        createInmuebleState.setValue(new FieldValidation("", Status.IDLE));
    }
}
