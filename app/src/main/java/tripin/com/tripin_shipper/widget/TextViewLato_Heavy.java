package tripin.com.tripin_shipper.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Android on 06/09/16.
 */
public class TextViewLato_Heavy extends TextView {

    private static final String TAG = TextViewLato_Heavy.class.getName();
    private static final String FONT = "Lato-Black.ttf";

    Context context;

    public TextViewLato_Heavy(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        init();
    }

    public TextViewLato_Heavy(Context context) {
        super(context);
        this.context = context;

        init();
    }

    public TextViewLato_Heavy(Context context, AttributeSet attrs, int defStyle) {
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
