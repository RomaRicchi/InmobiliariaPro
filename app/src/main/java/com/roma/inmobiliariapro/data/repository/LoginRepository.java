package com.roma.inmobiliariapro.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.roma.inmobiliariapro.data.api.ApiService;
import com.roma.inmobiliariapro.data.api.RetrofitClient;
import com.roma.inmobiliariapro.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    private ApiService apiService;
    private SessionManager sessionManager;

    public LoginRepository(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.apiService = RetrofitClient.getService(sessionManager);
    }

    public LiveData<String> login(String usuario, String clave) {
        MutableLiveData<String> loginResult = new MutableLiveData<>();
        apiService.login(usuario, clave).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body();
                    sessionManager.saveToken(token);
                    loginResult.setValue(token);
                } else {
                    loginResult.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loginResult.setValue(null);
            }
        });
        return loginResult;
    }
}
