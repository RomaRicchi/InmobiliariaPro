package com.roma.inmobiliariapro.ui.login;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.roma.inmobiliariapro.data.repository.LoginRepository;
import com.roma.inmobiliariapro.utils.SessionManager;

public class LoginViewModel extends AndroidViewModel {
    private LoginRepository repository;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        SessionManager sessionManager = new SessionManager(application);
        repository = new LoginRepository(sessionManager);
    }

    public LiveData<String> login(String usuario, String clave) {
        isLoading.setValue(true);
        MutableLiveData<String> result = (MutableLiveData<String>) repository.login(usuario, clave);
        // Note: Repository returns LiveData, we might want to observe it here to update isLoading
        return result;
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
