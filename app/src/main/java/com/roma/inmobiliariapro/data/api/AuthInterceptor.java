package com.roma.inmobiliariapro.data.api;

import androidx.annotation.NonNull;

import com.roma.inmobiliariapro.preferences.SessionManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final SessionManager sessionManager;

    public AuthInterceptor(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder();
        String token = sessionManager.getToken();

        if (token != null) {
            builder.addHeader(
                    "Authorization",
                    "Bearer " + token
            );
        }

        Response response = chain.proceed(builder.build());

        if (response.code() == 401) {
            sessionManager.logout();
        }

        return response;
    }
}
