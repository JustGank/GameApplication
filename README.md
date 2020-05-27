# GameApplication
Android动画之旅有一段时间没有写新的内容了。我之前想的是，边学习边开发一些有用的小Demo但是一写起来就根本停不下来了。先给大家展示一个成果，后续我会将每个项目的原码和原理分析发布出来。虽然我知道看的人可能不多，但是写东西总结对自己本身也是一种检验和复习。还是希望我的博客人可以越来越多。
先展示一下成果。本来是想写五个但是写着写着就写成了8个。
![这里写图片描述](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTYxMTA4MDkyOTI0NjM2?x-oss-process=image/format,png)
那么今天写一个最简单的大转盘开始，逐渐深入浅出给大家介绍一些Android View底层和Android动画的使用和原理。

那么我们先来看下大转盘的分解，然后逐步进行实现。
那么大家看到核心就是这个
![这里写图片描述](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTYxMTA4MTAwODQ1NTkw?x-oss-process=image/format,png)
夜神录的屏幕，然后又转的GIF 旋转起来感觉很慢，但是真实情况的转动还是很好的！这里为了大家更好的理解，所以还是放一个GIF吧。
做大转盘，首先我们需要一个转盘，而且我希望这个转盘，的奖项和区域是可以自定义的。因为很多时候我们的中奖率是有变化的，如果做成动态的，我们后台只需要简单的输入数据数组就可以，让我们新的转盘产生。

那么问题一来了，我没有转盘啊。怎么办那？而且又是动态的，那么没办法了，自己动手丰衣足食，我们先自己画一个圆盘，然后根据输入的比例，画出对应的扇形就可以了。
那么先给出自定义转盘类的代码。

```
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
	//View的宽和高
    private int mWidth;
    private int mHeight;
    //圆半径
    private int mRadio;
    //圆心坐标
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
			//设置浮点类型的矩形，也是我们所画扇形的区域。也就是四边与圆相切的正方形
            RectF rectF = new RectF(mCircleX - mRadio, mCircleY - mRadio, mCircleX + mRadio, mCircleY + mRadio);

            float tempRadio = 0.0f;
			//为每个扇形设置角度和颜色
            for(int i=0;i<bonusList.size();i++)
            {
	            //减90度 使扇形从12点方向开始
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
        //测试当前View的宽和高 以方便后面设置我们扇形的区域
        mWidth = getWidth();
        mHeight = getHeight();
        mCircleX = mWidth / 2;
        mCircleY = mHeight / 2;
        mRadio = mCircleX - 10;
    }
	//对输入的数据进行判断，如果数据错误，不会绘制圆周，
    private boolean checkData() {
        if (bonusList != null && bonusList.size() > 0 && bonusList.size() < 8) {
            float temp = 0.0f;
            for (int i = 0; i < bonusList.size(); i++) {
                temp = temp + bonusList.get(i).getBonusRates();
            }
			//看看加入数据比例之和是否等于1 由于是浮点数可能产生误差，所以这里不能写"==1"
            if (Math.abs(temp - 1.0f) < 0.01f) {
                colorList = new ArrayList<>();
                //设置颜色列表，我知道的颜色不多，可以自行改成你喜欢的颜色列表
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
```

那么有了我们圆形圆盘，剩下的我们只需要让这个圆盘转起来就可以了。
那么这里我使用的是，Android动画的对象动画，包括了Z轴旋转，和开始转动转盘加速，快结束的时候转盘减速。
在来看下布局

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@mipmap/table"
    android:orientation="vertical">

    <TextView
        android:text="开心转转转"
        android:layout_marginTop="10dp"
        android:textSize="30sp"
        android:gravity="center_horizontal"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

	<!--模拟中奖的指针-->
    <TextView
        android:text="|"
        android:layout_marginTop="10dp"
        android:textSize="30sp"
        android:gravity="center_horizontal"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

    <game.xjl.draw_card.RotationPanelView
        android:layout_marginTop="5dp"
        android:id="@+id/rotation_panel_view"
        android:layout_width="400dp"
        android:layout_height="400dp"
        android:layout_gravity="center"
        />

    <TextView
        android:layout_marginTop="20dp"
        android:id="@+id/the_result"
        android:textColor="#FF0000"
        android:textSize="35sp"
        android:gravity="center"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="恭喜您中奖啦"
        />
    
