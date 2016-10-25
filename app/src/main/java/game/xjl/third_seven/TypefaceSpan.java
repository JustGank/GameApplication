package game.xjl.third_seven;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.util.LruCache;

/**
 * 项目名称：GameApplication
 * 类描述：
 * 创建人：xjl
 * 创建时间：2016/10/21 14:31
 * 修改人：Administrator
 * 修改时间：2016/10/21 14:31
 * 修改备注：
 */

public class TypefaceSpan extends MetricAffectingSpan {

    private static LruCache<String, Typeface> sTypefaceCache = new LruCache<String, Typeface>(12);

    private Typeface mTypeface;

    public TypefaceSpan(Context context, String typefaceName)
    {
        mTypeface = sTypefaceCache.get(typefaceName);

        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
                    typefaceName);
            // Cache the loaded Typeface
            sTypefaceCache.put(typefaceName, mTypeface);
        }

    }



    @Override
    public void updateMeasureState(TextPaint p) {
        p.setTypeface(mTypeface);
        // Note: This flag is required for proper typeface rendering
        p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setTypeface(mTypeface);
        // Note: This flag is required for proper typeface rendering
        tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }
}
