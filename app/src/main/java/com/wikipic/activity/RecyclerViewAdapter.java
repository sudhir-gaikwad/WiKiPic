package com.wikipic.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wikipic.R;
import com.wikipic.model.Pages;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Context mContext = null;
    private List<Pages> mPages = new ArrayList<Pages>();

    private int mItemHeight = 0;
    private int mNumColumns = 0;
    private RelativeLayout.LayoutParams mImageViewLayoutParams = null;
    private OnItemClickListener mItemClickListener = null;

    public RecyclerViewAdapter(List<Pages> pages, Context context, OnItemClickListener itemClickListener) {
        mPages = pages;
        mContext = context;
        mItemClickListener = itemClickListener;

        mImageViewLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, null);
        ViewHolder holder = new ViewHolder(view, mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pages item = getItem(position);

        holder.mTitleView.setText(Html.fromHtml(item.getTitle()));
        holder.mImageView.setLayoutParams(mImageViewLayoutParams);

        // Check the height matches our calculated column width
        if (holder.mImageView.getLayoutParams().height != mItemHeight) {
            holder.mImageView.setLayoutParams(mImageViewLayoutParams);
        }

        if (item.hasValidThumnail()) {
            Glide.with(mContext)
                    .load(item.getThumbnail().getSource())
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .animate(R.anim.zoom_anim)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.mImageView);
        } else {
            // Set placeholder if no valid image url found
            holder.mImageView.setImageResource(R.drawable.image_placeholder);
        }
    }

    public Pages getItem(int position) {
        return mPages.get(position);
    }

    @Override
    public int getItemCount() {
        return mPages.size();
    }

    public void setItems(List<Pages> pages) {
        mPages = pages;
        notifyDataSetChanged();
    }

    // Set number of columns to display
    public void setNumColumns(int numColumns) {
        mNumColumns = numColumns;
    }

    public int getNumColumns() {
        return mNumColumns;
    }

    // Set image height
    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        mImageViewLayoutParams = new RelativeLayout.LayoutParams(
                GridLayout.LayoutParams.MATCH_PARENT, mItemHeight);
        notifyDataSetChanged();
    }
}
