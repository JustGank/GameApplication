package game.xjl.slot_machine;

import android.animation.TypeEvaluator;

/**
 * 项目名称：GameApplication
 * 类描述：
 * 创建人：xjl
 * 创建时间：2016/10/17 14:01
 * 修改人：Administrator
 * 修改时间：2016/10/17 14:01
 * 修改备注：
 */

public class ColorEvaluator implements TypeEvaluator {
    String startColor;
    String endColor;


    @Override
    public String evaluate(float fraction, Object startColor, Object endColor) {
        this.startColor=(String)startColor;
        this.endColor=(String)endColor;

        int startAlaph=Integer.parseInt("00",16);
        int endAlaph=Integer.parseInt("ff",16);

        int currentColor=(int)((endAlaph-startAlaph)*fraction);

        return "#"+getHexString(currentColor)+startColor;
    }

    private String getHexString(int value) {
        String hexString = Integer.toHexString(value);
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        return hexString;
    }

}
