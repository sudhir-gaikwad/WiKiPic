package com.wikipic.model;

import com.google.gson.annotations.SerializedName;

public class Continue {

    @SerializedName("gpsoffset")
    private String mGpsoffset;

    @SerializedName("continue")
    private String mContinue;

    public String getGpsoffset() {
        return mGpsoffset;
    }

    public void setGpsoffset(String gpsoffset) {
        mGpsoffset = gpsoffset;
    }

    public String getContinue() {
        return mContinue;
    }

    public void setContinue(String continue1) {
        mContinue = continue1;
    }

    @Override
    public String toString() {
        return "Continue [mGpsoffset = " + mGpsoffset + ", continue = " + mContinue + "]";
    }
}
