package com.wikipic.util;

public class Constants {

    public static final String BASE_URL = "https://en.wikipedia.org/w/api.php?";
    public static final String INTENT_EXTRA_IMAGE_URL = "image_url";
    public static final String INTENT_EXTRA_IMAGE_TITLE = "image_title";

    public static class SEARCH_URL_KEY {
        public static final String URL_KEY_ACTION = "action";
        public static final String URL_KEY_FORMAT = "format";
        public static final String URL_KEY_PROP = "prop";
        public static final String URL_KEY_GENERATOR = "generator";
        public static final String URL_KEY_PIPROP = "piprop";
        public static final String URL_KEY_THUMBSIZE = "pithumbsize";
        public static final String URL_KEY_PILIMIT = "pilimit";
        public static final String URL_KEY_GPSSEARCH = "gpssearch";
        public static final String URL_KEY_GPSLIMIT = "gpslimit";
        public static final String URL_KEY_FORMAT_VERSION = "formatversion";
    }

    public static class SEARCH_URL_VALUE {
        public static final String URL_VALUE_ACTION = "query";
        public static final String URL_VALUE_FORMAT = "json";
        public static final String URL_VALUE_PROP = "pageimages";
        public static final String URL_VALUE_GENERATOR = "prefixsearch";
        public static final String URL_VALUE_PIPROP = "thumbnail";
        public static final int URL_VALUE_THUMBSIZE = 500;
        public static final int URL_VALUE_PILIMIT = 50;
        //public static final String URL_VALUE_GPSSEARCH = "";
        public static final int URL_VALUE_GPSLIMIT = 50;
        public static final String URL_VALUE_FORMAT_VERSION = "latest";
    }

    public static class HTTP_CONSTANTS {
        public static final String HEADER_ACCEPT_LANGUAGE_KEY = "Accept-Language";
        public static final String HEADER_ACCEPT_LANGUAGE_VALUE = "en-US,en;q=0.5";
    }
}
