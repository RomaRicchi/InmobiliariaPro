package com.roma.inmobiliariapro.ui.inmuebles;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.roma.inmobiliariapro.data.model.response.Inmueble;
import com.roma.inmobiliariapro.data.repository.InmuebleRepository;
import com.roma.inmobiliariapro.utils.SessionManager;
import java.util.List;

public class InmuebleViewModel extends AndroidViewModel {

    private InmuebleRepository repository;
    private LiveData<List<Inmueble>> mInmuebles;

    public InmuebleViewModel(@NonNull Application application) {
        super(application);
        SessionManager sessionManager = new SessionManager(application);
        repository = new InmuebleRepository(sessionManager);
        mInmuebles = repository.obtenerInmuebles();
    }

    public LiveData<List<Inmueble>> getInmuebles() {
        return mInmuebles;
    }
}
