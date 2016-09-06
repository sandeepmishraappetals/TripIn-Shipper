package tripin.com.tripin_shipper.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tripin.com.tripin_shipper.R;
import tripin.com.tripin_shipper.SharedPreference.SharedPreference;
import tripin.com.tripin_shipper.helper.SessionManager;
import tripin.com.tripin_shipper.model.User;
import tripin.com.tripin_shipper.volley.AppController;
import tripin.com.tripin_shipper.volley.Config_URL;
import tripin.com.tripin_shipper.widget.UI.MyDialog;

/**
 * Created by Android on 28/07/16.
 */
public class Activity_phoneNumberVerification extends Activity implements View.OnClickListener{
    private static final String TAG = Activity_phoneNumberVerification.class.getSimpleName();
private Button Bt_changeNumber;
    private TextView Text_mobNo;
    private Button bt_resend, bt_changeNumber;
    private Button btnResend;
    private Button btnChange;
    private Button ok;
    private EditText inputOTP;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private String u_id, mobile_no;
    //check for Change Password parameter
    private String is_resetPassword;
    private ImageButton back;
    Activity context = this;
    private SharedPreference sharedPreference;
    String mob;
    EditText userInput;
    MyDialog mydialog;
    //Forgot Password
    String newPassword, ConfirmPassword;
    public String Access_Token;
    //End Forgot Password
private User user;
boolean isResetPassword = false;
    public MyDialog.myOnClickListener myListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneverification_header);
        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Access_Token=(mSharedPreference.getString("Token", null));
        Bt_changeNumber = (Button)findViewById(R.id.bt_changeNo);
        Bt_changeNumber.setOnClickListener(this);

        Text_mobNo = (TextView) findViewById(R.id.textView_mobile);
        bt_resend =(Button)findViewById(R.id.bt_resendOtp);
        back = (ImageButton) findViewById(R.id.bt_back);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            u_id = extras.getString("key");
            mobile_no = extras.getString("mobile");
            is_resetPassword = extras.getString("isForgotPassword");
            //The key argument here must match that used in the other activity
        }
        inputOTP = (EditText) findViewById(R.id.editText_OTP);

        btnResend = (Button) findViewById(R.id.bt_resendOtp);
        btnChange = (Button) findViewById(R.id.bt_changeNo);
        ok = (Button) findViewById(R.id.ok);
        Text_mobNo.setText(mobile_no);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        sharedPreference = new SharedPreference();


        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Activity_phoneNumberVerification.this, Activity_phoneNumberVerification.class);
            startActivity(intent);
            finish();
        }
