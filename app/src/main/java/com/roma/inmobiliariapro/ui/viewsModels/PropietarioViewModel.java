package com.roma.inmobiliariapro.ui.viewsModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.roma.inmobiliariapro.data.api.ApiService;
import com.roma.inmobiliariapro.data.api.RetrofitClient;
import com.roma.inmobiliariapro.data.model.UiMessage;
import com.roma.inmobiliariapro.data.model.response.Propietario;
import com.roma.inmobiliariapro.utils.MessageManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropietarioViewModel extends AndroidViewModel {
    private final ApiService apiService;
    private final MutableLiveData<Propietario> propietarioMutable = new MutableLiveData<>();
    private final MutableLiveData<String> msgErrorMutable = new MutableLiveData<>();
    public PropietarioViewModel(@NonNull Application application) {
        super(application);

        apiService = RetrofitClient.getService(application);
    }

    public LiveData<Propietario> getPropietarioMutable() {
        return propietarioMutable;
    }

    public LiveData<String> getMsgErrorMutable() {
        return msgErrorMutable;
    }

    public void getPropietario() {
        if(propietarioMutable.getValue() != null) {
            propietarioMutable.setValue(propietarioMutable.getValue());
            return;
        }

        Call<Propietario> call = apiService.obtenerPerfil();

        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if(response.isSuccessful() && response.body() != null) {
                    propietarioMutable.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable throwable) {
                // manejar el error ... msj etc
            }
        });
    }

    public void updatePropietario(String nombre, String apellido, String tel) {
        Propietario propietario = propietarioMutable.getValue();

        propietario.setNombre(nombre);
        propietario.setApellido(apellido);
        propietario.setTelefono(tel);

        Call<Propietario> call = apiService.actualizarPerfil(propietario);

        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if(response.isSuccessful() && response.body() != null) {
                    propietarioMutable.postValue(response.body());
                    MessageManager.send(new UiMessage("Perfil", "Perfil editado correctamente.", true));
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable throwable) {
                // manejar el error ... msj etc
            }
        });
    }

    public void cambiarContrasena(String currentPassword, String newPassword) {
        Call<Void> call = apiService.cambiarContrasena(currentPassword, newPassword);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    MessageManager.send(new UiMessage("Perfil", "Contraseña editada correctamente.", true));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                // manejar error etc msg ...
            }
        });
    }
}
