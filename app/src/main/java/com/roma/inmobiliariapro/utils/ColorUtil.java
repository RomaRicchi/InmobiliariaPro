package com.roma.inmobiliariapro.utils;

import androidx.annotation.ColorRes;

import com.roma.inmobiliariapro.R;
import com.roma.inmobiliariapro.data.model.Status;

public class ColorUtil {

    @ColorRes
    public static int getColorByStatus(Status status) {

        switch (status) {

            case SUCCESS:
                return R.color.success;

            case ERROR:
                return R.color.error;

            case WARNING:
                return R.color.warning;

            case INFO:
            default:
                return R.color.info;
        }
    }
}
