package com.app.healthybee;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse {
    @SerializedName("valid")
    private boolean valid;
    @SerializedName("param")
    private String param;
    @SerializedName("message")
    private String message;

    public ErrorResponse() {
    }

    public boolean isValid() {
        return valid;
    }

    public String getParam() {
        return param;
    }

    public String getMessage() {
        return message;
    }
}
