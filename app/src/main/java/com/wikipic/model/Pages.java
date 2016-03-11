package com.wikipic.model;

import com.google.gson.annotations.SerializedName;

public class Pages {

    @SerializedName("index")
    private String mIndex;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("ns")
    private String mNs;

    @SerializedName("thumbnail")
    private Thumbnail mThumbnail;

    @SerializedName("pageid")
    private String mPageid;

    public String getIndex() {
        return mIndex;
    }

    public void setIndex(String index) {
        mIndex = index;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getNs() {
        return mNs;
    }

    public void setNs(String ns) {
        mNs = ns;
    }

    public Thumbnail getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        mThumbnail = thumbnail;
    }

    public String getPageId() {
        return mPageid;
    }

    public void setPageId(String pageid) {
        mPageid = pageid;
    }

    public boolean hasValidThumnail() {
        if ((mThumbnail != null) && (mThumbnail.isValidSourceUrl())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Pages [mIndex = " + mIndex + ", mTitle = " + mTitle + ", mNs = " + mNs
                + ", mThumbnail = " + mThumbnail + ", mPageid = " + mPageid + "]";
    }
}
