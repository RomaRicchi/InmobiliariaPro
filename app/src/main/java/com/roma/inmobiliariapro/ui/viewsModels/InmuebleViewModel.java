package com.roma.inmobiliariapro.ui.viewsModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.roma.inmobiliariapro.data.api.ApiService;
import com.roma.inmobiliariapro.data.api.RetrofitClient;
import com.roma.inmobiliariapro.data.model.response.Inmueble;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleViewModel extends AndroidViewModel {
    private ApiService apiService;
    private MutableLiveData<List<Inmueble>> inmueblesMutable = new MutableLiveData<>();
    private MutableLiveData<List<Inmueble>> inmueblesAlquiladosMutable = new MutableLiveData<>();
    private MutableLiveData<Inmueble> inmuebleMutable = new MutableLiveData<>();
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
        //DEJO ESTE MSG ACA PARA QUE SE ENTIENDA QUE:
        //al obtener todos los inmubles con getInmuebles()
        //los inmubles segun el modelo Inmueble tiene un booleano que
        //afirma si tiene un contrato vigente por ende esta funcion no sirve
        //salvo que ese booleano no sirva o se utilice para otra cosa...

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

    public void setInmueble(Inmueble inmueble) {
        inmuebleMutable.setValue(inmueble);
    }
}