back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i = new Intent(getApplicationContext(), Activity_Main.class);
        startActivity(i);
        finish();
    }
});
        // Login button Click Event
        ok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String otp = inputOTP.getText().toString();
                String uid = u_id.toString();

                // Check for empty data in the form
                if (otp.trim().length() > 0 && uid.trim().length()>0) {
                    // login user
                    checkOTP(otp, uid);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the otp!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnResend.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String uid = u_id.toString();
                String mob = mobile_no.toString();

                // Check for empty data in the form
                if (mob.trim().length() > 0 && uid.trim().length()>0) {
                    // login user
                    checkResendOTP(mob, uid);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Error!", Toast.LENGTH_LONG)
                            .show();
                }
                Log.e("@@Resend Click","Resend" );
            }

        });

    }

    @Override
    public void onClick(View arg0) {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompt_changemobile, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

         userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("CONFIRM",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                Text_mobNo.setText(userInput.getText());
                                String uid = u_id.toString();
                                mob = userInput.toString();

                                // Check for empty data in the form
                                if (mob.trim().length() > 0 && uid.trim().length()>0 ) {
                                    // login user
                                    ChangeNumber(mob, uid);
                                } else {
                                    // Prompt user to enter credentials
                                    Toast.makeText(getApplicationContext(),
                                            "Error!", Toast.LENGTH_LONG)
                                            .show();
                                }
                                Log.e("@@Resend Click","Resend" );
                            }
                        });
                /*.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });*/

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
    /**
     * function to verify login details in mysql db
     * */
    private void checkOTP(final String otp, final String uid) {
        // Tag used to cancel the request
        String tag_string_req = "req_otp";

        pDialog.setMessage("otp in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.URL_OTP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Otp Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);

                    int status = jObj.getInt("status");
                    String error = jObj.getString("message");
                    JSONObject data = jObj.getJSONObject("data");
                    String username = data.optString("username");
                    final String user_id = data.optString("user_id");
                //    user.setUser_id(user_id);
                    // Check for error node in json
                  /*  if (error!=null)*/
                    if ( status == 1&& is_resetPassword==null)
                    {
                        // user successfully logged in
                        // Create login session

                       session.setLogin(true);

                       // Launch main activity
                        Intent intent = new Intent(Activity_phoneNumberVerification.this,
                                DashBoardActivity.class);
                        intent.putExtra("user_name",username);
                        startActivity(intent);
                        finish();




                    }
                   else if (status == 1 && is_resetPassword!=null ) {
                           isResetPassword = true;

                        // custom dialog
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.alert);
                        dialog.setTitle("Title...");


                        final EditText New_Pass = (EditText)dialog.findViewById(R.id.new_pass);
                        final EditText Confirm_Pass = (EditText)dialog.findViewById(R.id.confirm_pass);



                        final Button confirmPass = (Button) dialog.findViewById(R.id.bt_confirm);
                        Button resetPass = (Button) dialog.findViewById(R.id.bt_reset);
                        // if button is clicked, close the custom dialog
                        confirmPass.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isResetPassword == true) {
                                    newPassword = New_Pass.toString();
                                    ConfirmPassword = Confirm_Pass.toString();
                                    // Check for empty data in the form
                                    if (newPassword.trim().length() > 0 && ConfirmPassword.trim().length() > 0 && newPassword == ConfirmPassword) {

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

                        dialog.show();




                    }else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
              /*  Log.e(TAG, "OTP Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();*/
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "otp");
                params.put("otp", otp);
                params.put("user_id", uid);
                params.put("access_token",Access_Token);

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void checkResendOTP(final String mobile, final String uid) {
        // Tag used to cancel the request
        String tag_string_req = "req_otp";

        pDialog.setMessage("Resending otp in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.URL_REOTP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Otp Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    //  boolean error = jObj.getBoolean("error");

                    int status = jObj.getInt("status");
                    String error = jObj.getString("message");
                    JSONObject data = jObj.getJSONObject("data");
                    String username = data.optString("username");
                    // Check for error node in json
                  /*  if (error!=null)*/
                    if ( status == 1)
                    {
                        // user successfully logged in
                        // Create login session

                        session.setLogin(true);

                        /*// Launch main activity
                        Intent intent = new Intent(Activity_phoneNumberVerification.this,
                                Activity_Main.class);
                        startActivity(intent);
                        finish();*/
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "OTP Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "otp");
                params.put("mobile_number", mobile);
                params.put("user_id", uid);
                params.put("access_token",Access_Token);

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void ChangeNumber(final String otp, final String uid) {
        // Tag used to cancel the request
        String tag_string_req = "req_otp";

        pDialog.setMessage("otp in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.URL_CHANGE_NUMBER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Otp Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    //  boolean error = jObj.getBoolean("error");

                    int status = jObj.getInt("status");
                    String error = jObj.getString("message");
                    JSONObject data = jObj.getJSONObject("data");
                    String username = data.optString("username");
                    String user_id = data.optString("user_id");
                    // Check for error node in json
                  /*  if (error!=null)*/
                    if ( status == 1)
                    {
                        // user successfully logged in
                        // Create login session

                     //   session.setLogin(true);

                       /* // Launch main activity
                        Intent intent = new Intent(Activity_phoneNumberVerification.this,
                                Activity_phoneNumberVerification.class);
                        intent.putExtra("user_name",username);
                        startActivity(intent);
                        finish();*/
                        Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG).show();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "OTP Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "otp");
                params.put("mobile_number", String.valueOf(userInput.getText()));
                params.put("user_id", uid);
                params.put("access_token",Access_Token);

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void ResetPassword(final String uid, final String newPassword, final String ConfirmPassword) {
        try {
            // Tag used to cancel the request
            String tag_string_req = "req_otp";

            pDialog.setMessage("otp in ...");
            showDialog();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Config_URL.URL_RESET_PASSWORD, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Otp Response: " + response.toString());
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        //  boolean error = jObj.getBoolean("error");

                        int status = jObj.getInt("status");
                        String error = jObj.getString("message");

                        // Check for error node in json
                  /*  if (error!=null)*/
                        if (status == 1) {
                            // user successfully logged in
                            // Create login session

                            //   session.setLogin(true);

                            // Launch main activity
                            Intent intent = new Intent(Activity_phoneNumberVerification.this,
                                    Activity_Main.class);

                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(),
                                    error, Toast.LENGTH_LONG).show();
                        } else {
                            // Error in login. Get the error message
                            String errorMsg = jObj.getString("message");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "OTP Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("tag", "otp");
                    params.put("new_password", String.valueOf(newPassword));
                    params.put("confirm_password", ConfirmPassword);
                    params.put("user_id", uid);
                    params.put("access_token",Access_Token);

                    return params;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

                    return headers;
                }
                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };
            strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
        catch (Exception ex)
        {
            ex.toString();
        }
        }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}

