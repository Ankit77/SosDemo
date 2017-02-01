package app.sosdemo.common;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import app.sosdemo.KavachApp;
import app.sosdemo.util.Constant;

/**
 * Created by indianic on 01/02/17.
 */

public class CustomEditText extends EditText {
    public CustomEditText(Context context) {
        super(context);
        setFont(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context);
    }

    private void setFont(Context context) {
        if (KavachApp.getInstance().getPref().getString(Constant.PREF_LANGUAGE, Constant.LANGUAGE_ENGLISH).equalsIgnoreCase(Constant.LANGUAGE_GUJRATI)) {
            Typeface face = Typeface.createFromAsset(context.getAssets(),
                    "fonts/gujarati.ttf");
            setTypeface(face);
        }
    }
}
