package tripin.com.tripin_shipper.widget.UI;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.android.volley.Response;

import tripin.com.tripin_shipper.R;

/**
 * Created by Android on 12/08/16.
 */
public class MyDialog extends Dialog {
Button confirm;
    public MyDialog(Response.Listener<String> context, myOnClickListener myclick) {
        super((Context) context);
        this.myListener = myclick;
    }


    public myOnClickListener myListener;

    // This is my interface //
    public interface myOnClickListener {
        void onButtonClick();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alert);

        Button reset = (Button) findViewById(R.id.bt_reset);
        confirm = (Button)findViewById(R.id.bt_confirm);
        confirm.setOnClickListener(new android.view.View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Log.e("@@Confirm Click", "Confirm Button");
            }
        });
        reset.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                myListener.onButtonClick(); // I am giving the click to the
                Log.e("@@REset Click", "RESET Button");

            }
        });
    }

}