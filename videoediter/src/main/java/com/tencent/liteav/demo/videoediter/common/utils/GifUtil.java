package com.tencent.liteav.demo.videoediter.common.utils;

import android.graphics.Bitmap;

import com.bumptech.glide.gifencoder.AnimatedGifEncoder;
import com.tencent.liteav.basic.log.TXCLog;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by vinsonswang on 2018/10/24.
 */

public class GifUtil {
    private static final String TAG = "GifUtil";
    public static String createGifByBitmaps(String filePath, List<Bitmap> bitmapList, int delayMs, int width, int height) throws IOException{
        if(bitmapList == null || bitmapList.size() == 0){
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
        gifEncoder.start(baos);
        gifEncoder.setRepeat(0);
        gifEncoder.setDelay(delayMs);
        TXCLog.i(TAG, "start make gif");
        for(Bitmap bitmap : bitmapList){
            Bitmap resizeBitmap = TCEditerUtil.zoomImg(bitmap, width, height);
            gifEncoder.addFrame(resizeBitmap);
        }
        gifEncoder.finish();
        TXCLog.i(TAG, "finish make gif");
        FileOutputStream fos = new FileOutputStream(filePath);
        baos.writeTo(fos);
        baos.flush();
        fos.flush();
        baos.close();
        fos.close();
        return filePath;
    }
}
