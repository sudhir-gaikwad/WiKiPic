package com.wikipic.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class Thumbnail {

    @SerializedName("height")
    private String mHeight;

    @SerializedName("source")
    private String mSource;

    @SerializedName("width")
    private String mWidth;

    public String getHeight() {
        return mHeight;
    }

    public void setHeight(String height) {
        mHeight = height;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String mSource) {
        mSource = mSource;
    }

    public String getWidth() {
        return mWidth;
    }

    public void setWidth(String mWidth) {
        mWidth = mWidth;
    }

    public boolean isValidSourceUrl() {
        if (TextUtils.isEmpty(mSource)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Thumbnail [mHeight = " + mHeight + ", mSource = " + mSource
                + ", mWidth = " + mWidth + "]";
    }
}
