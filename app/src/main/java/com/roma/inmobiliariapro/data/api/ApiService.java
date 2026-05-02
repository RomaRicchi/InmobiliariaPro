package com.roma.inmobiliariapro.data.api;

import com.roma.inmobiliariapro.data.model.Contrato;
import com.roma.inmobiliariapro.data.model.Inmueble;
import com.roma.inmobiliariapro.data.model.Pago;
import com.roma.inmobiliariapro.data.model.Propietario;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    // --- Autenticación ---
    @FormUrlEncoded
    @POST("api/Propietarios/login")
    Call<String> login(@Field("Usuario") String usuario, @Field("Clave") String clave);

    // --- Propietario ---
    @GET("api/Propietarios")
    Call<Propietario> obtenerPerfil();

    @PUT("api/Propietarios/actualizar")
    Call<Propietario> actualizarPerfil(@Body Propietario propietario);

    @FormUrlEncoded
    @POST("api/Propietarios/email")
    Call<String> resetearContrasena(@Field("email") String email);

    @FormUrlEncoded
    @PUT("api/Propietarios/changePassword")
    Call<Void> cambiarContrasena(@Field("currentPassword") String currentPassword, @Field("newPassword") String newPassword);

    // --- Inmuebles ---
    @GET("api/Inmuebles")
    Call<List<Inmueble>> obtenerInmuebles();

    @GET("api/Inmuebles/GetContratoVigente")
    Call<List<Inmueble>> obtenerInmueblesAlquilados();

    @Multipart
    @POST("api/Inmuebles/cargar")
    Call<Inmueble> cargarInmueble(@Part MultipartBody.Part imagen, @Part("inmueble") RequestBody inmueble);

    @PUT("api/Inmuebles/actualizar")
    Call<Inmueble> actualizarInmueble(@Body Inmueble inmueble);

    // --- Contratos ---
    @GET("api/contratos/inmueble/{id}")
    Call<Contrato> obtenerContratoPorInmueble(@Path("id") int idInmueble);

    // --- Pagos ---
    @GET("api/pagos/contrato/{id}")
    Call<List<Pago>> obtenerPagosPorContrato(@Path("id") int idContrato);
}
