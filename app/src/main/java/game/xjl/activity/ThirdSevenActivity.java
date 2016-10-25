package game.xjl.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import game.xjl.R;
import game.xjl.third_seven.ISlotMachineItem;
import game.xjl.third_seven.PickerView;
import game.xjl.third_seven.TypefaceSpan;
import game.xjl.third_seven.WheelView;

/**
 * 项目名称：GameApplication
 * 类描述：
 * 创建人：xjl
 * 创建时间：2016/10/18 14:37
 * 修改人：Administrator
 * 修改时间：2016/10/18 14:37
 * 修改备注：
 */

public class ThirdSevenActivity extends Activity {



    //以下为数字版控件
    protected PickerView minute_pv;
    protected TextView minuteTv;
    protected PickerView second_pv;
    protected TextView secondTv;

    //以下为图片版控件
    private final String ROBOTO_BOLD="Roboto-Bold.ttf";
    private final String VAREL_REGULAR = "VarelaRound-Regular.ttf";

    private final int SPIN_TIME = 2500;
    private final int MESSAGE_CHECK_MATCH = 0;

    protected WheelView wheelView1;
    protected WheelView wheelView2;
    protected WheelView wheelView3;

    List<ISlotMachineItem> slotItems1;
    List<ISlotMachineItem> slotItems2;
    List<ISlotMachineItem> slotItems3;

