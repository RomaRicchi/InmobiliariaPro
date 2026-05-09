package com.roma.inmobiliariapro.data.api;

import android.content.Context;

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
    private static final String BASE_URL = "https://capacitacion.alwaysdata.net/";
    private static Retrofit retrofit = null;

    private static SharedPreferesManager sharedPreferesManager;

    public static ApiService getService(Context context) {
        sharedPreferesManager = new SharedPreferesManager(context.getApplicationContext());
        
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
                                // Limpiamos el token de posibles comillas si el ScalarConverter las dejó
                                String cleanToken = token.replace("\"", "");
                                requestBuilder.header("Authorization", "Bearer " + cleanToken);
                            }

                            Request request = requestBuilder.build();


                            return chain.proceed(request);
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
