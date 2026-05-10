package com.roma.inmobiliariapro.data.api;

import android.content.Intent;
import com.roma.inmobiliariapro.utils.SharedPreferesManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    public static final String ACTION_UNAUTHORIZED = "com.roma.inmobiliariapro.UNAUTHORIZED";
    private static final String BASE_URL = "https://capacitacion.alwaysdata.net/";
    private static Retrofit retrofit = null;

    public static ApiService getService(SharedPreferesManager sharedPreferesManager) {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            Request.Builder requestBuilder = original.newBuilder();
                            
                            String token = sharedPreferesManager.getToken();
                            if (token != null && !token.isEmpty()) {
                                String cleanToken = token.replace("\"", "");
                                requestBuilder.header("Authorization", "Bearer " + cleanToken);
                            }

                            Request request = requestBuilder.build();
                            Response response = chain.proceed(request);

                            // Manejo del error 401 (No autorizado/Sesión expirada)
                            if (response.code() == 401) {
                                sharedPreferesManager.clearSession();
                                // Enviamos un broadcast para que BaseActivity redirija al login
                                Intent intent = new Intent(ACTION_UNAUTHORIZED);
                                intent.setPackage(sharedPreferesManager.getContext().getPackageName());
                                sharedPreferesManager.getContext().sendBroadcast(intent);
                            }

                            return response;
                        }
                    })
                    .build();

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
