package com.roma.inmobiliariapro.ui.slideshow;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.roma.inmobiliariapro.data.model.response.Propietario;
import com.roma.inmobiliariapro.data.repository.PropietarioRepository;
import com.roma.inmobiliariapro.utils.SessionManager;

public class SlideshowViewModel extends AndroidViewModel {

    private PropietarioRepository repository;
    private MutableLiveData<Propietario> mPropietario = new MutableLiveData<>();
    private MutableLiveData<String> mError = new MutableLiveData<>();
    private MutableLiveData<Boolean> mPasswordChanged = new MutableLiveData<>();

    public SlideshowViewModel(@NonNull Application application) {
        super(application);
        SessionManager sessionManager = new SessionManager(application);
        repository = new PropietarioRepository(sessionManager);
    }

    public LiveData<Propietario> getPropietario() {
        return mPropietario;
    }

    public LiveData<String> getError() {
        return mError;
    }

    public LiveData<Boolean> getPasswordChanged() {
        return mPasswordChanged;
    }

    public void cargarPerfil() {
        repository.obtenerPerfil().observeForever(propietario -> {
            if (propietario != null) {
                mPropietario.setValue(propietario);
            } else {
                mError.setValue("Error al cargar el perfil");
            }
        });
    }

    public void actualizarPerfil(Propietario p) {
        repository.actualizarPerfil(p).observeForever(success -> {
            if (success) {
                mPropietario.setValue(p);
            } else {
                mError.setValue("Error al actualizar el perfil");
            }
        });
    }

    public void cambiarContrasena(String actual, String nueva) {
        repository.cambiarPassword(actual, nueva).observeForever(success -> {
            if (success) {
                mPasswordChanged.setValue(true);
            } else {
                mError.setValue("Error al cambiar la contraseña. Verifique la actual.");
                mPasswordChanged.setValue(false);
            }
        });
    }
}
