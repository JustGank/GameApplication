package game.xjl.slot_machine;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 项目名称：GameApplication
 * 类描述：
 * 创建人：xjl
 * 创建时间：2016/10/17 14:11
 * 修改人：Administrator
 * 修改时间：2016/10/17 14:11
 * 修改备注：
 */

public class MyColorImageView extends ImageView {
    public MyColorImageView(Context context) {
        super(context);
    }

    public MyColorImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyColorImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    private String color;

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
       setBackgroundColor(Color.parseColor(color));
    }


}
