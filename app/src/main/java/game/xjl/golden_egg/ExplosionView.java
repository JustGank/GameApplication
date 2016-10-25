package game.xjl.golden_egg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import tyrantgit.explosionfield.ExplosionAnimator;
import tyrantgit.explosionfield.Utils;

/**
 * 项目名称：GameApplication
 * 类描述：
 * 创建人：xjl
 * 创建时间：2016/10/10 16:17
 * 修改人：Administrator
 * 修改时间：2016/10/10 16:17
 * 修改备注：
 */
public class ExplosionView extends View {

    private List<ExplosionAnimator> mExplosions = new ArrayList<>();

    private int[] mExplosionInset = new int[2];


    public ExplosionView(Context context) {
        super(context);
        init();
    }

    public ExplosionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExplosionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        //把转换结果付给之前的这个数组
        Arrays.fill(mExplosionInset, Utils.dp2Px(32));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (ExplosionAnimator explosion : mExplosions) {
            explosion.draw(canvas);
        }
    }

    public void expandExplosionBound(int dx, int dy) {
        mExplosionInset[0] = dx;
        mExplosionInset[1] = dy;
    }

    public void explode(Bitmap bitmap, Rect bound, long startDelay, long duration) {
        final ExplosionAnimator explosion = new ExplosionAnimator(this, bitmap, bound);

        explosion.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mExplosions.remove(animation);
            }
        });

        explosion.setStartDelay(startDelay);

        explosion.setDuration(duration);

        mExplosions.add(explosion);

        explosion.start();

    }

    public void explode(final View view) {
        Rect r = new Rect();
        view.getGlobalVisibleRect(r);
        int[] location = new int[2];
        getLocationOnScreen(location);
        r.offset(-location[0], -location[1]);
        r.inset(-mExplosionInset[0], -mExplosionInset[1]);
        int startDelay = 100;

        /**
         * ValueAnimator 是属性动画的一个核心类 他负责将我们设置动画 平滑的进行过渡和计算
         * 使得动画效果更好,而且属性动画是单例的。而且是最流行的调用方法返回本对象的形式。
         *
         * 举一个简单的例子 animator 实现一个苹果的将一个数由0编程1的过程，其持续的时间为 1秒钟
         *animator.ofFloat(0.0f,1.0f); 浮点数平滑过渡
         *animator.ofArgb(); 颜色平滑过渡
         *animator.ofInt(); 整数平滑过渡
         *animator.setDuration(1000);
         *animator.start();
         *
         *
         * 摇晃动画的实现是 设置一个 数值动画
         * 然后添加刷新动画
         * 在刷新动画中设置一个随机数 那么在动画的持续时间内会因为不断的晃动而进行刷新
         * 从而完成晃动的效果
         * */
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(150);


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            Random random = new Random();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTranslationX((random.nextFloat() - 0.5f) * view.getWidth() * 0.05f);
                view.setTranslationY((random.nextFloat() - 0.5f) * view.getHeight() * 0.05f);
            }
        });
        //启动晃动动画
        animator.start();
        //整个晃动动画结束

        /**
         * 这个是一个View的缩放动画
         * */
        view.animate().setDuration(150).setStartDelay(startDelay).scaleX(0.0f).scaleY(0.0f).alpha(0f).start();

       explode(Utils.createBitmapFromView(view), r, startDelay, 0x400);
    }

    public void clear() {
        mExplosions.clear();
        invalidate();
    }

    public static ExplosionView attach2Window(Activity activity) {
        ViewGroup rootView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        ExplosionView explosionField = new ExplosionView(activity);
        rootView.addView(explosionField, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return explosionField;
    }


}
