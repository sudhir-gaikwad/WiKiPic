package com.wikipic.controller;

import com.wikipic.com.wikipic.request.ImageListener;
import com.wikipic.com.wikipic.request.ImageRequest;
import com.wikipic.util.HttpMethods;

public class SearchController {

    private NetworkManager mNetworkManager = null;

    public SearchController(ControllerManager cm) {
        mNetworkManager = cm.getNetworkManager();
    }

    /**
     * Make search request.
     * @param query Query
     * @param callback listener
     * @return Request id.
     */
    public int makeRequest(SearchQuery query, ControllerCallback callback) {

        String url = UrlBuilder.build(query);

        ControllerManager controllerMgr = ControllerManager.getInstance();
        int requestId = controllerMgr.getNextRequestId();
        controllerMgr.addListener(requestId, callback);

        ImageListener listener = new ImageListener(requestId);
        ImageRequest request = new ImageRequest(HttpMethods.GET, url, listener, listener);

        mNetworkManager.execute(request, requestId);

        return requestId;
    }
}
