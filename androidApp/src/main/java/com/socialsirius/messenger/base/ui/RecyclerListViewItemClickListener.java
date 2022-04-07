package com.socialsirius.messenger.base.ui;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


public class RecyclerListViewItemClickListener implements RecyclerView.OnItemTouchListener {

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public interface OnItemClickListenerWithGesture {
        void onItemClickWithGesture(View view, MotionEvent e, int position);
    }

    private OnItemClickListener mListener;

    public void setmListenerWithGesture(OnItemClickListenerWithGesture mListenerWithGesture) {
        this.mListenerWithGesture = mListenerWithGesture;
    }

    private OnItemClickListenerWithGesture mListenerWithGesture;

    GestureDetector mGestureDetector;

    public RecyclerListViewItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null) {
            if (mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildLayoutPosition(childView));
            }
            if ( mListenerWithGesture != null && mGestureDetector.onTouchEvent(e)) {
                 mListenerWithGesture.onItemClickWithGesture(childView, e,view.getChildLayoutPosition(childView));
            }

                /*int action = e.getAction();
                if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
                    childView.setPressed(true);
                } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
                    childView.setPressed(false);
                }*/
        } else {
                /*int count = view.getChildCount();
                for (int a = 0; a < count; a++) {
                    view.getChildAt(a).setPressed(false);
                }*/
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
