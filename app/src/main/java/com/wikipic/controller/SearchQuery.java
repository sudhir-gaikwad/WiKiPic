package com.wikipic.controller;


import com.wikipic.util.Constants;

public class SearchQuery {

    private int mThumbnailSize = Constants.SEARCH_URL_VALUE.URL_VALUE_THUMBSIZE;
    private int mPageLimit = Constants.SEARCH_URL_VALUE.URL_VALUE_PILIMIT;
    private int mImageLimit = Constants.SEARCH_URL_VALUE.URL_VALUE_GPSLIMIT;
    private String mSearchString = "";

    public int getThumbnailSize() {
        return mThumbnailSize;
    }

    public void setThumbnailSize(int thumbnailSize) {
        mThumbnailSize = thumbnailSize;
    }

    public int getPageLimit() {
        return mPageLimit;
    }

    public void setPageLimit(int pageLimit) {
        mPageLimit = pageLimit;
    }

    public int getImageLimit() {
        return mImageLimit;
    }

    public void setImageLimit(int imageLimit) {
        mImageLimit = imageLimit;
    }

    public String getSearchString() {
        return mSearchString;
    }

    public void setSearchString(String searchString) {
        mSearchString = searchString;
    }
}
