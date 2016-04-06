package com.wikipic.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
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
import com.wikipic.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnItemClickListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    // Id used to cancel the request.
    private int mRequestId = -1;

    /**
     * Time to wait before next request. Currently 500 milliseconds.
     */
    private static final long TIME_TO_WAIT_BEFORE_NEXT_REQUEST = 500;

    private static final String SEARCH_QUERY = "SEARCH_QUERY";
    private static final String IMAGE_LIST = "IMAGE_LIST";
    private static final String RECYCLER_VIEW_POSITION = "RECYCLER_VIEW_POSITION";
    private static final String RECYCLER_VIEW_OFFSET = "RECYCLER_VIEW_OFFSET";

    private int mImageSize = 0;
    private int mImageSpacing = 0;

    private EditText mSearchView = null;
    private ProgressBar mProgressBar = null;
    private View mGridLayout = null;
    private View mEmptyLayout = null;
    private RecyclerView mRecyclerView = null;
    private TextView mNetworkError = null;
    private String mLastSearch = null;

    private RecyclerViewAdapter mRecyclerViewAdapter = null;
    private RequestHandler mRequestHandler = new RequestHandler();
    private GridLayoutManager mGridLayoutManager = null;
    private Images mImageResult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Retrieve fixed image size and space between the images.
        mImageSize = getResources().getDimensionPixelSize(R.dimen.image_size);
        mImageSpacing = getResources().getDimensionPixelSize(R.dimen.image_spacing);

        initializeViews();

        if (!NetworkUtils.isConnected(this)) {
            showNetworkError();
        }

        // Orientation changed, then restore the last state
        if (savedInstanceState != null) {
            Utils.hideKeyboard(mSearchView, this);
            mLastSearch = savedInstanceState.getString(SEARCH_QUERY);
            mImageResult = (Images) savedInstanceState.getSerializable(IMAGE_LIST);
            final int pos = savedInstanceState.getInt(RECYCLER_VIEW_POSITION);
            final int offset = savedInstanceState.getInt(RECYCLER_VIEW_OFFSET);

            onImagesReceived(mImageResult);

            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mGridLayoutManager.scrollToPositionWithOffset(pos, offset);
                }
            });
        }
    }

    private void initializeViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.grid_view);
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new GlobalLayoutListener());
        mGridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.hasFixedSize();
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.addItemDecoration(new ImageItemDecoration(mImageSpacing));

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

            // Return if search query is same
            if (mLastSearch != null) {
                if (mLastSearch.equals(mSearchView.getText().toString())) {
                    mSearchView.clearFocus();
                    return;
                }
            }

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


                    mLastSearch = mSearchView.getText().toString();

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

        mImageResult = images;

        if (mRecyclerViewAdapter == null) {
            mRecyclerViewAdapter = new RecyclerViewAdapter(pages, this, this);
            mRecyclerView.setAdapter(mRecyclerViewAdapter);
        } else {
            mRecyclerViewAdapter.setItems(pages);

            // Move grid view to top
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.scrollToPosition(0);
                }
            });

        }

        showGridView();
    }

    @Override
    public void onItemClick(int i) {
        Pages page = mRecyclerViewAdapter.getItem(i);
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
            if (mRecyclerViewAdapter == null) {
                return;
            }

            int numColumns = (int) Math.floor(mRecyclerView.getWidth() / (mImageSize + mImageSpacing));
            if (numColumns == 0) {
                return;
            }

            final int columnWidth = (mRecyclerView.getWidth() / numColumns) - mImageSpacing;
            mRecyclerViewAdapter.setNumColumns(numColumns);
            mRecyclerViewAdapter.setItemHeight(columnWidth);

            mGridLayoutManager.setSpanCount(numColumns);
            mGridLayoutManager.requestLayout();
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
        mImageResult = null;

        if (mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.setItems(new ArrayList<Pages>());
        }

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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Utils.hideKeyboard(mSearchView, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Remove previous message from queue.
        mRequestHandler.removeMessages(RequestHandler.SEARCH_IMAGES);

        // Cancel the previous request.
        ControllerManager.getInstance().cancelRequest(mRequestId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save last state before orientation change
        outState.putString(SEARCH_QUERY, mLastSearch);
        outState.putSerializable(IMAGE_LIST, mImageResult);
        outState.putInt(RECYCLER_VIEW_POSITION, mGridLayoutManager.findFirstVisibleItemPosition());
        View view = mGridLayoutManager.findViewByPosition(mGridLayoutManager.findFirstVisibleItemPosition());
        if (view != null) {
            outState.putInt(RECYCLER_VIEW_OFFSET, view.getTop());
        }
    }
}
