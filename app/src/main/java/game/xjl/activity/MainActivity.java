package game.xjl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import game.xjl.R;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button golden_egg;
    private Button slot_machine;
    private Button lottery;
    private Button turntable;
    private Button shark;
    private Button three_seven;
    private Button draw_game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        golden_egg = (Button) findViewById(R.id.golden_egg);
        slot_machine = (Button) findViewById(R.id.slot_machine);
        lottery = (Button) findViewById(R.id.lottery);
        turntable = (Button) findViewById(R.id.turntable);
        shark = (Button) findViewById(R.id.shark);
        three_seven = (Button) findViewById(R.id.three_seven);
        draw_game = (Button) findViewById(R.id.draw_game);

        golden_egg.setOnClickListener(this);
        slot_machine.setOnClickListener(this);
        lottery.setOnClickListener(this);
        turntable.setOnClickListener(this);
        shark.setOnClickListener(this);
        three_seven.setOnClickListener(this);
        draw_game.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.golden_egg:
                startActivity(new Intent(this,GoldenEggActivity.class));
                break;
            case R.id.slot_machine:
                startActivity(new Intent(this,SlotMachineActivity.class));
                break;
            case R.id.lottery:
                startActivity(new Intent(this,RubberActivity.class));
                break;
            case R.id.turntable:
                startActivity(new Intent(this,RotationPanelActivity.class));
                break;
            case R.id.shark:
                startActivity(new Intent(this,ShakeActivity.class));
                break;
            case R.id.three_seven:
                startActivity(new Intent(this,ThirdSevenActivity.class));
                break;
            case R.id.draw_game:
                startActivity(new Intent(this,DrawCardActivity.class));
                break;


        }
    }
}