    Random random;
    TextView mResultsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_third_seven);
        initNumeberView();
        initSlotMachine();
    }

    // Slot Item images
    private final int slotItem1Images[] = new int[]{
            R.mipmap.bar,
            R.mipmap.cherry,
            R.mipmap.orange,
            R.mipmap.ring,
            R.mipmap.seven,
            R.mipmap.watermelon
    };

    private final int slotItem2Images[] = new int[]{
            R.mipmap.bar,
            R.mipmap.cherry,
            R.mipmap.orange,
            R.mipmap.ring,
            R.mipmap.seven,
            R.mipmap.watermelon
    };

    private final int slotItem3Images[] = new int[]{
            R.mipmap.bar,
            R.mipmap.cherry,
            R.mipmap.orange,
            R.mipmap.ring,
            R.mipmap.seven,
            R.mipmap.watermelon
    };

    private int wheel1Selection;
    private int wheel2Selection;
    private int wheel3Selection;

    Typeface varelRegularFont;
    Typeface robotoBoldFont;

    Button spin;

    MediaPlayer mediaPlayer;

    private void initSlotMachine()
    {
        random = new Random();
        mediaPlayer = MediaPlayer.create(this, R.raw.slot_level_pull);

        robotoBoldFont = Typeface.createFromAsset(getAssets(),ROBOTO_BOLD );
        varelRegularFont = Typeface.createFromAsset(getAssets(),VAREL_REGULAR );

        TextView mWelcomeText = (TextView) findViewById(R.id.welcomeTxt);
        mWelcomeText.setTypeface(robotoBoldFont);

        mResultsText = (TextView) findViewById(R.id.resultTxt);
        mResultsText.setTypeface(robotoBoldFont);
        mResultsText.setVisibility(View.INVISIBLE);

        wheelView1 = (WheelView) findViewById(R.id.wheel1);
        wheelView2 = (WheelView) findViewById(R.id.wheel2);
        wheelView3 = (WheelView) findViewById(R.id.wheel3);

        //Build the slot items for each wheel.
        slotItems1 = new ArrayList<ISlotMachineItem>();
        slotItems2 = new ArrayList<ISlotMachineItem>();
        slotItems3 = new ArrayList<ISlotMachineItem>();

        //Set the slot items for each wheels.
        slotItems1.add(new SlotItemsImpl(1,0));
        slotItems1.add(new SlotItemsImpl(1,1));
        slotItems1.add(new SlotItemsImpl(1,2));
        slotItems1.add(new SlotItemsImpl(1,3));
        slotItems1.add(new SlotItemsImpl(1,4));
        slotItems1.add(new SlotItemsImpl(1,5));
        wheelView1.setSlotItems(slotItems1);

        slotItems2.add(new SlotItemsImpl(2,0));
        slotItems2.add(new SlotItemsImpl(2,1));
        slotItems2.add(new SlotItemsImpl(2,2));
        slotItems2.add(new SlotItemsImpl(2,3));
        slotItems2.add(new SlotItemsImpl(2,4));
        slotItems2.add(new SlotItemsImpl(2,5));
        wheelView2.setSlotItems(slotItems2);

        slotItems3.add(new SlotItemsImpl(3,0));
        slotItems3.add(new SlotItemsImpl(3,1));
        slotItems3.add(new SlotItemsImpl(3,2));
        slotItems3.add(new SlotItemsImpl(3,3));
        slotItems3.add(new SlotItemsImpl(3,4));
        slotItems3.add(new SlotItemsImpl(3,5));
        wheelView3.setSlotItems(slotItems3);

        wheelView1.setNumberOfVisibleItems(3);
        wheelView2.setNumberOfVisibleItems(3);
        wheelView3.setNumberOfVisibleItems(3);

        wheelView1.setWheelBackground(getResources().getDrawable(R.drawable.wheel_frame));
        wheelView2.setWheelBackground(getResources().getDrawable(R.drawable.wheel_frame));
        wheelView3.setWheelBackground(getResources().getDrawable(R.drawable.wheel_frame));

        wheelView1.setScrollFinishedListener(new WheelView.OnScrollFinishedListener() {
            @Override
            public void onWheelFinishedScrolling(int position) {
                wheel1Selection = position;
            }
        });

        wheelView2.setScrollFinishedListener(new WheelView.OnScrollFinishedListener() {
            @Override
            public void onWheelFinishedScrolling(int position) {
                wheel2Selection = position;
            }
        });

        wheelView3.setScrollFinishedListener(new WheelView.OnScrollFinishedListener() {
            @Override
            public void onWheelFinishedScrolling(int position) {
                wheel3Selection = position;
            }
        });

        spin = (Button) findViewById(R.id.spin);
        spin.setTypeface(varelRegularFont);

        spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                spin.setEnabled(false);

                // Vary the time and distance range to obtain
                // randomness of wheel scrolling.
                int randomMultipler = random.nextInt(9);
                wheelView1.scroll(4000 + ( 100 * randomMultipler), SPIN_TIME);

                randomMultipler = random.nextInt(9);
                wheelView2.scroll(4000 + ( 100 * randomMultipler), SPIN_TIME);

                randomMultipler = random.nextInt(9);
                wheelView3.scroll(4000 + ( 100 * randomMultipler), SPIN_TIME);

                Message msg = Message.obtain();
                msg.what = MESSAGE_CHECK_MATCH;
                detectAnyMatchHandler.sendMessageDelayed(msg, SPIN_TIME + 1000);
            }
        });

        // Set the width of the Wheels based on total screen width.
        // So that it is well calculated on all devices.
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        final float density = getResources().getDisplayMetrics().density;
        int marginBetweenWheels = (int) (5 * density + 0.5f);
        int wheelWidth;
        int wheelHeight;
        boolean isPortrait = false;;

        if (width > height) {
            //Landscape: Take 70% of screen width
            width = (width * 65 )/100;
            wheelHeight = (height * 60)/100;
        } else {
            isPortrait = true;
            //Portrait: Take 90% of screen width
            width = (width * 90 )/100;
            wheelHeight = (height * 40)/100;
        }

        //Subtract the margin between two wheels
        width -= ( 2 * marginBetweenWheels);

        //Divide the total number of wheels ( which is 3 for this slot machine)
        wheelWidth = width / 3;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) wheelView1.getLayoutParams();
        params.width = wheelWidth;
        params.height = wheelHeight;

        params = (LinearLayout.LayoutParams) wheelView2.getLayoutParams();
        params.width = wheelWidth;
        params.height = wheelHeight;
        params.leftMargin = marginBetweenWheels;

        params = (LinearLayout.LayoutParams) wheelView3.getLayoutParams();
        params.width = wheelWidth;
        params.height = wheelHeight;
        params.leftMargin = marginBetweenWheels;

        View thickLine1 = (View) findViewById(R.id.thickLine1);
        View thickLine2 = (View) findViewById(R.id.thickLine2);
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) thickLine1.getLayoutParams();
        params1.width = width + ( 2 * marginBetweenWheels);

        params1 = (RelativeLayout.LayoutParams) thickLine2.getLayoutParams();
        params1.width = width + ( 2 * marginBetweenWheels);

        if (isPortrait == false) {
            //If landscape set the width for result layout
            params1 = (RelativeLayout.LayoutParams) ((View) findViewById(R.id.slotSpinLayout)).getLayoutParams();
            params1.width = wheelWidth + wheelWidth/2;
        }
    }

    private Handler detectAnyMatchHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_CHECK_MATCH) {
                spin.setEnabled(true);
                if (wheel1Selection ==1 && wheel2Selection == 1 && wheel3Selection == 1) {
                    //Coffee MATCH
                    setSuccessResultMessage(1);
                } else if (wheel1Selection == 2 && wheel2Selection == 2 && wheel3Selection == 2) {
                    //Tea MATCH
                    setSuccessResultMessage(2);
                } else if (wheel1Selection == 3 && wheel2Selection == 3 && wheel3Selection == 3) {
                    //Espresso MATCH
                    setSuccessResultMessage(3);
                } else {
                    mResultsText.setVisibility(View.VISIBLE);
                    mResultsText.setText(ThirdSevenActivity.this.getResources().getString(R.string.try_again_text));
                }
            }
        }
    };

    //Concrete implementation of interface ISlotMachineItem
    class SlotItemsImpl implements ISlotMachineItem {

        int wheelPos;
        int slotItemPos;

        public SlotItemsImpl(
                int wheelPos,
                int slotItemPos) {
            this.wheelPos = wheelPos;
            this.slotItemPos = slotItemPos;
        }

        @Override
        public View getView() {
            View view = (View) getLayoutInflater().inflate(R.layout.slot_item_layout, null, false);
            ImageView itemImageView = (ImageView) view.findViewById(R.id.itemImage);
            TextView itemTextView = (TextView) view.findViewById(R.id.itemTxt);
            itemTextView.setTypeface(varelRegularFont);

            Resources resources = getResources();
            if (wheelPos == 1) {
                itemImageView.setImageResource(slotItem1Images[slotItemPos]);
                //itemTextView.setText(resources.getString(slotItem1Texts[slotItemPos]));
            } else if (wheelPos == 2) {
                itemImageView.setImageResource(slotItem2Images[slotItemPos]);
                //itemTextView.setText(resources.getString(slotItem2Texts[slotItemPos]));
            } else if (wheelPos == 3) {
                itemImageView.setImageResource(slotItem3Images[slotItemPos]);
               // itemTextView.setText(resources.getString(slotItem3Texts[slotItemPos]));
            }else if(wheelPos == 4)
            {
                itemImageView.setImageResource(slotItem3Images[slotItemPos]);
            }else if(wheelPos == 5)
            {
                itemImageView.setImageResource(slotItem3Images[slotItemPos]);
            }else if(wheelPos == 6)
            {
                itemImageView.setImageResource(slotItem3Images[slotItemPos]);
            }
            return view;
        }
    }

    public SpannableString getActionBarSpannableTitle(String title) {
        SpannableString span = new SpannableString(title);
        span.setSpan(new TypefaceSpan(this, ROBOTO_BOLD ), 0, title.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    private void setSuccessResultMessage(int position) {
        mResultsText.setVisibility(View.VISIBLE);
        String resultString = null;
        int startPos = 0;
        int endPos = 0;
        String beverage = null;
        Resources res = getResources();

        if (position == 1) {
            beverage = res.getString(R.string.beverage_coffee);

        } else if (position == 2) {
            beverage = res.getString(R.string.beverage_tea);

        } else if (position == 3) {
            beverage = res.getString(R.string.beverage_espresso);
        } else {
            mResultsText.setVisibility(View.INVISIBLE);
            return;
        }

        resultString = String.format(res.getString(R.string.result_text), beverage);
        startPos = resultString.indexOf(beverage, 0);
        endPos = startPos + beverage.length();

        Spannable wordToSpan = new SpannableString(resultString);
        wordToSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.button_start_color)), startPos,
                endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordToSpan.setSpan(new RelativeSizeSpan(1.5f), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mResultsText.setText(wordToSpan);

    }



    private void initNumeberView() {
        minute_pv = (PickerView) findViewById(R.id.minute_pv);
        minuteTv = (TextView) findViewById(R.id.minute_tv);
        second_pv = (PickerView) findViewById(R.id.second_pv);
        secondTv = (TextView) findViewById(R.id.second_tv);
        List<String> data = new ArrayList<String>();
        List<String> seconds = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            data.add("0" + i);
        }
        for (int i = 0; i < 60; i++) {
            seconds.add(i < 10 ? "0" + i : "" + i);
        }
        minute_pv.setData(data);
        minute_pv.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                Toast.makeText(ThirdSevenActivity.this, "选择了 " + text + " 分",
                        Toast.LENGTH_SHORT).show();
            }
        });
        second_pv.setData(seconds);
        second_pv.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                Toast.makeText(ThirdSevenActivity.this, "选择了 " + text + " 秒",
                        Toast.LENGTH_SHORT).show();
            }
        });

        ValueAnimator va = ValueAnimator.ofFloat(0.0f, 5000f);
        va.setDuration(5000);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                minute_pv.doMove((float) animation.getAnimatedValue());

            }
        });

        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                minute_pv.doUp();
            }
        });
        va.start();
    }


}
