package com.tencent.liteav.demo.videoediter.bubble.utils;

/**
 * Created by hans on 2017/10/23.
 * <p>
 * {@link TCBubbleManager}
 * 保存气泡字幕解析出来的类
 */
public class TCBubbleInfo {
    private String bubblePath;
    private String iconPath;
    private int width, height;
    private int defaultSize;
    private float top, left, right, bottom;

    public TCBubbleInfo() {
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getBubblePath() {
        return bubblePath;
    }

    public void setBubblePath(String bubblePath) {
        this.bubblePath = bubblePath;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getDefaultSize() {
        return defaultSize;
    }

    public void setDefaultSize(int defaultSize) {
        this.defaultSize = defaultSize;
    }


    public void setRect(float top, float left, float right, float bottom) {
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }
}
