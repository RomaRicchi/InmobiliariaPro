package com.roma.inmobiliariapro.ui.viewsModels;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.roma.inmobiliariapro.data.api.ApiService;
import com.roma.inmobiliariapro.data.api.RetrofitClient;
import com.roma.inmobiliariapro.data.model.Status;
import com.roma.inmobiliariapro.data.model.UiMessage;
import com.roma.inmobiliariapro.data.model.response.Propietario;
import com.roma.inmobiliariapro.utils.FieldValidation;
import com.roma.inmobiliariapro.utils.MessageManager;
import com.roma.inmobiliariapro.utils.SharedPreferesManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropietarioViewModel extends AndroidViewModel {
    private final ApiService apiService;
    private final MutableLiveData<Propietario> propietarioMutable = new MutableLiveData<>();

//    private final MutableLiveData<Status> updateState = new MutableLiveData<>(Status.IDLE);
    private final MutableLiveData<FieldValidation> updateState = new MutableLiveData<>();

//    private final MutableLiveData<Status> changePasswordState = new MutableLiveData<>(Status.IDLE);

    private final MutableLiveData<FieldValidation> changePasswordState = new MutableLiveData<>();
    public PropietarioViewModel(@NonNull Application application) {
        super(application);
        SharedPreferesManager sharedPreferesManager = new SharedPreferesManager(application);
        apiService = RetrofitClient.getService(sharedPreferesManager);
    }

    public LiveData<Propietario> getPropietarioMutable() { return propietarioMutable; }
    public LiveData<FieldValidation> getUpdateState() { return updateState; }
    public LiveData<FieldValidation> getChangePasswordState() { return changePasswordState; }

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
                }else {
                    Log.e("API - PROPIETARIO", "Error en la respuesta del servidor: " + response.code());
                    MessageManager.sendMsgResponse(response.code(), "Propietario");
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable throwable) {
                MessageManager.sendMsgResponse(0, "Propietario");
                Log.e("API - PROPIETARIO", throwable.getMessage(), throwable);
            }
        });
    }

    public void updatePropietario(String nombre, String apellido, String tel) {
        Propietario propietario = propietarioMutable.getValue();

        if(nombre == null || nombre.isEmpty()) {
            updateState.setValue(new FieldValidation("nombre", Status.WARNING));
            MessageManager.send(new UiMessage("Propietario", "El campo Nombre es obligatorio.", Status.WARNING));
            return;
        }

        if(apellido == null || apellido.isEmpty()) {
            updateState.setValue(new FieldValidation("apellido", Status.WARNING));
            MessageManager.send(new UiMessage("Propietario", "El campo Apellido es obligatorio.", Status.WARNING));
            return;
        }

        if(tel == null || tel.isEmpty()) {
            updateState.setValue(new FieldValidation("telefono", Status.WARNING));
            MessageManager.send(new UiMessage("Propietario", "El campo Teléfono  es obligatorio.", Status.WARNING));
            return;
        }

        propietario.setNombre(nombre);
        propietario.setApellido(apellido);
        propietario.setTelefono(tel);

        updateState.setValue(new FieldValidation("", Status.LOADING));
        Call<Propietario> call = apiService.actualizarPerfil(propietario);
        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if(response.isSuccessful() && response.body() != null) {
                    propietarioMutable.postValue(response.body());
                    updateState.postValue(new FieldValidation("", Status.SUCCESS));
                    MessageManager.send(new UiMessage("Propietario", "Perfil editado correctamente.", Status.SUCCESS));
                } else {
                    Log.e("API - PROPIETARIO", "Error en la respuesta del servidor: " + response.code());
                    updateState.postValue(new FieldValidation("", Status.ERROR));
                    MessageManager.sendMsgResponse(response.code(), "Propietario");
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable throwable) {
                updateState.setValue(new FieldValidation("", Status.ERROR));
                MessageManager.sendMsgResponse(0, "Propietario");
                Log.e("API - PROPIETARIO", throwable.getMessage(), throwable);
            }
        });
    }

    public void cambiarContrasena(String currentPassword, String newPassword) {
        if(currentPassword == null || currentPassword.isEmpty()) {
            changePasswordState.setValue(new FieldValidation("currentPassword", Status.WARNING));
            MessageManager.send(new UiMessage("Propietario", "El campo Contraseña actual es obligatorio.", Status.WARNING));
            return;
        }

        if(newPassword == null || newPassword.isEmpty()) {
            changePasswordState.setValue(new FieldValidation("newPassword", Status.WARNING));
            MessageManager.send(new UiMessage("Propietario", "El campo Nueva contraseña es obligatorio.", Status.WARNING));
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
