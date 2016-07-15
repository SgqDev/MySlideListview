package com.samanlan.myslidelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 作者： SamanLan on 2016/7/13.
 */
public class MySlideViewGroup extends ViewGroup {

    LayoutInflater layoutInflater;

    // 在一个事件序列中是否第一次移动
    boolean first=true;

    // 上下左右四个View的index
    int leftViewIndex,rightViewIndex,topViewIndex,bottomViewIndex,centerViewIndex;

    double startX,startY;

    // 上下左右四个View是否部分或者全部显示出来
    boolean leftIsExsit=false,rightIsExsit=false,topIsExsit=false,bottomIsExsit=false;

    // 是往什么方向滑动，true为水平,false为垂直
    boolean where;

    // 上下左右四个View是否能滑动
    boolean CanSlideUp=false,CanSlideDown=false,CanSlideLeft=false,CanSlideRight=false;

    // View是否已滑动打开
    boolean isOpen=false;

    public boolean isOpen() {
        return isOpen;
    }

    public int getCenterViewIndex() {
        return centerViewIndex;
    }

    public int getRightViewIndex() {

        return rightViewIndex;
    }

    public int getTopViewIndex() {
        return topViewIndex;
    }

    public int getBottomViewIndex() {
        return bottomViewIndex;
    }

    public int getLeftViewIndex() {
        return leftViewIndex;
    }

    public boolean isCanSlideUp() {
        return CanSlideUp;
    }

    public void setCanSlideUp(boolean canSlideUp) {
        CanSlideUp = canSlideUp;
    }

    public boolean isCanSlideDown() {
        return CanSlideDown;
    }

    public void setCanSlideDown(boolean canSlideDown) {
        CanSlideDown = canSlideDown;
    }

    public boolean isCanSlideLeft() {
        return CanSlideLeft;
    }

    public void setCanSlideLeft(boolean canSlideLeft) {
        CanSlideLeft = canSlideLeft;
    }

    public boolean isCanSlideRight() {
        return CanSlideRight;
    }

    public void setCanSlideRight(boolean canSlideRight) {
        CanSlideRight = canSlideRight;
    }

    public MySlideViewGroup(Context context) {
        super(context);
        layoutInflater=LayoutInflater.from(context);
        setClickable(true);
    }

