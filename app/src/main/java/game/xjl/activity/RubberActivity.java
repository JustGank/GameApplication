package game.xjl.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import game.xjl.R;
import game.xjl.rubber_textview.ScratchTextView;

/**
 * 项目名称：GameApplication
 * 类描述：
 * 创建人：xjl
 * 创建时间：2016/10/13 11:05
 * 修改人：Administrator
 * 修改时间：2016/10/13 11:05
 * 修改备注：
 */
public class RubberActivity extends Activity {

    private ScratchTextView my_rubber_view;
    private String mSentence="一等奖";
    private TextView tv_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rabber);

        my_rubber_view=(ScratchTextView)findViewById(R.id.my_rubber_view);
        my_rubber_view.initScratchCard(0xFFCECED1,30,1f);

        my_rubber_view.setText(mSentence);

        my_rubber_view.setOnResultOutput(new ScratchTextView.OnResultOutput() {
            @Override
            public void onResultOutput() {
                Toast.makeText(RubberActivity.this,"恭喜您中奖了",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
