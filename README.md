学无止境，复习提高的最好办法就是撸代码，这一次写的是仿QQ的列表左滑，拓展了一点点，具体分为SlideView（可以上下左右滑动的ViewGroup）、和SlideListView（根据LIstView优化的可左右滑，处理了滑动冲突），用法相当简单（不信你看布局文件）。[源代码在此](https://github.com/SamanLan/MySlideListview)，欢迎大家关注我的[Github](https://github.com/SamanLan)、Star。
效果图如下：
![可左滑右滑上滑下滑出现view](http://upload-images.jianshu.io/upload_images/1787089-8eec552be6e27344.gif?imageMogr2/auto-orient/strip)
![可左滑右滑出现view的listview](http://upload-images.jianshu.io/upload_images/1787089-a91edf801507186c.gif?imageMogr2/auto-orient/strip)

代码有点长，这里不全部贴了，只说说主要思路
1. 首先看布局
```
<com.samanlan.myslidelistview.MySlideViewGroup
  android:id="@+id/my"
  android:layout_width="match_parent"
  android:layout_height="140dp">
  <TextView
    android:tag="CENTER"
    android:textSize="20sp"
    android:clickable="true"
    android:onClick="textClick"
    android:background="#cc8800"
    android:layout_width="match_parent"
    android:layout_height="140dp"
    android:text="center"/>
  <TextView
    android:tag="LEFT"
    android:textSize="20sp"
    android:clickable="true"
    android:onClick="textClick"
    android:background="#0099cc"
    android:layout_width="140dp"
    android:layout_height="140dp"
    android:text="left"/>
   <TextView
    android:tag="RIGHT"
    android:textSize="20sp"
    android:clickable="true"
    android:onClick="textClick"
    android:background="#667711"
    android:layout_width="140dp"
    android:layout_height="140dp"
  android:text="right"/>
<TextView
    android:tag="BOTTOM"
    android:textSize="20sp"
    android:clickable="true"
    android:onClick="textClick"
    android:background="#aa86dc"
    android:layout_width="match_parent"
    android:layout_height="50dp"
    android:text="bottom"/>
 <TextView
    android:tag="TOP"
    android:textSize="20sp"
    android:clickable="true"
    android:onClick="textClick"
    android:background="#913684"
    android:layout_width="match_parent"
    android:layout_height="50dp"
    android:text="top"/>
</com.samanlan.myslidelistview.MySlideViewGroup>
```
不知大家有没有发现特别的地方，相信细心点的朋友就发现了，在MySlideViewGroup里面的每一个View都有一个Tag，没错，这个自定义ViewGroup就是根据子View的Tag来设置子View的滑动出现的位置
2. 看看onLayout的代码就知道上面布局文件说的的Tag设置子View滑动出现是什么意思了
```
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
```
是不是阔然开朗，就是根据子View的Tag来设置子View的位置。
3. 滑动的View肯定少不了onTouchEvent这个方法，但是代码量有点多，具体请看源码。
4. 既可以通过布局文件设置，也可以通过代码方式实现
```
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
```
5. 而ListView则是处理好拦截事件就行了。
6. 说不清道不明，看代码最好解决，[源代码在此](https://github.com/SamanLan/MySlideListview)，欢迎大家关注我的[Github](https://github.com/SamanLan)、Star。
