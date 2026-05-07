package com.roma.inmobiliariapro.ui.inmuebles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.roma.inmobiliariapro.data.model.response.Inmueble;

public class InmuebleDetalleViewModel extends ViewModel {
    private MutableLiveData<Inmueble> mInmueble = new MutableLiveData<>();

    public LiveData<Inmueble> getInmueble() {
        return mInmueble;
    }

    public void setInmueble(Inmueble inmueble) {
        mInmueble.setValue(inmueble);
    }
}
