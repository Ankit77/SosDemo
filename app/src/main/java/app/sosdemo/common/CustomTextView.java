package app.sosdemo.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class CustomTextView extends TextView {

    private Typeface fontType;

    /**
     * Set the font according to style
     *
     * @param context
     * @param attrs   style attribute like bold, italic
     */
    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode())
            return;
//        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
//        final String fontName = ta.getString(R.styleable.CustomTextView_font_name);
//        final Typeface font = Typeface.createFromAsset(context.getAssets(), "font/" + fontName);
//        setTypeface(font);
//        ta.recycle();
    }

}