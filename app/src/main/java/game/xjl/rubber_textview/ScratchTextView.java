package game.xjl.rubber_textview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.HashMap;

/**
 * 项目名称：GameApplication
 * 类描述：
 * 创建人：xjl
 * 创建时间：2016/10/13 9:22
 * 修改人：Administrator
 * 修改时间：2016/10/13 9:22
 * 修改备注：
 */
public class ScratchTextView extends TextView {
    public ScratchTextView(Context context) {
        super(context);
    }

    public ScratchTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScratchTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private float TOUCH_TOLERANCE;//填充距离，实现条更自然，柔和，值越小，越柔和

    private Bitmap mBitmap;

    private Canvas mCanvas;

    private Path mPath;

    private Paint mPaint;

    private float mX, mY;

    private boolean isInited = false;

    private int mWidth, mHeight, mBeginX, mBeginY, mEndX, mEndY;

    private float paintRadius;//获得落笔点 的半径

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInited) {
            mCanvas.drawPath(mPath, mPaint);//把线画到mCanvas上,mCanvas会把线滑到mBitmap
            canvas.drawBitmap(mBitmap, 0, 0, null);//把mBitmap画到textview 上 canvas是父textview传来的

        }
    }

    /**
     * 开启擦除功能
     *
     * @param bgColor          背景颜色 用于盖住下面的文字
     * @param paintStrokeWidth 擦出线宽
     * @param touchTolerance   填充距离，值越小，越柔和
     */
    public void initScratchCard(final int bgColor, final int paintStrokeWidth, float touchTolerance) {
        TOUCH_TOLERANCE = touchTolerance;
        mPaint = new Paint();
        mPaint.setAlpha(255);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));//核心
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);//空心
        mPaint.setStrokeJoin(Paint.Join.ROUND);//前圆角
        mPaint.setStrokeCap(Paint.Cap.ROUND);//后圆角
        mPaint.setStrokeWidth(paintStrokeWidth);//笔宽
        paintRadius = paintStrokeWidth ;
        //痕迹
        mPath = new Path();

        mBitmap = Bitmap.createBitmap(getLayoutParams().width, getLayoutParams().height, Bitmap.Config.ARGB_8888);

        mCanvas = new Canvas(mBitmap);

        Paint paint = new Paint();//用于绘制生成的背景图片的字体

        paint.setTextSize(50);

        paint.setColor(Color.parseColor("#A79F9F"));

        mCanvas.drawColor(bgColor);//画上背景颜色

        mCanvas.drawText("刮开此图层", getLayoutParams().width / 4, getLayoutParams().height / 2 + 15, paint);

        isInited = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getWidth();
        mHeight = getHeight();

        //得到一个中心区域
        mBeginX = getWidth() / 3;
        mBeginY = getHeight() / 3;
        mEndX = getWidth() / 3 * 2;
        mEndY = getHeight() / 3 * 2;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isInited) {
            return true;
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN://触点按下
                    touchDown(event.getX(), event.getY());
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE://触点移动
                    touchMove(event.getX(), event.getY());
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touchUp(event.getX(), event.getY());
                    invalidate();
                    break;
            }
            isClear(event.getX(), event.getY());
        }

        return true;
    }

    private void touchDown(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;


    }

    private void touchMove(float x, float y) {
        //x和y 的移动距离
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            //二次贝塞尔，实现平滑曲线，mX,mY 为操作点，
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }


    }

    private void touchUp(float x, float y) {
        mPath.lineTo(x, y);
        mCanvas.drawPath(mPath, mPaint);
        mPath.reset();

    }

    private HashMap<String, Boolean> pointList;
    private float proportation = 1.0f;
    int time = 1;

    private float initPointListLength;

    private void isClear(float x, float y) {

        if (pointList == null) {
            pointList = new HashMap<>();
            for (int i = 0; i < mEndX-mBeginX; i++) {
                for (int j = 0; j < mEndY-mBeginY; j++) {
                    pointList.put(i + "" + j, false);
                }
            }
            initPointListLength = pointList.size();
        }



        if (proportation >= 0.5&&x>mBeginX&&x<mEndX&&y>mBeginY&&y<mEndY) {
            x=x-mBeginX;
            y=y-mBeginY;
            for (int i = 0; i < mEndX-mBeginX; i++) {
                for (int j = 0; j <  mEndY-mBeginY; j++) {
                    float x2 = Math.abs(i - x) * Math.abs(i - x);
                    float y2 = Math.abs(j - y) * Math.abs(j - y);
                    if (Math.sqrt(x2 + y2) - paintRadius<= 0.1) {
                        if (pointList.get(i + "" + j) != null) {
                            pointList.remove(i + "" + j);
                            proportation = (float) pointList.size() / initPointListLength;
                            Log.e("pointList.proportation", proportation + "");
                            if (proportation < 0.5 && time == 1) {
                                time--;
                                isInited = false;
                                if (resultOutput != null) {
                                    resultOutput.onResultOutput();
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }


    private OnResultOutput resultOutput;

    public void setOnResultOutput(OnResultOutput resultOutput) {
        this.resultOutput = resultOutput;
    }

    public interface OnResultOutput {
        public void onResultOutput();
    }

}
