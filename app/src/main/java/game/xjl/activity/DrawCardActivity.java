package game.xjl.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import game.xjl.R;

/**
 * 项目名称：GameApplication
 * 类描述：
 * 创建人：xjl
 * 创建时间：2016/10/12 13:58
 * 修改人：Administrator
 * 修改时间：2016/10/12 13:58
 * 修改备注：
 */
public class DrawCardActivity extends Activity implements View.OnClickListener {

    protected TextView card1;
    protected TextView card2;
    protected TextView card3;
    protected TextView card4;
    protected TextView card5;
    protected TextView card6;
    protected TextView card7;
    protected TextView card8;
    protected TextView card9;

    HashMap<Integer, TextView> imageMap;
    ArrayList<TextView> imageList;

    private Random random=new Random();
    private int Result;

    //只能点击一张牌进行抽奖
    private int singleClick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_card);
        initView();
    }

    private void getResult(int count)
    {
        Result=random.nextInt(count)+1;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.card5) {
            card5.setOnClickListener(null);
            singleClick = 1;
            initCardAndBonusResult();
            for (int i = 0; i < imageList.size(); i++) {
                TextView v = imageList.get(i);
                if (v != null) {
                    translationMethod(v, v.getX(), v.getY(), card5.getX(), card5.getY(), 1000);
                }
            }

            Toast.makeText(DrawCardActivity.this, "请选择一张卡牌", Toast.LENGTH_SHORT).show();

        } else {
            if (singleClick == 1) {
                singleClick = 0;
                final TextView iv = imageMap.get(view.getId());

                ObjectAnimator oa = ObjectAnimator.ofFloat(iv, "rotationY", 0f, 360f);
                oa.setDuration(1000);
                oa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (Math.abs(animation.getAnimatedFraction() - 0.25f) < 0.01f) {
                            iv.setBackground(getResources().getDrawable(R.mipmap.result));
                            iv.setText(isRevertResult(false));

                        }
                    }
                });

                oa.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        card5.setOnClickListener(DrawCardActivity.this);
                        Toast.makeText(DrawCardActivity.this,isRevertResult(false),Toast.LENGTH_SHORT).show();

                    }
                });
                oa.start();

            }
        }
    }

    private StringBuffer isRevertResult(boolean isRevert)
    {
        if(isRevert)
        {
           return  new StringBuffer("您中了"+Result+"奖").reverse();
        }else
        {
            return  new StringBuffer("您中了"+Result+"奖");
        }
    }


    private void translationMethod(final View v, float startX, float startY, float endX, float endY, int duration) {

        AnimatorSet animatiorSet = new AnimatorSet();
        animatiorSet.playTogether(initObjectAnimator(v, startX, endX, "translationX"), initObjectAnimator(v, startY, endY, "translationY"));
        animatiorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setAllViewOnClickListener(true);
            }
        });
        animatiorSet.setDuration(duration);
        animatiorSet.start();

    }

    private ObjectAnimator initObjectAnimator(final View v, float start, float end, String property) {

        ObjectAnimator oa;
        if (start <= end) {
            oa = ObjectAnimator.ofFloat(v, property, 0, Math.abs(start - end));
        } else {
            oa = ObjectAnimator.ofFloat(v, property, 0, -Math.abs(start - end));
        }
        oa.setRepeatCount(1);
        oa.setRepeatMode(ValueAnimator.REVERSE);
        return oa;
    }

    private void setAllViewOnClickListener(boolean isAdd) {
        for (int i = 0; i < imageList.size(); i++) {
            if (isAdd) {
                imageList.get(i).setOnClickListener(this);
            } else {
                imageList.get(i).setOnClickListener(null);
            }
        }
    }

    private void initCardAndBonusResult()
    {
        getResult(5);
        for(int i=0;i< imageList.size(); i++)
        {
            imageList.get(i).setBackground(getResources().getDrawable(R.mipmap.draw_card));
            imageList.get(i).setText("");
        }
    }


    private void initView() {
        card1 = (TextView) findViewById(R.id.card1);
        card2 = (TextView) findViewById(R.id.card2);
        card3 = (TextView) findViewById(R.id.card3);
        card4 = (TextView) findViewById(R.id.card4);
        card5 = (TextView) findViewById(R.id.card5);
        card6 = (TextView) findViewById(R.id.card6);
        card7 = (TextView) findViewById(R.id.card7);
        card8 = (TextView) findViewById(R.id.card8);
        card9 = (TextView) findViewById(R.id.card9);

        imageMap = new HashMap<>();
        imageList = new ArrayList<>();

        imageMap.put(R.id.card1, card1);
        imageMap.put(R.id.card2, card2);
        imageMap.put(R.id.card3, card3);
        imageMap.put(R.id.card4, card4);

        imageMap.put(R.id.card6, card6);
        imageMap.put(R.id.card7, card7);
        imageMap.put(R.id.card8, card8);
        imageMap.put(R.id.card9, card9);

        imageList.add(card1);
        imageList.add(card2);
        imageList.add(card3);
        imageList.add(card4);

        imageList.add(card6);
        imageList.add(card7);
        imageList.add(card8);
        imageList.add(card9);

        card5.setOnClickListener(DrawCardActivity.this);

    }
}
