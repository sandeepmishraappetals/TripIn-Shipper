package tripin.com.tripin_shipper.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import tripin.com.tripin_shipper.R;
import tripin.com.tripin_shipper.model.User;
import tripin.com.tripin_shipper.AppController;
import tripin.com.tripin_shipper.volley.Config_URL;

public class Activity_Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1500;
    private static final String TAG = Activity_Splash.class.getSimpleName();
    String Access_Token;
private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity__splash);
     //   user = new User();

        new Handler().postDelayed(new Runnable(){
 /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            String client_id = "testclient";
         String grant_type = "client_credentials";
          String client_secret = "testpass";
                @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(Activity_Splash.this, Activity_Main.class);

                Oauth(client_id, grant_type, client_secret);

                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    /**
     * function to verify login details in mysql db
     * */
    private void Oauth(final String client_id, final String grant_type, final String client_secret ) {
        // Tag used to cancel the request
        String tag_string_req = "Oauth";

       /* pDialog.setMessage("Logging in ...");
        showDialog();*/

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.URL_OAUTH, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Oauth Response: " + response.toString());
               // hideDialog();

                try {
              //      User user = new User();
                    JSONObject jObj = new JSONObject(response);
                    //  boolean error = jObj.getBoolean("error");
                    String token = jObj.getString("access_token");
                    int expires_in = jObj.getInt("expires_in");
                    String token_type = jObj.optString("token_type");
                    String scope = jObj.optString("scope");
               //   Access_Token=  user.setAccess_token(token);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Activity_Splash.this);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("Token", token);
                    editor.commit();
                }
                    catch (Exception ex)
                    {
                        ex.toString();
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Oauth Error: " + error.getMessage());
               /* Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();*/
                // As of f605da3 the following should work
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
            }
        })

        {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("client_id", "testclient");
                params.put("grant_type", "client_credentials");
                params.put("client_secret", "testpass");
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
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
