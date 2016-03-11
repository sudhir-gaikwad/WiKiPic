package com.wikipic.controller;

import com.wikipic.util.Constants;

public class UrlBuilder {

    /**
     * Build actual url by combing all parameters.
     * @param query
     * @return
     */
    public static String build(SearchQuery query) {

        StringBuilder builder = new StringBuilder();
        builder.append(Constants.BASE_URL);

        builder.append(Constants.SEARCH_URL_KEY.URL_KEY_ACTION);
        builder.append("=");
        builder.append(Constants.SEARCH_URL_VALUE.URL_VALUE_ACTION);
        builder.append("&");

        builder.append(Constants.SEARCH_URL_KEY.URL_KEY_FORMAT);
        builder.append("=");
        builder.append(Constants.SEARCH_URL_VALUE.URL_VALUE_FORMAT);
        builder.append("&");

        builder.append(Constants.SEARCH_URL_KEY.URL_KEY_PROP);
        builder.append("=");
        builder.append(Constants.SEARCH_URL_VALUE.URL_VALUE_PROP);
        builder.append("&");

        builder.append(Constants.SEARCH_URL_KEY.URL_KEY_GENERATOR);
        builder.append("=");
        builder.append(Constants.SEARCH_URL_VALUE.URL_VALUE_GENERATOR);
        builder.append("&");

        builder.append(Constants.SEARCH_URL_KEY.URL_KEY_PIPROP);
        builder.append("=");
        builder.append(Constants.SEARCH_URL_VALUE.URL_VALUE_PIPROP);
        builder.append("&");

        builder.append(Constants.SEARCH_URL_KEY.URL_KEY_FORMAT_VERSION);
        builder.append("=");
        builder.append(Constants.SEARCH_URL_VALUE.URL_VALUE_FORMAT_VERSION);
        builder.append("&");

        builder.append(Constants.SEARCH_URL_KEY.URL_KEY_THUMBSIZE);
        builder.append("=");
        builder.append(query.getThumbnailSize());
        builder.append("&");

        builder.append(Constants.SEARCH_URL_KEY.URL_KEY_PILIMIT);
        builder.append("=");
        builder.append(query.getPageLimit());
        builder.append("&");

        builder.append(Constants.SEARCH_URL_KEY.URL_KEY_GPSLIMIT);
        builder.append("=");
        builder.append(query.getImageLimit());
        builder.append("&");

        builder.append(Constants.SEARCH_URL_KEY.URL_KEY_GPSSEARCH);
        builder.append("=");
        builder.append(query.getSearchString());

        return builder.toString();
    }
}
