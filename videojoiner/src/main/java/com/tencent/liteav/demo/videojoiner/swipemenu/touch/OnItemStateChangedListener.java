
package com.tencent.liteav.demo.videojoiner.swipemenu.touch;

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface OnItemStateChangedListener {

    /**
     * ItemTouchHelper is in idle state. At this state, either there is no related ic_motion event by
     * the user or latest ic_motion events have not yet triggered a swipe or drag.
     */
    int ACTION_STATE_IDLE = ItemTouchHelper.ACTION_STATE_IDLE;

    /**
     * A View is currently being swiped.
     */
    int ACTION_STATE_SWIPE = ItemTouchHelper.ACTION_STATE_SWIPE;

    /**
     * A View is currently being dragged.
     */
    int ACTION_STATE_DRAG = ItemTouchHelper.ACTION_STATE_DRAG;

    /**
     * Called when the ViewHolder swiped or dragged by the ItemTouchHelper is changed.
     *
     * @param viewHolder  The new ViewHolder that is being swiped or dragged. Might be null if it is cleared.
     * @param actionState One of {@link OnItemStateChangedListener#ACTION_STATE_IDLE},
     *                    {@link OnItemStateChangedListener#ACTION_STATE_SWIPE} or
     *                    {@link OnItemStateChangedListener#ACTION_STATE_DRAG}.
     */
    void onSelectedChanged(RecyclerView.ViewHolder viewHolder, @ActionStateMode int actionState);

    @IntDef({ACTION_STATE_IDLE, ACTION_STATE_SWIPE, ACTION_STATE_DRAG})
    @Retention(RetentionPolicy.SOURCE)
    @interface ActionStateMode {
    }
}
