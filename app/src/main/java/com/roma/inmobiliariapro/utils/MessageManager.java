package com.roma.inmobiliariapro.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.roma.inmobiliariapro.data.model.Status;
import com.roma.inmobiliariapro.data.model.UiMessage;

public class MessageManager {
    private static final MutableLiveData<UiMessage> uiMessageMutable = new MutableLiveData<>();

    public static LiveData<UiMessage> getUiMessageMutable() {
        return uiMessageMutable;
    }

    public static void send(UiMessage msg) {
        uiMessageMutable.postValue(msg);
    }

    public static void sendMsgResponse(int code, String modulo) {
        switch (code) {
            case 200:
                uiMessageMutable.postValue(new UiMessage(modulo, "Solicitud realizada correctamente.", Status.SUCCESS));
                break;
            case 400:
                uiMessageMutable.postValue(new UiMessage(modulo, "Solicitud incorrecta.", Status.ERROR));
                break;
            case 401:
                uiMessageMutable.postValue(new UiMessage(modulo, "Credenciales inválidas.", Status.ERROR));
                break;
            case 403:
                uiMessageMutable.postValue(new UiMessage(modulo, "No autorizado.", Status.ERROR));
                break;
            case 404:
                uiMessageMutable.postValue(new UiMessage(modulo, "Recurso no encontrado.", Status.ERROR));
                break;
            case 500:
                uiMessageMutable.postValue(new UiMessage(modulo, "Error del servidor.", Status.ERROR));
                break;
            default:
                uiMessageMutable.postValue(new UiMessage(modulo, "Ocurrió un error, inténtalo más tarde.", Status.ERROR));
                break;
        }
    }
}
