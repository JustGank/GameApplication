package game.xjl.activity;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

import game.xjl.R;
import game.xjl.shake.ShakeListener;

/**
 * 项目名称：GameApplication
 * 类描述：
 * 创建人：xjl
 * 创建时间：2016/10/24 15:54
 * 修改人：Administrator
 * 修改时间：2016/10/24 15:54
 * 修改备注：
 */

public class ShakeActivity extends Activity {


    protected RelativeLayout mImgUp;
    protected RelativeLayout mImgDn;

    private SoundPool sndPool;
    private HashMap<Integer, Integer> soundPoolMap = new HashMap<>();

    ShakeListener mShakeListener = null;
    //获取振动器
    Vibrator mVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_shake_shake);
        initView();
        initParamsAndFunction();
    }

    private void initView() {

        mImgUp = (RelativeLayout) findViewById(R.id.shakeImagUp);
        mImgDn = (RelativeLayout) findViewById(R.id.shakeImagDown);


    }

    private void initParamsAndFunction()
    {
        mVibrator=(Vibrator)getApplication().getSystemService(VIBRATOR_SERVICE);
        loadSound();
        mShakeListener=new ShakeListener(this);
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                startAnim();
                startVibrato();
                mShakeListener.stop();
                /**
                 * 这几个参数的意思为
                 * 1.声音资源ID
                 * 2.左声道 声音大小
                 * 3.右声道 声音大小
                 * 4.声音质量
                 * 5.是否循环
                 * 6.播放速度
                 * */
                sndPool.play(soundPoolMap.get(0),1.0f,1.0f,1,0,1.2f);
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        sndPool.play(soundPoolMap.get(1), (float) 1, (float) 1, 0, 0,(float) 1.0);
                        Toast mtoast;
                        mtoast = Toast.makeText(getApplicationContext(),
                                "抱歉，暂时没有找到\n在同一时刻摇一摇的人。\n再试一次吧！", Toast.LENGTH_SHORT);
                        //mtoast.setGravity(Gravity.CENTER, 0, 0);
                        mtoast.show();
                        mVibrator.cancel();
                        mShakeListener.start();
                    }
                },2000);
            }
        });

    }

    private void loadSound() {
        /**
         *在Android开发中经常需要播放多媒体音频文件，那么通常会使用MediaPlayer类来执行
         *但是MediaPlayer类占用的系统资源比较多，对于游戏等应用
         * 而言就会带来性能上的降低。在Android中专门提供了SoundPoollei
         * 来执行此类音频文件播放。
         * SoundPool的优点是占用CPU较少反应快.
         */
        sndPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
        new Thread() {
            @Override
            public void run() {
                try {
                    //soundPool会返回当前加载音频的ID
                    soundPoolMap.put(0
                            , sndPool.load(getAssets().openFd("sound/shake_sound_male.mp3"), 1));
                    soundPoolMap.put(1
                            , sndPool.load(getAssets().openFd("sound/shake_match.mp3"), 1));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void startAnim() {
        AnimationSet animup = new AnimationSet(true);
        //设置移动动画，相对于自身的位置  向上移动相当于自身高度50%的大小
        TranslateAnimation mytranslateanimup0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -0.5f);
        TranslateAnimation mytranslateanimup1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, +0.5f);
        //0先开始在1秒内完成 1动画延迟一秒后启动把现在的图片移动回来
        mytranslateanimup0.setDuration(1000);
        mytranslateanimup1.setDuration(1000);
        mytranslateanimup1.setStartOffset(1000);
        animup.addAnimation(mytranslateanimup0);
        animup.addAnimation(mytranslateanimup1);
        mImgUp.startAnimation(animup);

        AnimationSet animdn = new AnimationSet(true);
        TranslateAnimation mytranslateanimup2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, +0.5f);
        TranslateAnimation mytranslateanimup3 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -0.5f);
        mytranslateanimup2.setDuration(1000);
        mytranslateanimup3.setDuration(1000);
        mytranslateanimup3.setStartOffset(1000);
        animdn.addAnimation(mytranslateanimup2);
        animdn.addAnimation(mytranslateanimup3);
        mImgDn.startAnimation(animdn);
    }

    public void startVibrato(){
        //定义震动  第一个｛｝里面是节奏数组 第二个是重复的次数，-1为不重复
        mVibrator.vibrate(new long[]{500,200,500,200},-1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mShakeListener!=null)
        {
            mShakeListener.stop();
        }
    }
}
