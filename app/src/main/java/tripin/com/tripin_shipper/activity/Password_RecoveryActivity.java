package tripin.com.tripin_shipper.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import tripin.com.tripin_shipper.R;

/**
 * Created by Appetals Solutions on 27-07-2016.
 */
public class Password_RecoveryActivity extends Activity {
    private EditText OTP;
    private int keyDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneverification_header);

        OTP.addTextChangedListener(new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            OTP.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (keyCode == KeyEvent.KEYCODE_DEL)
                        keyDel = 1;
                    return false;
                }
            });

            if (keyDel == 0) {
                int len = OTP.getText().length();
                if(len == 3) {
                    OTP.setText(OTP.getText() + "-");
                    OTP.setSelection(OTP.getText().length());
                }
            } else {
                keyDel = 0;
            }
        }

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    });
}
}