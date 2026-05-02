package com.roma.inmobiliariapro.ui.slideshow;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.roma.inmobiliariapro.data.model.Propietario;
import com.roma.inmobiliariapro.data.repository.PropietarioRepository;
import com.roma.inmobiliariapro.utils.SessionManager;

public class SlideshowViewModel extends AndroidViewModel {

    private PropietarioRepository repository;
    private MutableLiveData<Propietario> mPropietario = new MutableLiveData<>();
    private MutableLiveData<String> mError = new MutableLiveData<>();

    public SlideshowViewModel(@NonNull Application application) {
        super(application);
        SessionManager sessionManager = new SessionManager(application);
        repository = new PropietarioRepository(sessionManager);
    }

    public LiveData<Propietario> getPropietario() {
        return mPropietario;
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
                mError.setValue("Error al actualizar");
            }
        });
    }
}
