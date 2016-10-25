package game.xjl.third_seven;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
/**
 * 项目名称：GameApplication
 * 类描述：
 * 创建人：xjl
 * 创建时间：2016/10/18 14:38
 * 修改人：Administrator
 * 修改时间：2016/10/18 14:38
 * 修改备注：
 */

public class PickerView extends View {

    public static final String TAG = "PickerView";

    //text之间间距和minTextSize之比
    public static final float MARGIN_ALPHA = 2.8F;
    //自动回滚到中间的速度
    public static final float SPEED = 2;

    private List<String> mDataList;

    private int mCurrentSelected=1;
    private Paint mPaint;

    private float mMaxTextSize = 80;

    private float mMinTextSize = 40;

    private float mMaxTextAlaph = 225;

    private float mMinTextAlpha = 120;

    private int mColorText = 0x333333;

    private int mViewHeight;

    private int mViewWidth;

    private float mLastDownY;

    //滑动的距离 重要参数
    private float mMoveLen = 0;

    private boolean isInit = false;

    private onSelectListener mSelectLitener;

    private Timer timer;

    private MyTimerTask mtask;

    Handler updateHandler  = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (Math.abs(mMoveLen) < SPEED) {
                mMoveLen = 0;
                if (mtask != null) {
                    mtask.cancel();
                    mtask = null;
                    performSelect();
                }
            } else {
                // 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下滚
                mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED;
                invalidate();
            }
        }
    };

    public PickerView(Context context) {
        super(context);
        init();
    }

    public PickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOnSelectListener(onSelectListener listener) {
        mSelectLitener = listener;
    }

    private void performSelect() {
        if (mSelectLitener != null) {
            mSelectLitener.onSelect(mDataList.get(mCurrentSelected));
        }
    }

    public void setData(List<String> mDataList) {
        this.mDataList = mDataList;
    }

    public void setSelected(int selected) {
        mCurrentSelected = selected;
    }

    //这是给成典型的链式结构对一个队列进行操作 第一个很简单就是将第一个元素移除 然后加到尾巴上就是 HeadtoTail
    private void moveHeadToTail() {
        String head = mDataList.get(0);
        mDataList.remove(0);
        mDataList.add(head);
    }
    //这是第二个操作是得到末尾的元素 然后将末尾的元素查到第一个位置上 并没有减少元素数量，之前的元素依次向后移动一位 这就是TailToHead
    private void moveTailtoHead() {
        String tail = mDataList.get(mDataList.size() - 1);
        mDataList.remove(mDataList.size() - 1);
        mDataList.add(0, tail);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();

        //看到这里不妨问问自己这三个得到宽度的函数到底得到的是什么数值
        Log.e("getMeasured value",getMeasuredHeight()+"  ^  "+getMeasuredWidth());
        Log.e("getMeasured Spec value",widthMeasureSpec+"  ^  "+heightMeasureSpec);
        Log.e("getMeasured value",getWidth()+"  ^  "+getHeight());
        //按照View的高度计算字体大小

        mMaxTextSize = mViewHeight / 4.0f;
        mMinTextSize = mMaxTextSize / 2.0f;
        isInit = true;
        invalidate();
    }

    private void init() {
        timer = new Timer();
        mDataList = new ArrayList<>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);//设置为 根据所给点画出剧中显示的文字
        mPaint.setColor(mColorText);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isInit)
        {
            drawData(canvas);
        }
    }



    /**
     * 抛物线
     * @param zero 零点坐标
     * @param x 偏移量
     * @return scale
     * */
    private float parabola(float zero,float x)
    {
        float scale=(float)(1-Math.pow(x/zero,2));
        //保证了scale的非负性
        return scale<0?0:scale;
    }

    private void drawData(Canvas canvas)
    {
         //先绘制选中的text再往上往下绘制其余的text 在这个计算过程中这个scale是最重要的
        float scale=parabola(mViewHeight/4.0f,mMoveLen);
        Log.e("getmMoveLen",mViewHeight+"  ^  "+mMoveLen);
        float size=(mMaxTextSize-mMinTextSize)*scale+mMinTextSize;
        mPaint.setTextSize(size);
        int alpha=(int)((mMaxTextAlaph-mMinTextAlpha)*scale+mMinTextAlpha);
        mPaint.setAlpha(alpha);

        //text 剧中绘制，注意baseline的计算才能打到剧中 y值是text中心坐标
        float x=mViewWidth/2.0f;
        float y=mViewHeight/2.0f+mMoveLen;

        Paint.FontMetricsInt fmi=mPaint.getFontMetricsInt();//得到字体的位置参数
        float baseline=(float)(y-(fmi.bottom/2.0+fmi.top/2.0)); //得到字体的开始位置
        canvas.drawText(mDataList.get(mCurrentSelected),x,baseline,mPaint);//在中间位置绘制文字
        //绘制上方data
        Log.e("绘制上方data",mCurrentSelected+"");
        for(int i=1;mCurrentSelected-i>=0;i++)
        {
            drawOtherText(canvas,i,-1);
        }
        //绘制下方的data
        for(int i=1;mCurrentSelected+i<mDataList.size();i++)
        {
            drawOtherText(canvas,i,1);
        }
    }


    /**
     * @param canvas
     * @param position 距离mCurrentSelected的差值
     * @param type 1表示向下绘制 -1表示向上绘制
     * */
    private void drawOtherText(Canvas canvas,int position,int type)
    {
        float d=MARGIN_ALPHA*mMinTextSize*position+type*mMoveLen;
        //这个scale是一个从0到1的二次曲线
        float scale=parabola(mViewHeight/4.0f,d);
        //最大字大小减去最小字大小*scle
        float size=(mMaxTextSize-mMinTextSize)*scale+mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int)((mMaxTextAlaph-mMinTextAlpha)*scale+mMinTextAlpha));
        float y=(float)(mViewHeight/2.0+type*d);
        Paint.FontMetricsInt fmi=mPaint.getFontMetricsInt();
        float baseline=(float)(y-(fmi.bottom/2.0+fmi.top/2.0));
        canvas.drawText(mDataList.get(mCurrentSelected+type*position),(float)(mViewWidth/2.0),baseline,mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:doDown(event);break;
            case MotionEvent.ACTION_MOVE:doMove(event);break;
            case MotionEvent.ACTION_UP:doUp();break;
        }
        return  true;
    }

    private void doDown(MotionEvent event)
    {
        if(mtask!=null)
        {
            mtask.cancel();
            mtask=null;
        }
        mLastDownY=event.getY();
    }

    private void doMove(MotionEvent event)
    {
        mMoveLen+=(event.getY()-mLastDownY);
        if(mMoveLen>MARGIN_ALPHA*mMinTextSize/2){
            //往下滑超过离开距离
            moveTailtoHead();
            mMoveLen=mMoveLen-MARGIN_ALPHA*mMinTextSize;
        }else if(mMoveLen<-MARGIN_ALPHA*mMinTextSize/2)
        {
            //上滑超过离开距离
            moveHeadToTail();
            mMoveLen=mMoveLen+MARGIN_ALPHA*mMinTextSize;
        }
        mLastDownY=event.getY();
        invalidate();
    }

    public void doMove(float f)
    {
        mMoveLen+=(f-mLastDownY);
        if(mMoveLen>MARGIN_ALPHA*mMinTextSize/2){
            //往下滑超过离开距离
            moveTailtoHead();
            mMoveLen=mMoveLen-MARGIN_ALPHA*mMinTextSize;
        }else if(mMoveLen<-MARGIN_ALPHA*mMinTextSize/2)
        {
            //上滑超过离开距离
            moveHeadToTail();
            mMoveLen=mMoveLen+MARGIN_ALPHA*mMinTextSize;
        }
        mLastDownY=f;
        invalidate();
    }

    public void doUp()
    {
        // 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
        if (Math.abs(mMoveLen) < 0.0001)
        {
            mMoveLen = 0;
            return;
        }
        if (mtask != null)
        {
            mtask.cancel();
            mtask = null;
        }
        mtask = new MyTimerTask(updateHandler);
        timer.schedule(mtask, 0, 10);
    }




    class MyTimerTask extends TimerTask {
        Handler handler;
        public MyTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }
    }

    public interface onSelectListener {
        void onSelect(String text);
    }
}
