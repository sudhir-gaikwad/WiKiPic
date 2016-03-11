package com.wikipic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wikipic.R;
import com.wikipic.controller.ControllerCallback;
import com.wikipic.controller.ControllerManager;
import com.wikipic.controller.SearchQuery;
import com.wikipic.model.Images;
import com.wikipic.model.Pages;
import com.wikipic.model.Query;
import com.wikipic.util.Constants;
import com.wikipic.util.LogUtil;
import com.wikipic.util.NetworkUtils;
import com.wikipic.util.PageComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    // Id used to cancel the request.
    private int mRequestId = -1;

    /**
     * Time to wait before next request. Currently 500 milliseconds.
     */
    private static final long TIME_TO_WAIT_BEFORE_NEXT_REQUEST = 500;

    private int mImageSize = 0;
    private int mImageSpacing = 0;

    private EditText mSearchView = null;
    private ProgressBar mProgressBar = null;
    private View mGridLayout = null;
    private View mEmptyLayout = null;
    private GridView mGridView = null;
    private TextView mNetworkError = null;

    private GridViewAdapter mGridViewAdapter = null;
    private RequestHandler mRequestHandler = new RequestHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Retrieve fixed image size and space between the images.
        mImageSize = getResources().getDimensionPixelSize(R.dimen.image_size);
        mImageSpacing = getResources().getDimensionPixelSize(R.dimen.image_spacing);

        initializeViews();

        mGridViewAdapter = new GridViewAdapter(this, R.layout.grid_item, new ArrayList<Pages>());
        mGridView.setAdapter(mGridViewAdapter);

        if (!NetworkUtils.isConnected(this)) {
            showNetworkError();
        }
    }

    private void initializeViews() {
        mGridView = (GridView) findViewById(R.id.grid_view);
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new GlobalLayoutListener());
        mGridView.setOnItemClickListener(this);

        mSearchView = (EditText) findViewById(R.id.search_view);
        mSearchView.addTextChangedListener(mTextWatcher);
        mSearchView.setOnEditorActionListener(new EditorActionListener());

        mGridLayout = findViewById(R.id.image_container);
        mEmptyLayout = findViewById(R.id.empty_container);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mNetworkError = (TextView) findViewById(R.id.network_error);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            //LogUtil.d(TAG, "Text changed: " + editable.toString());

            // Stop progress.
            stopProgress();

            // Remove previous message from queue.
            mRequestHandler.removeMessages(RequestHandler.SEARCH_IMAGES);

            // Do not proceed if no network
            if (!NetworkUtils.isConnected(HomeActivity.this)) {
                showNetworkError();
                return;
            }

            hideNetworkError();

            /*
             * Make delayed request. Just wait for TIME_TO_WAIT_BEFORE_NEXT_REQUEST before
             * making actual request. This will reduce the number for request count
             * while user is typing.
             */
            mRequestHandler.sendEmptyMessageDelayed(RequestHandler.SEARCH_IMAGES,
                    TIME_TO_WAIT_BEFORE_NEXT_REQUEST);
        }
    };

    private class RequestHandler extends Handler {
        public static final int SEARCH_IMAGES = 100;

        public void handleMessage(Message msg) {
            switch (msg.what) {

                case SEARCH_IMAGES:
                    // Show progress, request is being initiated.
                    startProgress();

                    // Cancel the previous request.
                    ControllerManager.getInstance().cancelRequest(mRequestId);

                    // Initiate network request
                    SearchQuery searchQuery = new SearchQuery();
                    searchQuery.setSearchString(mSearchView.getText().toString().trim());
                    mRequestId = ControllerManager.getInstance().getSearchController().
                            makeRequest(searchQuery, mCallback);
                    break;
            }
        }
    };

    /**
     * Images from server is received.
     * 1. Stop progress bar
     * 2. Sort the result based on index.
     * 3. Set the result to adapter
     * 4. Make grid view visible.
     *
     * If result is empty hide the grid view.
     *
     * @param images Image list
     */
    private void onImagesReceived(Images images) {
        // Result received, stop progress.
        stopProgress();

        if (images == null) {
            onNoResultsFound();
            return;
        }

        Query query = images.getQuery();
        if (query == null) {
            onNoResultsFound();
            return;
        }

        Pages[] qPages = query.getPages();
        if (qPages == null) {
            onNoResultsFound();
            return;
        }

        // Sort result based on index of images.
        List<Pages> pages = Arrays.asList(qPages);
        Collections.sort(pages, new PageComparator());

        mGridViewAdapter.setItems(pages);
        mGridViewAdapter.notifyDataSetChanged();

        // Move grid view to top
        mGridView.post(new Runnable() {
            @Override
            public void run() {
                mGridView.smoothScrollToPosition(0);
            }
        });

        showGridView();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Pages page = mGridViewAdapter.getItem(i);
        if (!page.hasValidThumnail()) {
            return;
        }

        Intent intent = new Intent(HomeActivity.this, ImageActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_IMAGE_URL, page.getThumbnail().getSource());
        intent.putExtra(Constants.INTENT_EXTRA_IMAGE_TITLE, page.getTitle());
        startActivity(intent);

        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }

    protected ControllerCallback mCallback = new ControllerCallback(this) {

        @Override
        public void onErrorResponse(int requestId, int status, Object data) {
            LogUtil.e(TAG, "Image search request failed");
            Images images = (Images) data;
            LogUtil.e(TAG, "ErrorCode: " + images.getErrorCode()
                    + " Description: " + images.getErrorDescription());
            onImagesReceived(null);
        }

        @Override
        public void onSuccessResponse(int requestId, int status, Object data) {
            LogUtil.d(TAG, "Image search request success");
            Images images = (Images) data;
            onImagesReceived(images);
        }
    };

    /**
     * Calculate the number of columns and image height.
     * This will dynamically adjust the number of columns on screen.
     */
    private class GlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
            if (mGridViewAdapter.getNumColumns() == 0) {
                int numColumns = (int) Math.floor(mGridView.getWidth() / (mImageSize + mImageSpacing));
                if (numColumns > 0) {
                    final int columnWidth = (mGridView.getWidth() / numColumns) - mImageSpacing;
                    mGridViewAdapter.setNumColumns(numColumns);
                    mGridViewAdapter.setItemHeight(columnWidth);
                }
            }
        }
    };

    private void showGridView() {
        mEmptyLayout.setVisibility(View.GONE);
        mNetworkError.setVisibility(View.INVISIBLE);
        mGridLayout.setVisibility(View.VISIBLE);
    }

    private void showEmptyView() {
        mGridLayout.setVisibility(View.GONE);
        mNetworkError.setVisibility(View.INVISIBLE);
        mEmptyLayout.setVisibility(View.VISIBLE);
    }

    private void startProgress() {
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void stopProgress() {
        mProgressBar.setIndeterminate(false);
        // set progress as max to display it as a line
        mProgressBar.setProgress(100);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void showNetworkError() {
        showEmptyView();

        mNetworkError.setText(getResources().getString(R.string.network_error));
        mNetworkError.setVisibility(View.VISIBLE);
    }

    public void onNoResultsFound() {
        mGridViewAdapter.setItems(new ArrayList<Pages>());
        mGridViewAdapter.notifyDataSetChanged();

        showEmptyView();

        if (!TextUtils.isEmpty(mSearchView.getText())) {
            mNetworkError.setText(getResources().getString(R.string.no_results_found));
            mNetworkError.setVisibility(View.VISIBLE);
        }
    }

    private void hideNetworkError() {
        mNetworkError.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Remove previous message from queue.
        mRequestHandler.removeMessages(RequestHandler.SEARCH_IMAGES);

        // Cancel the previous request.
        ControllerManager.getInstance().cancelRequest(mRequestId);
    }
}
