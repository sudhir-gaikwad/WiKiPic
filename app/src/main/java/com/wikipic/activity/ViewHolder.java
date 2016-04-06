package com.wikipic.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wikipic.R;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView mTitleView;
    ImageView mImageView;
    private OnItemClickListener mItemClickListener = null;

    public ViewHolder(View view, OnItemClickListener clickListener) {
        super(view);
        mTitleView = (TextView) view.findViewById(R.id.country_name);
        mImageView = (ImageView) view.findViewById(R.id.country_photo);
        view.setOnClickListener(this);
        mItemClickListener = clickListener;
    }

    @Override
    public void onClick(View view) {
        mItemClickListener.onItemClick(getAdapterPosition());
    }
}
