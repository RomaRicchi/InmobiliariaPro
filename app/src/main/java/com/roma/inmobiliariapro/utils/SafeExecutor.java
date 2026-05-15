package com.roma.inmobiliariapro.utils;

import android.util.Log;

import com.roma.inmobiliariapro.data.model.Status;
import com.roma.inmobiliariapro.data.model.UiMessage;

public class SafeExecutor {
    /**
     * SafeExecutor: helper para ejecutar bloques de código con una red de seguridad.
     * Captura excepciones inesperadas, las loguea y envía un mensaje genérico al usuario.
     *
     * Uso recomendado:
     * - Envolver lógica sincrónica que pueda lanzar excepciones no controladas.
     * - No reemplaza validaciones específicas (ej. parseo de números).
     * - En callbacks asíncronos (Retrofit, listeners), debe usarse dentro del callback.
     */
    public static void safeExecute(Runnable runnable, String context) {
        try {
            runnable.run();
        } catch (Exception e) {
            Log.e("SAFE_EXECUTOR - " + context, "Error inesperado", e);
            MessageManager.send(new UiMessage(context, "Ocurrió un error inesperado.", Status.ERROR));
        }
    }
}

