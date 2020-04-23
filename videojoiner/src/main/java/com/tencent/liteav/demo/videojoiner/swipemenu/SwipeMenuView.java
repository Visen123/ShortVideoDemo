package com.tencent.liteav.demo.videojoiner.swipemenu;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class SwipeMenuView extends LinearLayout {

    private SwipeSwitch mSwipeSwitch;

    private RecyclerView.ViewHolder mAdapterVIewHolder;

    private int mDirection;

    private OnSwipeMenuItemClickListener mItemClickListener;

    public SwipeMenuView(Context context) {
        this(context, null);
    }

    public SwipeMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bindMenu(SwipeMenu swipeMenu, int direction) {
        removeAllViews();
        this.mDirection = direction;
        List<SwipeMenuItem> items = swipeMenu.getMenuItems();
        int index = 0;
        for (SwipeMenuItem item : items) {
            addItem(item, index++);
        }
    }

    public void bindMenuItemClickListener(OnSwipeMenuItemClickListener swipeMenuItemClickListener, SwipeSwitch swipeSwitch) {
        this.mItemClickListener = swipeMenuItemClickListener;
        this.mSwipeSwitch = swipeSwitch;
    }

    public void bindAdapterViewHolder(RecyclerView.ViewHolder adapterVIewHolder) {
        this.mAdapterVIewHolder = adapterVIewHolder;
    }

    private void addItem(SwipeMenuItem item, int index) {
        LayoutParams params = new LayoutParams(item.getWidth(), item.getHeight());
        params.weight = item.getWeight();
        LinearLayout parent = new LinearLayout(getContext());
        parent.setId(index);
        parent.setGravity(Gravity.CENTER);
        parent.setOrientation(VERTICAL);
        parent.setLayoutParams(params);
        ResCompat.setBackground(parent, item.getBackground());
        parent.setOnClickListener(mMenuClickListener);
        addView(parent);

        if (item.getImage() != null)
            parent.addView(createIcon(item));

        if (!TextUtils.isEmpty(item.getText()))
            parent.addView(createTitle(item));
    }

    private ImageView createIcon(SwipeMenuItem item) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(item.getImage());
        return imageView;
    }

    private TextView createTitle(SwipeMenuItem item) {
        TextView textView = new TextView(getContext());
        textView.setText(item.getText());
        textView.setGravity(Gravity.CENTER);
        int textSize = item.getTextSize();
        if (textSize > 0)
            textView.setTextSize(textSize);
        ColorStateList textColor = item.getTitleColor();
        if (textColor != null)
            textView.setTextColor(textColor);
        int textAppearance = item.getTextAppearance();
        if (textAppearance != 0)
            ResCompat.setTextAppearance(textView, textAppearance);
        Typeface typeface = item.getTextTypeface();
        if (typeface != null)
            textView.setTypeface(typeface);
        return textView;
    }

    private OnClickListener mMenuClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mItemClickListener != null && mSwipeSwitch != null && mSwipeSwitch.isMenuOpen()) {
                mItemClickListener.onItemClick(mSwipeSwitch, mAdapterVIewHolder.getAdapterPosition(), v.getId(), mDirection);
            }
        }
    };
}