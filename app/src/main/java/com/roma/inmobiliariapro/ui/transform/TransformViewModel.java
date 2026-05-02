package com.roma.inmobiliariapro.ui.transform;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.roma.inmobiliariapro.data.model.Inmueble;
import com.roma.inmobiliariapro.data.repository.InmuebleRepository;
import com.roma.inmobiliariapro.utils.SessionManager;
import java.util.List;

public class TransformViewModel extends AndroidViewModel {

    private InmuebleRepository repository;
    private LiveData<List<Inmueble>> mInmuebles;

    public TransformViewModel(@NonNull Application application) {
        super(application);
        SessionManager sessionManager = new SessionManager(application);
        repository = new InmuebleRepository(sessionManager);
        mInmuebles = repository.obtenerInmuebles();
    }

    public LiveData<List<Inmueble>> getInmuebles() {
        return mInmuebles;
    }
}
