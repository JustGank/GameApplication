package game.xjl.draw_card;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import game.xjl.Bean.BonusBean;

/**
 * 项目名称：GameApplication
 * 类描述：
 * 创建人：xjl
 * 创建时间：2016/10/11 11:32
 * 修改人：Administrator
 * 修改时间：2016/10/11 11:32
 * 修改备注：
 */
public class RotationPanelView extends View {

    private int mWidth;
    private int mHeight;
    private int mRadio;
    private int mCircleX;
    private int mCircleY;
    private Paint mPaint;
    private ArrayList<BonusBean> bonusList;
    private ArrayList<Integer> colorList;

    //设置画布抗锯齿
    private PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    public RotationPanelView(Context context) {
        super(context);
        init();
    }

    public RotationPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RotationPanelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //这里的队列的长度需要小于8
    public void setArrayList(ArrayList<BonusBean> bonusList) {
        this.bonusList = bonusList;
    }


    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (checkData()) {
            canvas.setDrawFilter(pfd);
            mPaint.setColor(Color.BLACK);
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(mCircleX, mCircleY, mRadio, mPaint);

            RectF rectF = new RectF(mCircleX - mRadio, mCircleY - mRadio, mCircleX + mRadio, mCircleY + mRadio);

            float tempRadio = 0.0f;


            for(int i=0;i<bonusList.size();i++)
            {
                float begainRadio=360.0f*tempRadio-90.0f;
                tempRadio=tempRadio+bonusList.get(i).getBonusRates();
                float endRadio=360*bonusList.get(i).getBonusRates();
                mPaint.setColor(colorList.get(i));
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawArc(rectF,begainRadio,endRadio,true,mPaint);
            }


            invalidate();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getWidth();
        mHeight = getHeight();
        mCircleX = mWidth / 2;
        mCircleY = mHeight / 2;
        mRadio = mCircleX - 10;
    }

    private boolean checkData() {
        if (bonusList != null && bonusList.size() > 0 && bonusList.size() < 8) {
            float temp = 0.0f;
            for (int i = 0; i < bonusList.size(); i++) {
                temp = temp + bonusList.get(i).getBonusRates();
            }

            if (Math.abs(temp - 1.0f) < 0.01f) {
                colorList = new ArrayList<>();
                colorList.add(Color.RED);
                colorList.add(Color.BLUE);
                colorList.add(Color.YELLOW);
                colorList.add(Color.GREEN);
                colorList.add(Color.GRAY);
                colorList.add(0xFF123456);
                colorList.add(0xFF456123);
                colorList.add(0xFF456456);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
