package tripin.com.tripin_shipper.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewLatoRegular extends TextView {

    private static final String TAG = TextViewLatoRegular.class.getName();
    private static final String FONT = "Lato-Regular.ttf";

    Context context;

    public TextViewLatoRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        init();
    }

    public TextViewLatoRegular(Context context) {
        super(context);
        this.context = context;

        init();
    }

    public TextViewLatoRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;

        init();
    }

    private void init() {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/" + FONT);
        setTypeface(font);
    }

    @Override
    public void setTypeface(Typeface tf) {
        super.setTypeface(tf);
    }
}

