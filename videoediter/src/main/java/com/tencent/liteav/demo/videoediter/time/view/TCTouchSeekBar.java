package com.tencent.liteav.demo.videoediter.time.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.tencent.liteav.demo.videoediter.R;

import java.util.ArrayList;

/**
 * Created by hans on 2017/10/17.
 */
public class TCTouchSeekBar extends View {
    private int downX = 0;
    private int downY = 0;
    private int upX = 0;
    private int upY = 0;
    private int moveX = 0;
    private int moveY = 0;
    // 控件宽高
    private int mViewWidth, mViewHeight;
    //刻度
    private Bitmap mDotDefaultBitmap, mDotCheckedBitmap;
    private OnTouchCallback mCallback;
    //焦点位置
    private int mCurrentPos = 2;
    //刻度文字
    private ArrayList<String> mSelectionList;
    //刻度单位长度，按钮单位长度
    private int mUnitWidth;
    //画笔
    private Paint mPaint;
    //文字大小
    private int mTextSize = 40;

    public TCTouchSeekBar(Context context) {
        super(context);
    }

    public TCTouchSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TCTouchSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TCTouchSeekBar);
            mDotDefaultBitmap = ((BitmapDrawable) a.getDrawable(R.styleable.TCTouchSeekBar_tsb_dotDefault)).getBitmap();
            mDotCheckedBitmap = ((BitmapDrawable) a.getDrawable(R.styleable.TCTouchSeekBar_tsb_dotChecked)).getBitmap();
            a.recycle();
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true); // 去锯齿
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(mTextSize);

        setSelectionList(null);
    }

    /**
     * 实例化后调用，设置bar的段数和文字
     */
    public void setSelectionList(String[] section) {
        if (section != null) {
            mSelectionList = new ArrayList<String>();
            for (int i = 0; i < section.length; i++) {
                mSelectionList.add(section[i]);

            }
        } else {
            //随便写的
            String[] str = new String[]{"低", "中", "高"};
            mSelectionList = new ArrayList<String>();
            for (int i = 0; i < str.length; i++) {
                mSelectionList.add(str[i]);

            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mViewWidth, mViewHeight);
        //计算一个刻度的长度
        mUnitWidth = (mViewWidth - mDotDefaultBitmap.getWidth()) / (mSelectionList.size() - 1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //先画一个背景 X轴横线
        canvas.drawLine(mDotDefaultBitmap.getWidth() / 2, mViewHeight / 2, mViewWidth - mDotDefaultBitmap.getWidth() / 2, mViewHeight / 2, mPaint);

        //画刻度点和刻度
        for (int i = 0; i < mSelectionList.size(); i++) {
            if (i == mCurrentPos) {
                canvas.drawBitmap(mDotCheckedBitmap,
                        mCurrentPos * mUnitWidth - (mDotCheckedBitmap.getWidth() - mDotDefaultBitmap.getWidth()) / 2,
                        mViewHeight / 2 - mDotCheckedBitmap.getHeight() / 2, mPaint);
            } else {
                canvas.drawBitmap(mDotDefaultBitmap, i * mUnitWidth, mViewHeight / 2 - mDotDefaultBitmap.getHeight() / 2, mPaint);
            }
            canvas.drawText(mSelectionList.get(i), i * mUnitWidth + (mDotDefaultBitmap.getWidth() - mTextSize / 2 * mSelectionList.get(i).length()) / 2, mViewHeight / 2 - mDotDefaultBitmap.getHeight() / 2 - 5, mPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                responseTouch(downX, downY);
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = (int) event.getX();
                moveY = (int) event.getY();
                responseTouch(moveX, moveY);
                break;
            case MotionEvent.ACTION_UP:
                upX = (int) event.getX();
                upY = (int) event.getY();
                responseTouch(upX, upY);
                mCallback.onCallback(mCurrentPos);
                break;
        }
        return true;
    }

    /**
     * 刷新点
     *
     * @param x
     * @param y
     */
    private void responseTouch(int x, int y) {
        if (x <= 0)
            mCurrentPos = 0;
        else if (x % mUnitWidth >= mUnitWidth / 2)
            mCurrentPos = x / mUnitWidth + 1;
        else
            mCurrentPos = x / mUnitWidth;

        invalidate();
    }

    //设置监听
    public void setOnTouchCallback(OnTouchCallback callback) {
        mCallback = callback;
    }

    //设置进度
    public void setProgress(int progress) {
        if (progress < 0)
            mCurrentPos = 0;
        else if (progress > mSelectionList.size() - 1)
            mCurrentPos = mSelectionList.size() - 1;
        else
            mCurrentPos = progress;
        invalidate();
    }

    //获取进度
    public int getProgress() {
        return mCurrentPos;
    }

    public interface OnTouchCallback {
        void onCallback(int position);
    }
}
