package com.roma.inmobiliariapro.utils;

import com.roma.inmobiliariapro.data.model.Status;

public class FieldValidation {
    private final String fieldName;
    private final Status status;

    public FieldValidation(String fieldName, Status status) {
        this.fieldName = fieldName;
        this.status = status;
    }

    public String getFieldName() { return fieldName; }
    public Status getStatus() { return status; }
}

