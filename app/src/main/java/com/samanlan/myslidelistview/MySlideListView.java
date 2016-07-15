package com.samanlan.myslidelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.zip.DeflaterOutputStream;

/**
 * 作者： SamanLan on 2016/7/14.
 */
public class MySlideListView extends ListView {
    public MySlideListView(Context context) {
        super(context);
    }

    public MySlideListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    float startY,startX;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // System.out.println("dispatchHoverEvent");
        return super.dispatchTouchEvent(ev);
    }

    MySlideViewGroup v;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
         System.out.println("listview是否拦截触发");
        if (ev.getAction()==MotionEvent.ACTION_DOWN){

            if (v!=null&&v.isOpen){
                // 根据子item是否打开和点击的点击的位置，决定是否拦截事件，哪里可以点击
                if (pointToPosition((int) ev.getX(), (int) ev.getY()) - getFirstVisiblePosition()==pointToPosition((int)startX, (int)startY) - getFirstVisiblePosition()) {
                        if (v.getScrollX()>0){
                            if (ev.getX()<v.getWidth()-v.getScrollX())
                                return true;
                            else
                                v.close();
                        }else if (v.getScrollX()<0){
                            if (ev.getX()>-v.getScrollX())
                                return true;
                            else
                                v.close();
                        }
                }else {
                    return true;
                }
            }
            startX=ev.getX();
            startY=ev.getY();
            // 根据点击的坐标判断是哪一个Item
            v= (MySlideViewGroup) (getChildAt(pointToPosition((int) ev.getX(), (int) ev.getY()) - getFirstVisiblePosition()));
        }
        if (ev.getAction()==MotionEvent.ACTION_MOVE){
            if (v.isOpen){
                 System.out.println("因为view打开了，所以不拦截listview");
                return false;
            }
            else {
            if (Math.abs(ev.getX()-startX)>Math.abs(ev.getY()-startY)) {
                 System.out.println("因为是水平，所以不拦截");
                return false;
            }
            else
                 System.out.println("因为是垂直，所以拦截");
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction()==MotionEvent.ACTION_DOWN){
            if (v!=null&&v.isOpen){
                v.close();
                return true;
            }
        }
        return super.onTouchEvent(ev);
    }
}
