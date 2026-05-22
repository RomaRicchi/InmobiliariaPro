package com.roma.inmobiliariapro.data.api;

import android.content.Context;

import com.roma.inmobiliariapro.preferences.SessionManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    public static final String ACTION_UNAUTHORIZED = "com.roma.inmobiliariapro.UNAUTHORIZED";
    private static final String BASE_URL = "https://capacitacion.alwaysdata.net/";
    private static Retrofit retrofit = null;

    public static ApiService getService(Context context) {
        if (retrofit == null) {
            SessionManager sessionManager = SessionManager.getInstance(context.getApplicationContext());
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new AuthInterceptor(sessionManager)).build();

//            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}
