package com.wikipic.activity;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wikipic.R;
import com.wikipic.model.Pages;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends ArrayAdapter<Pages> {

    private Context mContext = null;
    private int mLayoutResourceId;
    private List<Pages> mPages = new ArrayList<Pages>();

    private int mItemHeight = 0;
    private int mNumColumns = 0;
    private RelativeLayout.LayoutParams mImageViewLayoutParams = null;
    private LayoutInflater mInflater = null;

    public GridViewAdapter(Context context, int layoutResourceId, List<Pages> pages) {
        super(context, layoutResourceId, pages);
        mLayoutResourceId = layoutResourceId;
        mContext = context;
        mPages = pages;

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageViewLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void setItems(List<Pages> pages) {
        mPages = pages;
    }

    @Override
    public int getCount() {
        return mPages.size();
    }

    @Override
    public Pages getItem(int position) {
        return mPages.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.mTitleView = (TextView) convertView.findViewById(R.id.country_name);
            holder.mImageView = (ImageView) convertView.findViewById(R.id.country_photo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

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

        return convertView;
    }

    private static class ViewHolder {
        TextView mTitleView;
        ImageView mImageView;
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
