package com.wikipic.com.wikipic.request;

import com.google.gson.annotations.SerializedName;

public class BaseErrorResponse {

    @SerializedName("ErrorCode")
    private String mErrorCode = "";

    @SerializedName("ErrorDescription")
    private String mErrorDescription = "";

    public String getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(String mErrorCode) {
        this.mErrorCode = mErrorCode;
    }

    public String getErrorDescription() {
        return mErrorDescription;
    }

    public void setErrorDescription(String mErrorDescription) {
        this.mErrorDescription = mErrorDescription;
    }
}
