package com.roma.inmobiliariapro.ui.viewsModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.roma.inmobiliariapro.data.api.ApiService;
import com.roma.inmobiliariapro.data.api.RetrofitClient;
import com.roma.inmobiliariapro.data.model.UiMessage;
import com.roma.inmobiliariapro.data.model.response.Inmueble;
import com.roma.inmobiliariapro.utils.MessageManager;

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
    public InmuebleViewModel(@NonNull Application application) {
        super(application);
        apiService = RetrofitClient.getService(application);
    }

    public LiveData<Inmueble> getInmuebleMutable() {
        return inmuebleMutable;
    }

    public LiveData<List<Inmueble>> getInmueblesMutable() {
        return inmueblesMutable;
    }

    public LiveData<List<Inmueble>> getInmueblesAlquiladosMutable() {
        return inmueblesAlquiladosMutable;
    }

    public void getInmuebles() {
        Call<List<Inmueble>> call = apiService.obtenerInmuebles();

        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    inmueblesMutable.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable throwable) {
                // manejar errores msg etcx.....
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
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable throwable) {
                // manejar errores etc...
            }
        });
    }

    public void toggleEstadoInmueble() {
        Inmueble inmueble = inmuebleMutable.getValue();
        inmueble.setEstado(!inmueble.isEstado());
        Call<Inmueble> call = apiService.actualizarInmueble(inmueble);

        call.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if(response.isSuccessful() && response.body() != null) {
                    inmuebleMutable.postValue(response.body());
                    MessageManager.send(new UiMessage("Inmueble", "Estado actualizado correctamente.", true));
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable throwable) {

            }
        });
    }

    public void crearInmueble(File imageFile, Inmueble inmueble) {
        RequestBody requestFile = RequestBody.create(imageFile, MediaType.get("image/*"));
        MultipartBody.Part imagen = MultipartBody.Part.createFormData(
                "imagen",
                imageFile.getName(),
                requestFile
        );

        Gson gson = new Gson();
        String inmuebleJson = gson.toJson(inmueble);

        RequestBody inmuebleBody = RequestBody.create(
                inmuebleJson,
                MediaType.get("application/json")
        );

        Call<Inmueble> call = apiService.cargarInmueble(imagen, inmuebleBody);
        call.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<Inmueble> actual = inmueblesMutable.getValue() != null ? new ArrayList<>(inmueblesMutable.getValue()) : new ArrayList<>();
                    actual.add(response.body());
                    inmueblesMutable.postValue(actual);

                    MessageManager.send(new UiMessage("Inmueble", "Inmueble creado correctamente.", true));
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable throwable) {
                // manejar el error easdasdasd
            }
        });
    }

    public void setInmueble(Inmueble inmueble) {
        inmuebleMutable.setValue(inmueble);
    }

    public boolean validarCampos(
            String direccionText,
            String usoText,
            String tipoText,
            String ambientesText,
            String superficieText,
            String precioText
    ) {

        // validar vacíos
        if(
                direccionText.isEmpty() ||
                usoText.isEmpty() ||
                tipoText.isEmpty() ||
                ambientesText.isEmpty() ||
                superficieText.isEmpty() ||
                precioText.isEmpty()
        ) {
            MessageManager.send(new UiMessage("Inmueble", "Debe completar todos los campos.", false));
            return false;
        }

        try {
            int ambientes = Integer.parseInt(ambientesText);
            int superficie = Integer.parseInt(superficieText);
            double precio = Double.parseDouble(precioText);

            // validar valores válidos
            if(ambientes <= 0) {
                MessageManager.send(new UiMessage("Inmueble", "Ambientes debe ser mayor a 0.", false));
                return false;
            }

            if(superficie <= 0) {
                MessageManager.send(new UiMessage("Inmueble", "Superficie debe ser mayor a 0.", false));
                return false;
            }

            if(precio <= 0) {
                MessageManager.send(new UiMessage("Precio", "Superficie debe ser mayor a 0.", false));
                return false;
            }

            return true;

        } catch (NumberFormatException e) {

            return false;
        }
    }
}
