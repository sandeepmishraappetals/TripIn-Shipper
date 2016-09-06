package tripin.com.tripin_shipper.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import tripin.com.tripin_shipper.*;

import tripin.com.tripin_shipper.model.User;
import tripin.com.tripin_shipper.volley.Config_URL;

/**
 * Created by Android on 16/08/16.
 */
public class Activity_forgot extends AppCompatActivity implements View.OnClickListener{
    private Button ok;
   // private static final String REGISTER_URL = "http://simplifiedcoding.16mb.com/UserRegistration/volleyRegister.php";

    public static final String KEY_USERID = "user_id";
    public static final String KEY_NEWPASSWORD = "new_password";
    public static final String KEY_CONFIRMPASSWORD = "confirm_password";

    private String u_id, mobile_no;
    private EditText editTextOTP;
    private User user;
    private Button buttonRegister;

    Activity context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneforgot_pass);
        editTextOTP = (EditText) findViewById(R.id.editText_OTP);
        ok.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            u_id = extras.getString("key");
            mobile_no = extras.getString("mobile");

            //The key argument here must match that used in the other activity
        }
        String user_id = null;
        user.setUser_id(u_id);
    }

    @Override
    public void onClick(View v) {
ResetPassword();
    }

    private void ResetPassword() {
       final boolean isResetPassword = true;
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.alert);
        dialog.setTitle("Title...");


        final EditText New_Pass = (EditText)dialog.findViewById(R.id.new_pass);
        final EditText Confirm_Pass = (EditText)dialog.findViewById(R.id.confirm_pass);



        final Button confirmPass = (Button) dialog.findViewById(R.id.bt_confirm);
        Button resetPass = (Button) dialog.findViewById(R.id.bt_reset);
        final String otp = editTextOTP.getText().toString().trim();
        final String newPass = New_Pass.getText().toString().trim();
        final String Cpassword = Confirm_Pass.getText().toString().trim();
        confirmPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isResetPassword == true) {

                    // Check for empty data in the form
                    if (newPass.trim().length() > 0 && Cpassword.trim().length() > 0 && newPass == Cpassword) {

                        // ResetPassword(user_id, newPassword, ConfirmPassword);
                        Intent forgot = new Intent(context, Activity_forgot.class);
                        startActivity(forgot);
                    } else {
                        // Prompt user to enter credentials
                        Toast.makeText(getApplicationContext(),
                                "Error!", Toast.LENGTH_LONG)
                                .show();
                    }

                    dialog.dismiss();
                }
            }


        });
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                New_Pass.setText("");
                Confirm_Pass.setText("");
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config_URL.URL_RESET_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Activity_forgot.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Activity_forgot.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put(KEY_USERID,u_id);
                params.put(KEY_NEWPASSWORD,newPass);
                params.put(KEY_CONFIRMPASSWORD, Cpassword);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}