</LinearLayout>
```
然后就是我们的使用啦。这里我们为了产生结果，和更好的真实性即随机效果，我设置了二次随机，即第一次我们随机产生一个中几等奖，然后在这个基础上，在随机一个在这个中奖区域的角度，那么这样转起来，仿真效果就会很好。我在网上也看了一些例子，大多是没有速度变化效果，然后生硬的停在一个中奖区域的中间。这样的用户体验很差，做产品，要做就做好。既是小产品。

```
/**
 * 项目名称：GameApplication
 * 类描述：
 * 创建人：xjl
 * 创建时间：2016/10/11 14:05
 * 修改人：Administrator
 * 修改时间：2016/10/11 14:05
 * 修改备注：
 */
public class RotationPanelActivity extends Activity implements View.OnClickListener {

    private RotationPanelView rotation_panel_view;
    private ArrayList<BonusBean> bonusBeens;
    private TextView the_result;
    private int randomBonusResult; //所中的奖等
    private float randomBonusRadio;//随机角度


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotation_panel);
        initData();
        initView();
   }

    private void initData() {
        bonusBeens = new ArrayList<>();
		//初始化中奖数据，只需要让中奖数据相加等于1即可
        BonusBean bonusBean = new BonusBean();
        bonusBean.setBonusRates(0.2f);
        bonusBeens.add(bonusBean);

        BonusBean bonusBean1 = new BonusBean();
        bonusBean1.setBonusRates(0.2f);
        bonusBeens.add(bonusBean1);

        BonusBean bonusBean2 = new BonusBean();
        bonusBean2.setBonusRates(0.20f);
        bonusBeens.add(bonusBean2);

        BonusBean bonusBean3 = new BonusBean();
        bonusBean3.setBonusRates(0.15f);
        bonusBeens.add(bonusBean3);

        BonusBean bonusBean4 = new BonusBean();
        bonusBean4.setBonusRates(0.25f);
        bonusBeens.add(bonusBean4);

    }


    private void initView() {
        the_result = (TextView) findViewById(R.id.the_result);
        rotation_panel_view = (RotationPanelView) findViewById(R.id.rotation_panel_view);
        rotation_panel_view.setOnClickListener(this);
        rotation_panel_view.setArrayList(bonusBeens);
    }

    @Override
    public void onClick(View v) {
        if (R.id.rotation_panel_view == v.getId()) {
            initAnimationAndSetRotationCount(10);
        }
    }
	//开放仿真转圈度数，要不转一下，只有一圈就尴尬了。所以想先转多少圈在显示中奖结果，自己定。
    private void initAnimationAndSetRotationCount(int count) {
        Random random = new Random();
        //第一次随机产生中奖结果
        randomBonusResult = random.nextInt(bonusBeens.size()) + 1;
        //二次随机产生一个停在目标中奖区域的随机度数
        randomBonusRadio = random.nextFloat();
        float tempFloat = 0.0f;
        for (int i = 0; i < randomBonusResult; i++) {
            tempFloat = tempFloat + bonusBeens.get(i).getBonusRates();
        }
        tempFloat = tempFloat - bonusBeens.get(randomBonusResult - 1).getBonusRates() * randomBonusRadio;
        tempFloat = 360.0f * tempFloat;
        //Andorid对象动画 设置我们的View  动画类型。由于自定义View是顺时针画的，所以为了停在我们的中奖区域使用逆时针旋转。即中奖度数取反。
        ObjectAnimator animator = ObjectAnimator.ofFloat(rotation_panel_view, "rotation", 0f,- (360f * count + tempFloat));
        if (count == 0) {
            animator.setDuration(1000);
        } else {
	        //根据设置的圈数 设置动画持续时间 即每圈0.5秒
            animator.setDuration(500 * count);
        }
        animator.start();
        rotation_panel_view.setOnClickListener(null);
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                rotation_panel_view.setOnClickListener(RotationPanelActivity.this);
                the_result.setText("恭喜您" + randomBonusResult + "奖");
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
    }

}
```

