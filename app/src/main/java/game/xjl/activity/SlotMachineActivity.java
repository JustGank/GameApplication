package game.xjl.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import java.util.ArrayList;
import java.util.Random;

import game.xjl.R;
import game.xjl.slot_machine.ColorEvaluator;
import game.xjl.slot_machine.MyColorImageView;

/**
 * 项目名称：GameApplication
 * 类描述：
 * 创建人：xjl
 * 创建时间：2016/10/17 11:25
 * 修改人：Administrator
 * 修改时间：2016/10/17 11:25
 * 修改备注：
 */

public class SlotMachineActivity extends Activity implements View.OnClickListener {

    protected Button submit;
    protected MyColorImageView bar1;
    protected MyColorImageView cherry1;
    protected MyColorImageView orange1;
    protected MyColorImageView seven1;
    protected MyColorImageView ring1;
    protected MyColorImageView watermelon1;
    protected MyColorImageView bar2;
    protected MyColorImageView watermelon4;
    protected MyColorImageView ring4;
    protected MyColorImageView seven4;
    protected MyColorImageView orange4;
    protected MyColorImageView cherry4;
    protected MyColorImageView cherry2;
    protected MyColorImageView orange2;
    protected MyColorImageView seven2;
    protected MyColorImageView ring2;
    protected MyColorImageView watermelon2;
    protected MyColorImageView bar4;
    protected MyColorImageView watermelon3;
    protected MyColorImageView ring3;
    protected MyColorImageView seven3;
    protected MyColorImageView orange3;
    protected MyColorImageView cherry3;
    protected MyColorImageView bar3;

    ArrayList<MyColorImageView> imageList = null;

    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.slot_machine);
        initView();
    }

    int i = -1;
    int count = -1;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit && imageList != null) {

            initImageList();
            submit.setOnClickListener(null);
            final int result = 24 * 5 + random.nextInt(imageList.size());
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, result);
            valueAnimator.setDuration(50 * result);

            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    if (i != (int) animation.getAnimatedValue()) {
                        i = (int) animation.getAnimatedValue();
                        final int j = i % imageList.size();
                        imageList.get(j).setBackgroundColor(Color.RED);
                        ObjectAnimator oa = ObjectAnimator.ofObject(imageList.get(j), "color", new ColorEvaluator(), "ff0000", "ff0000");
                        oa.setDuration(300);
                        oa.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                imageList.get(j).setBackgroundColor(Color.parseColor("#ffffffff"));
                                count = count + 1;
                                Log.e("count result",count+"  ^  "+result);
                                if (count == result) {
                                    imageList.get(result % imageList.size()).setBackgroundColor(Color.RED);
                                    submit.setOnClickListener(SlotMachineActivity.this);
                                }
                            }
                        });
                        oa.start();
                    }
                }
            });
            valueAnimator.start();
        }
    }

    private void initView() {
        imageList = new ArrayList<>();

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(SlotMachineActivity.this);

        bar1 = (MyColorImageView) findViewById(R.id.bar1);
        cherry1 = (MyColorImageView) findViewById(R.id.cherry1);
        orange1 = (MyColorImageView) findViewById(R.id.orange1);
        seven1 = (MyColorImageView) findViewById(R.id.seven1);
        ring1 = (MyColorImageView) findViewById(R.id.ring1);
        watermelon1 = (MyColorImageView) findViewById(R.id.watermelon1);
        bar2 = (MyColorImageView) findViewById(R.id.bar2);
        watermelon4 = (MyColorImageView) findViewById(R.id.watermelon4);
        ring4 = (MyColorImageView) findViewById(R.id.ring4);
        seven4 = (MyColorImageView) findViewById(R.id.seven4);
        orange4 = (MyColorImageView) findViewById(R.id.orange4);
        cherry4 = (MyColorImageView) findViewById(R.id.cherry4);
        cherry2 = (MyColorImageView) findViewById(R.id.cherry2);
        orange2 = (MyColorImageView) findViewById(R.id.orange2);
        seven2 = (MyColorImageView) findViewById(R.id.seven2);
        ring2 = (MyColorImageView) findViewById(R.id.ring2);
        watermelon2 = (MyColorImageView) findViewById(R.id.watermelon2);
        bar4 = (MyColorImageView) findViewById(R.id.bar4);
        watermelon3 = (MyColorImageView) findViewById(R.id.watermelon3);
        ring3 = (MyColorImageView) findViewById(R.id.ring3);
        seven3 = (MyColorImageView) findViewById(R.id.seven3);
        orange3 = (MyColorImageView) findViewById(R.id.orange3);
        cherry3 = (MyColorImageView) findViewById(R.id.cherry3);
        bar3 = (MyColorImageView) findViewById(R.id.bar3);


        imageList.add(bar1);
        imageList.add(cherry1);
        imageList.add(orange1);
        imageList.add(seven1);
        imageList.add(ring1);
        imageList.add(watermelon1);

        imageList.add(bar2);
        imageList.add(cherry2);
        imageList.add(orange2);
        imageList.add(seven2);
        imageList.add(ring2);
        imageList.add(watermelon2);

        imageList.add(bar3);
        imageList.add(cherry3);
        imageList.add(orange3);
        imageList.add(seven3);
        imageList.add(ring3);
        imageList.add(watermelon3);

        imageList.add(bar4);
        imageList.add(cherry4);
        imageList.add(orange4);
        imageList.add(seven4);
        imageList.add(ring4);
        imageList.add(watermelon4);
    }

    private void initImageList()
    {
        count = -1;
        for(int i=0;i<imageList.size();i++)
        {
            imageList.get(i).setBackgroundColor(Color.parseColor("#ffffffff"));
        }

    }
}