    public MySlideViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        layoutInflater=LayoutInflater.from(context);
        setClickable(true);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept=false;
        if (ev.getAction()==MotionEvent.ACTION_DOWN) {
            startX = ev.getX();
            startY= ev.getY();
        }
        if (ev.getAction()==MotionEvent.ACTION_MOVE) {
            intercept = true;
        }
        return intercept;
    }

    // 传入View和方向:"LEFT","RIGHT","TOP","BOTTOM","CENTER"
    public void addMyView(View v,String where){
        addMyView(v,where,null);
    }

    // 传入View和方向:"LEFT","RIGHT","TOP","BOTTOM","CENTER"和LayoutParams
    public void addMyView(View v,String where,LayoutParams params){
        v.setTag(where);// 为这个View设置Tag
        if (params==null)
            params=generateDefaultLayoutParams();// 设置为wrap_content
        addView(v,params);
        requestLayout();// 告诉父View重新测量我
        invalidate();// 重绘
    }

    // 传入View的布局文件和方向:"LEFT","RIGHT","TOP","BOTTOM","CENTER"
    public void addMyView(int ResId,String where){
        addMyView(ResId,where,null);
    }

    public void addMyView(int ResId,String where,LayoutParams params){
        View v=layoutInflater.inflate(ResId,this,false);
        addMyView(v,where,params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);// 测量所有子元素
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int setWidth=0,setHeight=0;
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i).getTag().toString().equals("CENTER")){
                setHeight=Math.min(width,getChildAt(i).getMeasuredHeight());
                setWidth=Math.min(width,getChildAt(i).getMeasuredWidth());
            }
        }
        if (widthMode==MeasureSpec.AT_MOST){
            width=Math.min(width,setWidth);
        }
        if (heightMode==MeasureSpec.AT_MOST){
            height=Math.min(height,setHeight);
        }
        if (heightMode==MeasureSpec.UNSPECIFIED){
            height=setHeight;
        }
        if (widthMode==MeasureSpec.UNSPECIFIED){
            width=setWidth;
        }

        // 如果是listview这类使用它，MeasureSpec的mode是UNSPECIFIED，最好再使用下面这句，不然，四个view的matchparent和wrapcontent不起作用
        // measureChildren(widthMeasureSpec,MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY));// 测量所有子元素

        setMeasuredDimension(width,height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                //startX= (int) event.getX();
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                isOpen=true;
                int offX=(int) (event.getX()-startX);
                int offY=(int) (event.getY()-startY);

                if (first) {
                    where=Math.abs(offX)>Math.abs(offY)?true:false;// true为水平,false为垂直

                    if (where){
                        // 水平移动
                        if (offX < 0) {
                            rightIsExsit = true;
                            leftIsExsit = false;
                        } else {
                            rightIsExsit = false;
                            leftIsExsit = true;
                        }
                    }else {
                        // 垂直移动
                        if (offY < 0) {
                            bottomIsExsit = true;
                            topIsExsit = false;
                        } else {
                            bottomIsExsit= false;
                            topIsExsit =  true;
                        }
                    }
                    first=false;
                }

                // 根据四个View能否滑动，响应滑动的方向和距离
                if (!CanSlideRight)
                    if (!leftIsExsit)
                        offX=offX<0?0:offX;
                if (!CanSlideLeft)
                    if (!rightIsExsit)
                        offX=offX>0?0:offX;
                if (!CanSlideUp)
                    if (!bottomIsExsit)
                        offY=offY>0?0:offY;
                if (!CanSlideDown)
                    if (!topIsExsit)
                        offY=offY<0?0:offY;

                // 根据四个View的宽度，保证不能滑动超过它的宽度。同时一次滑动事件序列只能响应一个方向
                if (leftIsExsit){
                    if (offX>0&&Math.abs(getScrollX())+offX>getChildAt(leftViewIndex).getMeasuredWidth()){
                        offX=getChildAt(leftViewIndex).getMeasuredWidth()-Math.abs(getScrollX());
                    }
                    if (offX<0&&Math.abs(getScrollX())-Math.abs(offX)<0){
                        offX=0-Math.abs(getScrollX());
                    }
                }
                if (rightIsExsit){
                    if (offX<0&&getScrollX()+Math.abs(offX)>getChildAt(rightViewIndex).getMeasuredWidth()){
                        offX=-(getChildAt(rightViewIndex).getMeasuredWidth()-getScrollX());
                    }
                    if (offX>0&&getScrollX()-offX<0){
                        offX=getScrollX();
                    }
                }
                if (topIsExsit){
                    if (offY>0&&Math.abs(getScrollY())+offY>getChildAt(topViewIndex).getMeasuredHeight()){
                        offY=getChildAt(topViewIndex).getMeasuredHeight()-Math.abs(getScrollY());
                    }
                    if (offY<0&&Math.abs(getScrollY())-Math.abs(offY)<0){
                        offY=0-Math.abs(getScrollY());
                    }
                }
                if (bottomIsExsit){
                    if (offY<0&&getScrollY()+Math.abs(offY)>getChildAt(bottomViewIndex).getMeasuredHeight()){
                        offY=-(getChildAt(bottomViewIndex).getMeasuredHeight()-getScrollY());
                    }
                    if (offY>0&&getScrollY()-offY<0){
                        offY=getScrollY();
                    }
                }

                //Log.e(TAG, "offX="+offX+",leftIsexsit="+leftIsExsit+",rightIsexsit="+rightIsExsit );
                scrollBy(where?-offX:0,where?0:-offY);
                startX= (int) event.getX();
                startY= (int) event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP:{
                if (where) {
                    if (getScrollX() > 0) {
                        if (getScrollX() > getChildAt(rightViewIndex).getMeasuredWidth() / 2) {
                            smoothScrollTo(getChildAt(rightViewIndex).getMeasuredWidth(), 0);
                            rightIsExsit = true;
                            leftIsExsit = false;
                            first = false;
                            isOpen=true;
                        } else {
                            smoothScrollTo(0, 0);
                            isOpen=false;
                            first = true;
                        }
                    } else {
                        if (Math.abs(getScrollX()) > getChildAt(leftViewIndex).getMeasuredWidth() / 2) {
                            smoothScrollTo(-getChildAt(leftViewIndex).getMeasuredWidth(), 0);
                            rightIsExsit = false;
                            leftIsExsit = true;
                            first = false;
                            isOpen=true;
                        } else {
                            smoothScrollTo(0, 0);
                            isOpen=false;
                            first = true;
                        }
                    }
                }else {
                    if (getScrollY() > 0) {
                        if (getScrollY() > getChildAt(bottomViewIndex).getMeasuredHeight() / 2) {
                            smoothScrollTo(0, getChildAt(bottomViewIndex).getMeasuredHeight());
                            bottomIsExsit = true;
                            topIsExsit = false;
                            first = false;
                            isOpen=true;
                        } else {
                            smoothScrollTo(0, 0);
                            isOpen=false;
                            first = true;
                        }
                    } else {
                        if (Math.abs(getScrollY()) > getChildAt(topViewIndex).getMeasuredHeight() / 2) {
                            smoothScrollTo(0, -getChildAt(topViewIndex).getMeasuredHeight());
                            bottomIsExsit = false;
                            topIsExsit = true;
                            first = false;
                            isOpen=true;
                        } else {
                            smoothScrollTo(0, 0);
                            isOpen=false;
                            first = true;
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int toLeft = 0;
        int toTop = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view=getChildAt(i);
            // 根据子View的Tag设置：布局位置、能否滑动、index
            switch (view.getTag().toString()){
                case "TOP":{
                    topViewIndex=i;
                    CanSlideUp=true;
                    view.layout(0,-view.getMeasuredHeight(),view.getMeasuredWidth(),0);
                    break;
                }
                case "BOTTOM":{
                    bottomViewIndex=i;
                    CanSlideDown=true;
                    view.layout(0,toTop,view.getMeasuredWidth(),toTop+view.getMeasuredHeight());
                    break;
                }
                case "LEFT":{
                    leftViewIndex=i;
                    CanSlideLeft=true;
                    view.layout(-view.getMeasuredWidth(),0,0,view.getMeasuredHeight());
                    break;
                }
                case "RIGHT":{
                    rightViewIndex=i;
                    CanSlideRight=true;
                    view.layout(toLeft,0,view.getMeasuredWidth()+toLeft,view.getMeasuredHeight());
                    break;
                }
                case "CENTER":{
                    centerViewIndex=i;

                    // 注意，在布局文件中，center必须在bottom和right之前声明
                    // 注意，在代码添加中，center必须在bottom和right之前声明

                    view.layout(0,0,view.getMeasuredWidth(),view.getMeasuredHeight());
                    toLeft=view.getMeasuredWidth();
                    toTop=view.getMeasuredHeight();
                    break;
                }
            }
        }
    }

    // 删除指定Tag的子View，并更新
    public void removeMyViewByTag(String where){
        for (int i=0;i<getChildCount();i++){
            if (getChildAt(i).getTag().toString().equals(where)){
                removeViewAt(i);
                requestLayout();
                invalidate();
            }
        }
    }

    // 滚动的动画
    Scroller scroller=new Scroller(getContext());
    private void smoothScrollTo(int destX,int destY){
        int scrollX=getScrollX();
        int deltaX=destX-scrollX;//滑动的距离
        int scrollY=getScrollY();
        int deltaY=destY-scrollY;
        scroller.startScroll(scrollX,scrollY,deltaX,deltaY,300);
        invalidate();//重绘
    }
    @Override
    public void computeScroll(){//重绘会调用这个方法，是空实现，由我们来实现
        if(scroller.computeScrollOffset()){//根据时间的流逝和剩下要滑动的距离计算currX的和currY的值，返回true代表滑动未完成
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            postInvalidate();//滑动未完成，继续重绘
        }
    }

    // 恢复滑动打开前
    public void close() {
        smoothScrollTo(0,0);
        isOpen=false;
        first = true;
        leftIsExsit=false;
        rightIsExsit=false;
        topIsExsit=false;
        bottomIsExsit=false;
    }
}
