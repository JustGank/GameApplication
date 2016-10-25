package game.xjl.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import game.xjl.Bean.BonusBean;
import game.xjl.R;
import game.xjl.draw_card.RotationPanelView;

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

    private void initAnimationAndSetRotationCount(int count) {
        Random random = new Random();
        randomBonusResult = random.nextInt(bonusBeens.size()) + 1;
        randomBonusRadio = random.nextFloat();
        float tempFloat = 0.0f;
        for (int i = 0; i < randomBonusResult; i++) {
            tempFloat = tempFloat + bonusBeens.get(i).getBonusRates();
        }
        tempFloat = tempFloat - bonusBeens.get(randomBonusResult - 1).getBonusRates() * randomBonusRadio;
        tempFloat = 360.0f * tempFloat;
        ObjectAnimator animator = ObjectAnimator.ofFloat(rotation_panel_view, "rotation", 0f,- (360f * count + tempFloat));
        if (count == 0) {
            animator.setDuration(1000);
        } else {
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
