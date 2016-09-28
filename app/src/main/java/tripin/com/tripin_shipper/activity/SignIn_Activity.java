package tripin.com.tripin_shipper.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import tripin.com.tripin_shipper.R;
import tripin.com.tripin_shipper.helper.SessionManager;
import tripin.com.tripin_shipper.model.User;
import tripin.com.tripin_shipper.utils.NetworkUtils;
import tripin.com.tripin_shipper.AppController;
import tripin.com.tripin_shipper.volley.Config_URL;

/**
 * Created by SUMEET on 07-08-2016.
 */
public class SignIn_Activity extends AppCompatActivity implements View.OnClickListener{
    // LogCat tag
    private static final String TAG = SignIn_Activity.class.getSimpleName();
    private static final String TAG_CANCEL = SignIn_Activity.class.getSimpleName();
    private static final String TAG_ERROR = SignIn_Activity.class.getSimpleName();
    private Button btnLogin;
    private ImageButton back;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private TextInputLayout mTextInputLayoutPassword;
    private TextInputLayout mTextInputLayoutEmail;
    private LinearLayout social_LL/*,activity_login_header*/;
    private ImageView google, fb;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private TextView forgot_password;
    View activity_login_header;
    private CoordinatorLayout coordinatorLayout;
    private User user;
    public String Access_Token;
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            displayMessage(profile);

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };
    // Creating Facebook CallbackManager Value
    public static CallbackManager callbackmanager;

    //end Facebook


    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                displayMessage1(newToken);
            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };
        setContentView(R.layout.activity_login);

        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Access_Token=(mSharedPreference.getString("Token", null));
     //   Log.e("@@Access Token", Access_Token);
        mTextInputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        mTextInputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.bt_login);
        social_LL = (LinearLayout)findViewById(R.id.social) ;
         activity_login_header = findViewById(R.id.login_header);
        back = (ImageButton)activity_login_header.findViewById(R.id.back_bt) ;
        google = (ImageView) social_LL.findViewById(R.id.imageView_google);
        fb = (ImageView) social_LL.findViewById(R.id.imageView_fb);
        forgot_password = (TextView) findViewById(R.id.Text_Forgot_Pass);
        forgot_password.setOnClickListener(this);
        google.setOnClickListener(this);
        fb.setOnClickListener(this);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
        //    Intent intent = new Intent(SignIn_Activity.this, DashBoardActivity.class);
            Intent intent = new Intent(SignIn_Activity.this, Activity_Dashboard.class);

            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                // Check for empty data in the form
                if (email.trim().length() > 0 && password.trim().length() > 0) {
                    // login user
                    checkLogin(email, password, Access_Token);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to back navigation
        back.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        Activity_Main.class);
                startActivity(i);
                finish();
            }
        });
        }


    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password, final String access_Token) {
        if (AppController.getInstance().isConnected(this))
        {
            // Tag used to cancel the request
            String tag_string_req = "req_login";

            pDialog.setMessage("Logging in ...");
            showDialog();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Config_URL.URL_LOGIN, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Login Response: " + response.toString());
                    hideDialog();

                    try {

                        JSONObject jObj = new JSONObject(response);
                        //  boolean error = jObj.getBoolean("error");
                        String error = jObj.getString("message");
                        int status = jObj.getInt("status");

                        try {


                        }

                        catch (Exception ex)
                        {
                            ex.toString();
                        }
                        // Check for error node in json
                  /*  if (error!=null)*/
                        if ( status == 1)
                        {
                            // user successfully logged in
                            // Create login session
                            JSONObject data = jObj.getJSONObject("data");
                            String user_id = data.optString("user_id");
                            User user = new User();
                            user.setUser_id(user_id);
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SignIn_Activity.this);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("UserId", user_id);
                            editor.commit();
                            session.setLogin(true);

                            // Launch main activity
                            Intent intent = new Intent(SignIn_Activity.this,
                                    Activity_Dashboard.class);
                               /* Activity_Address_page.class);*/
                            startActivity(intent);
                            finish();
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
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("tag", "login");
                    params.put("username", email);
                    params.put("password", password);
                    params.put("access_token",access_Token);

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

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imageView_google:
                if (!NetworkUtils.isNetworkConnectionAvailable(this)) {
                    NetworkUtils.showNoInternetDialog(this);
                } else {
                    //   signIn();
                  //  signInWithGplus();
                }
                break;
            case R.id.imageView_fb:
                onFblogin();
                break;

            case R.id.Text_Forgot_Pass:
            /*    String email = inputEmail.getText().toString();
                if (!NetworkUtils.isNetworkConnectionAvailable(this)) {
                    NetworkUtils.showNoInternetDialog(this);
                } else if (email.trim().length() > 0 && email.trim().contains("@")){*/
                String email = inputEmail.getText().toString();
                    onForgotPasword(email);

             /*   }
                else if (email == null){
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Invalid parameters", Snackbar.LENGTH_LONG);

                snackbar.show();
            }*/

                break;


        }
    }
    // Private method to handle Facebook login and callback
    private void onFblogin()
    {
        callbackmanager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));

        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("Success");
                        GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject json, GraphResponse response) {
                                        if (response.getError() != null) {
                                            // handle error
                                            System.out.println("ERROR");
                                        } else {
                                            System.out.println("Success");
                                            try {

                                                String jsonresult = String.valueOf(json);
                                                System.out.println("JSON Result"+jsonresult);

                                                String str_email = json.optString("email");
                                                String str_id = json.optString("id");
                                                String str_firstname = json.optString("first_name");
                                                String str_lastname = json.optString("last_name");

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }

                                    }

                                }).executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG_CANCEL,"On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG_ERROR,error.toString());
                    }
                });
    }

    private void onForgotPasword(final String email) {
        // Tag used to cancel the request
        String tag_string_req = "req_forgotPass";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.URL_FORGOT_PASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    //  boolean error = jObj.getBoolean("error");
                    String error = jObj.getString("message");
                    int status = jObj.getInt("status");
                    String user_id = null;
                    String   Mobile = null;
                    try {
                        JSONObject data = jObj.getJSONObject("data");
                         user_id = data.optString("user_id");
                        String username = data.optString("username");
                           Mobile = jObj.optString("is_mobile");
                    } catch (Exception ex) {
                        ex.toString();
                    }
                    // Check for error node in json
                  /*  if (error!=null)*/
                    if (status == 1)
                    {
                        if (Mobile.trim().contentEquals("y")) {

                            // Launch main activity
                           Intent intent = new Intent(SignIn_Activity.this,Activity_phoneNumberVerification.class);

                            intent.putExtra("key", user_id);
                            intent.putExtra("isForgotPassword", "Y");
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            // Launch main activity
                            Intent intent = new Intent(SignIn_Activity.this,Activity_Main.class);
                          //  intent.putExtra("key", user_id);
                            startActivity(intent);
                            finish();

                        }
                    }


                    else {
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
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "Forgot_Password");
                params.put("username", email);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackmanager.onActivityResult(requestCode, resultCode, data);
      /* if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }*/
      //  Intent i = new Intent(this, DashBoardActivity.class);

     Intent i = new Intent(this, Activity_Maps.class);
        startActivity(i);
        finish();
    }
    private void displayMessage(Profile profile){
        if(profile != null){
            //  textView.setText(profile.getName());
        }
    }
    private void displayMessage1(AccessToken accessToken){
        if(accessToken != null){
            Log.e("@@Access",accessToken.getToken());
            Log.e("@App Id", accessToken.getApplicationId());
            Log.e("@Uid", accessToken.getUserId());

        }
    }
    protected void onStart() {
        super.onStart();
     //   mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();

     /*   if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }

    /*@Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }*/

    /*@Override
    protected void onActivityResult1(int requestCode, int responseCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }*/

  /*  @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

        // Get user's information
        getProfileInformation();

        // Update the UI after signin
        updateUI(true);

    }*/

   /* @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
        updateUI(false);
    }
*/
    /**
     * Updating the UI, showing/hiding buttons and profile layout
     * */
    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            google.setVisibility(View.GONE);
            //    btnSignOut.setVisibility(View.VISIBLE);
            //    btnRevokeAccess.setVisibility(View.VISIBLE);
            //    llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            google.setVisibility(View.VISIBLE);
            //   btnSignOut.setVisibility(View.GONE);
            //   btnRevokeAccess.setVisibility(View.GONE);
            //   llProfileLayout.setVisibility(View.GONE);
        }
    }
    /**
     * Sign-in into google
     * */
   /* private void signInWithGplus() {
        try {
            if (!mGoogleApiClient.isConnecting()) {
                mSignInClicked = true;
                resolveSignInError();
            }
        }
        catch (Exception e)
        {
            e.toString();
        }
    }

    *//**
     * Method to resolve any signin errors
     * *//*
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }*/
    /**
     * Fetching user's information name, email, profile pic
     * */
   /* private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);

              *//*  txtName.setText(personName);
                txtEmail.setText(email);*//*

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

                //    new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}*/


}
