package tripin.com.tripin_shipper.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import tripin.com.tripin_shipper.R;
import tripin.com.tripin_shipper.SharedPreference.SharedPreference;
import tripin.com.tripin_shipper.helper.SQLiteHandler;
import tripin.com.tripin_shipper.helper.SessionManager;
import tripin.com.tripin_shipper.utils.NetworkUtils;
import tripin.com.tripin_shipper.AppController;
import tripin.com.tripin_shipper.volley.Config_URL;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

/**
 * Created by SUMEET on 07-08-2016.
 */
public class SignUp_Activity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG_activity = SignUp_Activity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private TextInputLayout mTextInputLayoutPassword;
    private TextInputLayout mTextInputLayoutEmail;
    private TextInputLayout mTextInputLayoutName;
    private RadioButton rb;
    private LinearLayout social_LL;
    private ImageView google, fb;
    private ImageButton back;
    public String Access_Token;

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .build();
    //private static final int RC_SIGN_IN = 9001;
    private SharedPreference sharedPreference;
    String user_name;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private static final String TAG_CANCEL = SignUp_Activity.class.getSimpleName();
    private static final String TAG_ERROR = SignUp_Activity.class.getSimpleName();
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

    //Google
    private static final int RC_SIGN_IN = 0;
    // Logcat tag
    private static final String TAG = "SignUp";

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;

    private boolean mSignInClicked;
    private boolean isTermAccept;

    private ConnectionResult mConnectionResult;

    //End Google

    Activity context = this;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize SDK before setContentView(Layout ID)
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
         setContentView(R.layout.activity_signup);
        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Access_Token=(mSharedPreference.getString("Token", null));
//*/1 compute IMEI
     /*   TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        String m_szImei = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
        Log.e("@@IMEI", m_szImei);*/
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mTextInputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        mTextInputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        mTextInputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.bt_createAccount);
        rb = (RadioButton) findViewById(R.id.radioButton);
        social_LL = (LinearLayout)findViewById(R.id.social) ;
        google = (ImageView) social_LL.findViewById(R.id.imageView_google);
        fb = (ImageView) social_LL.findViewById(R.id.imageView_fb);
        google.setOnClickListener(this);
        fb.setOnClickListener(this);
back = (ImageButton)findViewById(R.id.imageButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(getApplicationContext(), Activity_Main.class);
                startActivity(back);
            }
        });
        rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rb.isChecked()) {

                    isTermAccept = true;

                }
                else {
                    makeText(getApplicationContext(),
                            "Please select terms!", LENGTH_LONG)
                            .show();
                }

            }
        });

               // Register Button Click event
            btnRegister.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    if (isTermAccept == true) {
                        String name = inputFullName.getText().toString().trim();
                        String email = inputEmail.getText().toString().trim();
                        String password = inputPassword.getText().toString().trim();

                        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                            registerUser(name, email, password);
                        } else {
                            makeText(getApplicationContext(),
                                    "Please enter your details!", LENGTH_LONG)
                                    .show();
                        }

                    }
                    else {
                        makeText(getApplicationContext(),
                                "Please select Terms & Condition !", LENGTH_LONG)
                                .show();
                    }
                }

            });

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(SignUp_Activity.this,
                    DashBoardActivity.class);
            startActivity(intent);
            finish();
        }

        /*// Initializing google plus api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API, null)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();*/

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String name, final String email,
                              final String password)
    {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                Config_URL.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    Log.d(TAG, "@@Register Response: " + response.toString());
                    hideDialog();
                }
                catch(Exception ex){
                    ex.toString();
                }
                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("message");
                    int status = jObj.getInt("status");
                   /* if (error!=null)*/
                    if (status == 1 )
                    {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("status");

                        JSONObject user = jObj.optJSONObject("data");

                        String name = jObj.optString("name");

                     //   String created_at = jObj.optString("created_at");
                        String Mobile = jObj.optString("is_mobile");
                        String user_id = null;
                        try {
                            user_id = user.optString("user_id");
                            String email = user.optString("username");
                        } catch (Exception e) {
                            e.toString();
                        }
                        /*   User  user1 = new User();

                       user1.getUser_id();*/
                        if (user_id != null) {
                            try {
                                // Save the text in SharedPreference
                                sharedPreference.save(context, user_id);
                               /* Toast.makeText(context,
                                        *//*getResources().getString(R.string.saved)*//* ,
                                        Toast.LENGTH_LONG).show();*/
                                Log.e("@@ User_id", user_id);
                            } catch (Exception ex) {
                                ex.toString();
                            }
                        }
                        // Inserting row in users table
                        if (Mobile.contentEquals("y")) {
                            try {
                                //        db.addUser(name, email, uid/*created_at*/);


                                Intent i = new Intent(SignUp_Activity.this,
                                        Activity_phoneNumberVerification.class);
                                i.putExtra("key", user_id);
                                i.putExtra("mobile", email);
                                Log.e("@@isMobile", Mobile);
                                startActivity(i);
                                finish();

                            } catch (Exception ex) {
                                ex.toString();
                            }

                        } else {
                            // Launch login activity
                            Intent intent = new Intent(
                                    SignUp_Activity.this,
                                    DashBoardActivity.class);

                            startActivity(intent);
                            finish();
                        }
                    }

                    else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("message");
                        makeText(getApplicationContext(),
                                errorMsg, LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                makeText(getApplicationContext(),
                        error.getMessage(), LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
              //  params.put("tag", "register");
                params.put("name", name);
                params.put("username", email);
                params.put("password", password);
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


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
                    signInWithGplus();
                }
                break;
            case R.id.imageView_fb:
                if (!NetworkUtils.isNetworkConnectionAvailable(this)) {
                    NetworkUtils.showNoInternetDialog(this);
                } else {
                    //   signIn();
                    onFblogin();
                }

                break;


        }
        }


   // Private method to handle Facebook login and callback
   private void onFblogin()
    {
       callbackmanager = CallbackManager.Factory.create();

       // Set permissions
       LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile,email,user_birthday"/*"public_profile", "email"*/));

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackmanager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
        Intent i = new Intent(this, DashBoardActivity.class);
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
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }

    @Override
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

    }

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

    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        makeText(this, "User is connected!", LENGTH_LONG).show();

        // Get user's information
        getProfileInformation();

        // Update the UI after signin
        updateUI(true);

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
        updateUI(false);
    }

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
    private void signInWithGplus() {
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

    /**
     * Method to resolve any signin errors
     * */
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
    }
    /**
     * Fetching user's information name, email, profile pic
     * */
    private void getProfileInformation() {
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

              /*  txtName.setText(personName);
                txtEmail.setText(email);*/

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

            //    new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                makeText(getApplicationContext(),
                        "Person information is null", LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

