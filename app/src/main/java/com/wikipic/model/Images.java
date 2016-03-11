package com.wikipic.model;

import com.google.gson.annotations.SerializedName;
import com.wikipic.com.wikipic.request.BaseErrorResponse;

public class Images extends BaseErrorResponse {

    @SerializedName("query")
    private Query mQuery;

    @SerializedName("continue")
    private Continue mContinue;

    @SerializedName("batchcomplete")
    private String mBatchcomplete;

    public Query getQuery() {
        return mQuery;
    }

    public void setQuery(Query query) {
        mQuery = query;
    }

    public Continue getContinue() {
        return mContinue;
    }

    public void setContinue(Continue continue1) {
        mContinue = continue1;
    }

    public String getBatchcomplete() {
        return mBatchcomplete;
    }

    public void setBatchcomplete(String batchcomplete) {
        mBatchcomplete = batchcomplete;
    }

    @Override
    public String toString() {
        return "Images [mQuery = " + mQuery + ", continue = " + mContinue
                + ", mBatchcomplete = " + mBatchcomplete + "]";
    }
}
