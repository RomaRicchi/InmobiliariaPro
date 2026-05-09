package com.roma.inmobiliariapro.ui.login;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.roma.inmobiliariapro.MainActivity;
import com.roma.inmobiliariapro.data.api.ApiService;
import com.roma.inmobiliariapro.data.api.RetrofitClient;
import com.roma.inmobiliariapro.utils.SharedPreferesManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private ApiService apiService;
    private SharedPreferesManager sharedPreferesManager;

    public LoginViewModel(@NonNull Application application) {
        super(application);
       sharedPreferesManager = new SharedPreferesManager(application);
       apiService = RetrofitClient.getService(application);
    }

    public void login(String usuario, String clave) {
        isLoading.setValue(true);

        Call<String> call = apiService.login(usuario, clave);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful() && response.body() != null) {
                    String token = response.body();
                    sharedPreferesManager.saveToken(token);
                    //msg de bienvenida
                    isLoading.setValue(false);
                    Intent itt = new Intent(getApplication(), MainActivity.class);
                    itt.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    getApplication().startActivity(itt);
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                //msg de error

                isLoading.setValue(false);
            }
        });
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public void setLoading(boolean loading) {
        isLoading.setValue(loading);
    }
}
