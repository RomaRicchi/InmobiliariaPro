package com.roma.inmobiliariapro.ui.perfil;

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
import com.roma.inmobiliariapro.utils.FieldValidation;
import com.roma.inmobiliariapro.utils.MessageManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CambiarClaveViewModel extends AndroidViewModel {
    private final ApiService apiService;
    private final MutableLiveData<FieldValidation> changePasswordState = new MutableLiveData<>();
    public CambiarClaveViewModel(@NonNull Application application) {
        super(application);
        apiService = RetrofitClient.getService(application);
    }

    public LiveData<FieldValidation> getChangePasswordState() { return changePasswordState; }

    public void resetChangePasswordState() { changePasswordState.setValue(null); }

    public void cambiarContrasena(String currentPassword, String newPassword) {
        if(currentPassword == null || currentPassword.isEmpty()) {
            changePasswordState.setValue(new FieldValidation("currentPassword", Status.WARNING));
            return;
        }

        if(newPassword == null || newPassword.isEmpty()) {
            changePasswordState.setValue(new FieldValidation("newPassword", Status.WARNING));
            return;
        }

        changePasswordState.setValue(new FieldValidation("", Status.LOADING));
        Call<Void> call = apiService.cambiarContrasena(currentPassword, newPassword);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    changePasswordState.postValue(new FieldValidation("", Status.SUCCESS));
                    MessageManager.send(new UiMessage("Propietario", "Contraseña editada correctamente.", Status.SUCCESS));
                } else {
                    Log.e("API - PROPIETARIO", "Error en la respuesta del servidor: " + response.code());
                    changePasswordState.postValue(new FieldValidation("", Status.ERROR));
                    MessageManager.sendMsgResponse(response.code(), "Propietario");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                changePasswordState.postValue(new FieldValidation("", Status.ERROR));
                MessageManager.sendMsgResponse(0, "Propietario");
                Log.e("API - PROPIETARIO", throwable.getMessage(), throwable);
            }
        });
    }
}
