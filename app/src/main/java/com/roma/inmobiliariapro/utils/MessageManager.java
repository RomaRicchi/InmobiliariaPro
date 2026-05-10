package com.roma.inmobiliariapro.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.roma.inmobiliariapro.data.model.UiMessage;

public class MessageManager {
    private static final MutableLiveData<UiMessage> uiMessageMutable = new MutableLiveData<>();

    public static LiveData<UiMessage> getUiMessageMutable() {
        return uiMessageMutable;
    }

    public static void send(UiMessage msg) {
        uiMessageMutable.postValue(msg);
    }
}
