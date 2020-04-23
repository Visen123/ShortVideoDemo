package com.tencent.liteav.demo.videoediter.bubble;


import com.tencent.liteav.demo.videoediter.bubble.ui.bubble.TCBubbleViewParams;

/**
 * Created by hans on 2017/10/25.
 * <p>
 * 用于保存 气泡字幕控件相关参数的类
 * <p>
 * 主要是用于恢复编辑字幕的场景
 */
public class TCBubbleViewInfo {
    private float viewCenterX, viewCenterY;// 控件的x y
    private float imageRotation;
    private TCBubbleViewParams viewParams; //气泡字幕配置参数
    private long startTime, endTime;
    private float scale;


    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getViewCenterX() {
        return viewCenterX;
    }

    public void setViewCenterX(float viewCenterX) {
        this.viewCenterX = viewCenterX;
    }

    public float getViewCenterY() {
        return viewCenterY;
    }

    public void setViewCenterY(float viewCenterY) {
        this.viewCenterY = viewCenterY;
    }

    public float getRotation() {
        return imageRotation;
    }

    public void setRotation(float viewRotation) {
        this.imageRotation = viewRotation;
    }

    public TCBubbleViewParams getViewParams() {
        return viewParams;
    }

    public void setViewParams(TCBubbleViewParams bubbleInfo) {
        this.viewParams = bubbleInfo;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
