package com.wikipic.model;

import com.google.gson.annotations.SerializedName;

public class Query {

    @SerializedName("pages")
    private Pages[] mPages;

    public Pages[] getPages() {
        return mPages;
    }

    public void setPages(Pages[] pages) {
        mPages = pages;
    }

    @Override
    public String toString() {
        return "Query [mPages = " + mPages + "]";
    }
}
