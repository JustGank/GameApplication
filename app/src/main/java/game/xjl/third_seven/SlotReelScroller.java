package game.xjl.third_seven;

import android.content.Context;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;

/**
 * 项目名称：GameApplication
 * 类描述：
 * 创建人：xjl
 * 创建时间：2016/10/20 14:53
 * 修改人：Administrator
 * 修改时间：2016/10/20 14:53
 * 修改备注：
 */

public class SlotReelScroller implements Runnable {
    //Scrolling listener interface
    public interface ScrollingListener{

        //callback called when scrolling is performed
        void onScroll(int distance);
        //finishing callback
        void onFinished();
    }

    private Handler mHandler;
    private Scroller mScroller;
    private ScrollingListener mScrollingListener;

    int lastY=0;
    private int distance;
    private int previousDistance;


    public SlotReelScroller(Context context,ScrollingListener listener)
    {
        mHandler=new Handler();
        //新建一个可以先加速后减速的scroller
        mScroller=new Scroller(context,new AccelerateDecelerateInterpolator());
        mScrollingListener=listener;
    }

    public void scroll(int distance,int duration)
    {
        this.distance=distance;
        mScroller.forceFinished(true);
        mScroller.startScroll(0,0,0,distance,duration);
        mHandler.post(this);
    }

    @Override
    public void run() {
        int delta=0;
        /**
         * Call this when you want to know the new location.  If it returns true,
         * the animation is not yet finished.
         */
        mScroller.computeScrollOffset();
        int currY=mScroller.getCurrY();

        delta=currY-lastY;
        lastY=currY;

        if(Math.abs(delta)!=previousDistance&&delta!=0)
        {
            mScrollingListener.onScroll(delta);
        }

        if(mScroller.isFinished()==false)
        {
            //Post this runnable again on UI thread until all scroll values are read.
            mHandler.post(this);
        }else
        {
            previousDistance=distance;
            mScrollingListener.onFinished();
        }
    }








}
