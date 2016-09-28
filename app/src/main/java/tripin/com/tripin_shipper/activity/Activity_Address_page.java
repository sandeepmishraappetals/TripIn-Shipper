package tripin.com.tripin_shipper.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import tripin.com.tripin_shipper.AppController;
import tripin.com.tripin_shipper.R;
import tripin.com.tripin_shipper.helper.SessionManager;
import tripin.com.tripin_shipper.model.AddressBook;
import tripin.com.tripin_shipper.model.AddressList;
import tripin.com.tripin_shipper.model.User;
import tripin.com.tripin_shipper.volley.Config_URL;

/**
 * Created by Android on 26/08/16.
 */
public class Activity_Address_page extends Activity implements View.OnClickListener, View.OnTouchListener {
    private TextView state;
    private TextView city;
    private EditText survey;
    private ProgressDialog pDialog;
    private SessionManager session;

    private TextInputLayout mTextInputLayoutState;
    private TextInputLayout mTextInputLayoutCity;
    private TextInputLayout mTextInputLayoutSurvey;
    private TextInputLayout mTextInputLayoutContact;
    private TextInputLayout NameOfContactPerson;
    private TextInputLayout MobileOfContactPerson;
    private TextInputLayout mTextInputLayoutLoading;
    private TextInputLayout Loadin;
    private EditText Mobile;
    private EditText Contact_Name;
    private EditText Name_of_firm;
    private EditText Address;
    private EditText Landmark;
    private EditText Pincode;
    private EditText Loading_time;
    private EditText Loading_time_to;
    private EditText Landline;
    private EditText Email_id;
    private TextView Loading_from;
    private TextView Loading_to;
    private EditText Loading_EditText;
    private ImageButton ImbtTrash;

    //  private EditText
    private String Access_Token;

    private Button add_address;
    View activity_login_header;

    private ImageButton Contact;
    private static final int PICK_CONTACT = 100;
    User user = new User();
    private static final String TAG = Activity_Address_page.class.getSimpleName();
    String state_id, state_name, City_name, City_Id;
    static final int TIME_DIALOG_ID = 999;
    private int hour;
    private int minute;
    private ImageButton back;

    private TimePicker timePicker1, timePicker2;
    private ViewGroup timePopup;
    private GoogleApiClient client;
    String time1,time2;
    String loading_to, loading_from;
   // edit page parameter
    String order_address_id, address_id;
    //end edit page
    private int mode;
    private ViewGroup Alert_Trash, alertSave;
    private RelativeLayout LoadingTimeRl;
    Bundle bundle ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* setContentView(R.layout.activity_add_address1);*/
        setContentView(R.layout.add_address_pg1);

        mode = getIntent().getIntExtra("mode", -1);
        Log.e("mode", ""+mode);
        init();

    }

    private void init() {

        Bundle extras = getIntent().getExtras();

        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
        //  Access_Token=(mSharedPreference.getString("Token", null));
        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();

        // Get the results of country
        state_id = i.getStringExtra("state_id");
        //    City_name = i.getStringExtra("city_name");
        /*if (state_name!=null)
        {*/
            state_name = (mSharedPreference.getString("State", null));
   //     }

        City_name = (mSharedPreference.getString("city_name", null));
        if (address_id!=null )
        {
            address_id = i.getStringExtra("AddressId");
        }
        else {
            address_id = "0";
        }

        order_address_id =  "0"/*i.getStringExtra("address")*/;
        //  City_Id = i.getStringExtra("city_id");
        View page1 = findViewById(R.id.page1); // root View id from that link
        View page2 = findViewById(R.id.page2); // id of a view contained in the included file
        View page3 = findViewById(R.id.page3);
        View header = findViewById(R.id.address_header);
        back = (ImageButton) header.findViewById(R.id.back);
        mTextInputLayoutState = (TextInputLayout) page1.findViewById(R.id.input_layout_state);
        mTextInputLayoutCity = (TextInputLayout) page1.findViewById(R.id.input_layout_city);
        mTextInputLayoutSurvey = (TextInputLayout) page1.findViewById(R.id.input_layout_survey);
        mTextInputLayoutLoading = (TextInputLayout) page2.findViewById(R.id.loading_time_InputLayout);
     //   Loadin = (TextInputLayout) page2.findViewById(R.id.loading_time);
       /* state = (EditText) findViewById(R.id.state);*/
        city = (TextView) page1.findViewById(R.id.city);
        survey = (EditText) page1.findViewById(R.id.survey);
        add_address = (Button) findViewById(R.id.Add_address);
        Contact = (ImageButton) findViewById(R.id.bt_contact);
        Name_of_firm = (EditText) findViewById(R.id.name);
        Address = (EditText) findViewById(R.id.address);
        Landmark = (EditText) findViewById(R.id.landmark);
        Pincode = (EditText) findViewById(R.id.pincode);
        survey = (EditText) findViewById(R.id.survey);
        Loading_from = (TextView) findViewById(R.id.text_from);
        Loading_to = (TextView) findViewById(R.id.text_to);
        mTextInputLayoutSurvey = (TextInputLayout) findViewById(R.id.input_layout_survey);
        state = (TextView) page1.findViewById(R.id.state);
        NameOfContactPerson = (TextInputLayout) findViewById(R.id.TextInputLayoutname_contact);
        Contact_Name = (EditText) findViewById(R.id.name_contact);
        MobileOfContactPerson = (TextInputLayout) findViewById(R.id.input_layout_mobile);
        Mobile = (EditText) findViewById(R.id.mobile);
        Email_id = (EditText) findViewById(R.id.email_id);
        Landline = (EditText) findViewById(R.id.landline);
        state.setText(state_name);
        city.setText(City_name);
        Loading_EditText = (EditText) page2.findViewById(R.id.loadingtime);
        activity_login_header = findViewById(R.id.include);
        final SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Access_Token = (mSharedPreference1.getString("Token", null));
        ImbtTrash = (ImageButton) header.findViewById(R.id.imageButton_trash);
            state.setOnClickListener(this);

            city.setOnClickListener(this);
        mTextInputLayoutLoading.setOnClickListener(this);
        LoadingTimeRl = (RelativeLayout)page2.findViewById(R.id.timeRl) ;
        LoadingTimeRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingTimePopUp();
            }
        });
      //  Loadin.setOnClickListener(this);
      //  Loadin.setOnTouchListener(this);
            back.setOnClickListener(this);
            timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
            timePicker1.setIs24HourView(false);


            timePicker2 = (TimePicker) findViewById(R.id.timePicker2);
            timePicker2.setIs24HourView(false);

            timePopup = (ViewGroup) findViewById(R.id.timePopup);
            timePopup.setVisibility(View.GONE);
            int convertedWidth = 1090 * 100/100;
            int convertedHeight= 1920 * 100/100;
            timePopup.getLayoutParams().height = convertedHeight;
Alert_Trash = (ViewGroup) findViewById(R.id.Alert_delet);
        alertSave = (ViewGroup) findViewById(R.id.Savealert);
        final Button delete = (Button) findViewById(R.id.button_Delete);
        final Button cancel = (Button) findViewById(R.id.button_Cancel);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete.setBackgroundColor(getResources().getColor(R.color.aqua_marine));
                deleteAddress();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAddressAlert();
            }
        });

        ImbtTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alert_Trash.setVisibility(View.VISIBLE);

            }
        });
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        if(mode == 1){
            Bundle bundle = getIntent().getBundleExtra("addressBundle");
            AddressBook addressBook = (AddressBook) bundle.getSerializable("obj");
            ImbtTrash.setVisibility(View.VISIBLE);
            assignPrefilledData(addressBook);
        }
            add_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    addAddress();
                }
            });

        mTextInputLayoutLoading.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                   Log.e("clicked", "has clicked");
                }
            }
        });
            Contact.setOnClickListener(this);

       setPrefillEntries();
    }

    private void setPrefillEntries(){
        state.setText("Gujarat");
        city.setText("Vadodara");
        survey.setText("djks");
        Name_of_firm.setText("sssss");
        Address.setText("sdsddss c ssd");
        Landmark.setText("bhau chi galli");
        Pincode.setText("");
        Contact_Name.setText("Test21");
        Mobile.setText("9702576304");
        Landline.setText("");
        Email_id.setText("sanjeev@test1.com");
    }

    private void addAddress() {

        String state_name = state.getText().toString().trim(); //Mandatory
        String city_name = city.getText().toString(); //Mandatory
        String survey_name = survey.getText().toString().trim(); //Mandatory
        String name_firm = Name_of_firm.getText().toString().trim(); //Mandatory
        String address = Address.getText().toString().trim(); //Mandatory
        String landmark = Landmark.getText().toString().trim();
        String pincode = Pincode.getText().toString().trim();
        loading_from = /*"from"+*/time1;//Loading_from.getText().toString().trim(); //Mandatory
        loading_to = /*"To"+*/time2;//Loading_to.getText().toString().trim();  //Mandatory
        String name_contact = Contact_Name.getText().toString().trim();  //Mandatory
        String mobile = Mobile.getText().toString().trim(); //Mandatory
        String landline = Landline.getText().toString().trim();
        String email = Email_id.getText().toString().trim();
        String firmtype = "0"; //need to work with blling address
        String order_add_id = "0";
        if (state_name == null && city_name.isEmpty() && survey_name.isEmpty() && name_firm.isEmpty() && address.isEmpty() && loading_from.isEmpty()
                && loading_to.isEmpty() && name_contact.isEmpty() && mobile.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill proper input", Toast.LENGTH_SHORT).show();
        } else {
            add_address.setBackgroundResource(R.color.aqua_marine);
            if (mode == 1){
                SaveAdd(Access_Token, state_name, city_name, survey_name, name_firm, address, landline, pincode, landmark, loading_to, loading_from, name_contact, mobile, email,firmtype, order_add_id,address_id);

            }
            else{
                SaveAdd1(Access_Token, state_name, city_name, survey_name, name_firm, address, landline, pincode, landmark, time1, time2, name_contact, mobile, email,firmtype, order_address_id, address_id);

            }
        }

    }


    String user_id = "28";
   // String address_ID = "95";
    private void deleteAddress() {
        deleteAddressAPI(Access_Token,user_id,address_id);
        Log.e("@Delete", "Click");
        Alert_Trash.setVisibility(View.GONE);
    }

    private void deleteAddressAPI(String access_token, final String user_id, final String address_id) {

            if (AppController.getInstance().isConnected(this))
            {
                // Tag used to cancel the request
                String tag_string_req = "req_deleteAddress";

             /*   pDialog.setMessage("DeleteAddress in ...");
                showDialog();*/

                StringRequest strReq = new StringRequest(Request.Method.POST,
                        Config_URL.URL_ADDRESS_DELETE, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "DeleteAddress Response: " + response.toString());
                    //    hideDialog();

                        try {

                            JSONObject jObj = new JSONObject(response);
                            //  boolean error = jObj.getBoolean("error");
                            String error = jObj.getString("message");
                            int status = jObj.getInt("status");

                            // Check for error node in json
                  /*  if (error!=null)*/
                            if ( status == 1)
                            {
                                String errorMsg = jObj.getString("message");
                                Toast.makeText(getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                                Intent addressBook = new Intent(getApplicationContext(), Activity_AddressBook.class);
                                startActivity(addressBook);
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
                        params.put("address_id", address_id);
                        params.put("user_id", user_id);
                        params.put("access_token",Access_Token);
Log.e("DEleteParam", params.toString());
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

       /* private void showDialog() {
            if (!pDialog.isShowing())
                pDialog.show();
        }

        private void hideDialog() {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }*/


    private void cancelAddressAlert() {
        Alert_Trash.setVisibility(View.GONE);
    }
    private void assignPrefilledData(AddressBook addressBook){
            Log.e("mode", "" + addressBook.getCity() + addressBook.getAddress_id());
        String state_name = null;
        String city_name = null;
        String survey_name = null;
        String name_firm = null;
        String address = null;
        String landmark= null;
        String pincode = null;
        String L_from = null;
        String L_to= null;
        String name_contact= null;
        String mobile = null;
        String landline = null;
        String email = null;
        if(!addressBook.getState().toString().trim().equals(null)){
            state_name  = addressBook.getState().toString().trim(); //Mandatorys
            state.setText(state_name);
        }
        else {
            Toast.makeText(this, "State Error", Toast.LENGTH_SHORT).show();
        }
        if(!addressBook.getCity().toString().equals(null)){
             city_name = addressBook.getCity().toString(); //Mandatory
            city.setText(city_name);
        }
        else {
            Toast.makeText(this, "City Error", Toast.LENGTH_SHORT).show();
        }
        if (!addressBook.getSurvey_no().toString().equals(null))
        {
            survey_name = addressBook.getSurvey_no().toString().trim(); //Mandatory
            survey.setText(survey_name);
        }
        else {
            Toast.makeText(this, "Survey Error", Toast.LENGTH_SHORT).show();
        }
        if (!addressBook.getFirm_name().toString().equals(null))
        {
            name_firm = addressBook.getFirm_name().toString().trim(); //Mandatory
            Name_of_firm.setText(name_firm);
        }
        else {
            Toast.makeText(this, "Firm Name Error", Toast.LENGTH_SHORT).show();
        }
        if (!addressBook.getAddress().toString().equals(null))
        {
            address = addressBook.getAddress().toString().trim(); //Mandatory
            Address.setText(address);
        }
        else {
            Toast.makeText(this, "Address Error", Toast.LENGTH_SHORT).show();
        }
      try {
          if (!addressBook.getLandmark().toString().equals(null)) {
              landmark = addressBook.getLandmark().toString().trim();
              Landmark.setText(landmark);
          }
      }
      catch (Exception ex)
      {
          ex.toString();
      }
        if (!addressBook.getPincode().toString().equals(null))
        {
            pincode = addressBook.getPincode().toString().trim();
            Pincode.setText(pincode);
        }
       try {
           if (!addressBook.getWorking_hour_from().toString().equals(null)) {
               L_from = addressBook.getWorking_hour_from().toString().trim();
               Loading_from.setText(L_from);

           }
           else {
               Toast.makeText(this, "Loading Time Error", Toast.LENGTH_SHORT).show();
           }
       }
       catch (Exception ex)
       {
           ex.toString();
       }
        try {
            if (!addressBook.getWorking_hour_to().toString().equals(null)) {
                L_to = addressBook.getWorking_hour_to().toString().trim();
                Loading_to.setText(L_to);
            }
        }
        catch (Exception ex)
        {
            ex.toString();
        }
        if (!addressBook.getName_of_person().toString().equals(null))
        {
            name_contact = addressBook.getName_of_person().toString().trim();
            Contact_Name.setText(name_contact);
        }
        else {
            Toast.makeText(this, "Contact Name Error", Toast.LENGTH_SHORT).show();
        }
try {
    if (!addressBook.getMobile().toString().equals(null)) {
        mobile = addressBook.getMobile().toString().trim();
        Mobile.setText(mobile);
    }
    else {
        Toast.makeText(this, "Mobile Error", Toast.LENGTH_SHORT).show();
    }
}
catch (Exception ex)
{
    ex.toString();
}
     //   String firmtype = "0";

    }
    private void SaveAdd1(final String access_token, final String state_name, final String city_name, final String survey_name,
                          final String name_firm, final String address, final String landline, final String pincode,
                          final String landmark, final String loading_to, final String loading_from,
                          final String name_contact, final String mobile, final String email, final String firmType, final String order_address_id, final String address_id) {

        // Tag used to cancel the request
        String tag_string_req = "req_saveAddress";

//        pDialog.setMessage("Saving");
        //  showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.URL_SAVE_ADDRESS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "SAving Address Response: " + response.toString());
                //        hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    //  boolean error = jObj.getBoolean("error");
                    String error = jObj.getString("message");
                    int status = jObj.getInt("status");

                    try {
                        JSONObject data = jObj.getJSONObject("data");
                        String lat = data.optString("lat");
                        String lng = data.optString("lng");
                        if (data.optString("address_id")!=null)
                        {
                            String address_id = data.optString("address_id");
                        }
                        else {
                            String address_id = "0";
                        }

                      //  String order_address_id = "0";/*data.optString("order_address_id");*/
                    } catch (Exception ex) {
                        ex.toString();
                    }
                    if (status == 1) {
                        // Need to Work

                       // Intent intent = new Intent (getApplicationContext(), Activity_AddressBook.class);
                        //intent.putExtra("message", "null");
                        //startActivity(intent);

                        gotSuccesfulresponseForSaveAddress();
                        Toast.makeText(Activity_Address_page.this, error.toString(), Toast.LENGTH_SHORT).show();
                        //finish();




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
                params.put("firm_name", name_firm);
                params.put("access_token", access_token);
                params.put("working_hour_from",time1 /*loading_from*//*loading_from*/);
                params.put("working_hour_to", time2/*loading_to*//*loading_to*/);
                params.put("survey_no", survey_name);
                params.put("address", address);
                params.put("city", city_name);
                params.put("state", state_name);
                params.put("pincode", pincode);
                params.put("address_id", address_id);
                params.put("name_of_person", name_contact);
                params.put("mobile", mobile);
                params.put("email", email);
                params.put("user_id", "28");
                params.put("landline", landline);
                params.put("landmark", landmark);
                params.put("firm_type", name_firm);
                params.put("order_address_id", order_address_id);
                params.put("firm_type", "0");
Log.e("@save", params.toString());
                return params;
            }
            private Map<String, String> checkParams(Map<String, String> map){
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
                    if(pairs.getValue()==null){
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
       // request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void gotSuccesfulresponseForSaveAddress(){
        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Access_Token=(mSharedPreference.getString("Token", null));
        /*
        address_Book(Access_Token, UserId);
     *//*   btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnGetAll = (Button) findViewById(R.id.btnGetAll);
        btnGetAll.setOnClickListener(this);*//*

        listView = (ListView) findViewById(R.id.list);

        adapter = new CustomListAdapter_AddressBook(Activity_AddressBook.this, addressList);
        listView.setAdapter(adapter);
*/
        address_Book1(Access_Token, user_id); //need to work on bug
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri contactData = data.getData();
                Cursor cursor = managedQuery(contactData, null, null, null, null);
                cursor.moveToFirst();

                String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String Name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                // String Email = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null));
                //contactName.setText(name);
                Mobile.setText(number);
                Contact_Name.setText(Name);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
          /*  case R.id.back_bt:

                Toast.makeText(getApplicationContext(), "Back", Toast.LENGTH_SHORT).show();
                break;*/
            case R.id.state:

                Intent i = new Intent(this, Activity_state.class);
                startActivity(i);
                finish();
                //  state.getText().toString();
                break;

            case R.id.city:
                Intent icity = new Intent(this, Activity_CityNew.class);
                startActivity(icity);
                finish();
                break;

            case R.id.survey:
                break;

            case R.id.add_address:


                break;
            case R.id.bt_contact:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, 100);
                finish();
                break;
            case R.id.loading_time_InputLayout:
               AppController.getInstance().hideSoftKeyboard(this);
                timePopup.setVisibility(View.VISIBLE);
                break;
// Back button functionality
            case R.id.back:
                Intent back = new Intent(this, Activity_Dashboard.class);
               startActivity(back);
                finish();
                break;
        }
    }

    private void address_Book1(final String access_Token, final String userId) {
        // Tag used to cancel the request
        String tag_string_req = "req_AddressBook";

       /* pDialog.setMessage("Logging in ...");
        showDialog();*/

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.URL_ADDRESS_BOOK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Address Book Response: " + response.toString());
                //   hideDialog();

                try {

                    final JSONObject jObj = new JSONObject(response);
                    //  boolean error = jObj.getBoolean("error");
                    String error = jObj.getString("message");
                    int status = jObj.getInt("status");
                    JSONArray data = jObj.optJSONArray("data");

                    // Check for error node in json
                  /*  if (error!=null)*/
                    if ( status == 1)
                    {
                        try {
                            //    JSONArray Arraydata = new JSONArray();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);
                                String address_id = obj.optString("address_id");
                                String firm_name = obj.optString("firm_name");
                                String working_hour_from = obj.optString("working_hour_from");
                                String working_hour_to = obj.optString("working_hour_to");
                                String survey_no = obj.optString("survey_no");
                                String address = obj.optString("gmap_address");
                                String state = obj.optString("state");
                                String city = obj.optString("city");
                                String pincode = obj.optString("pincode");
                                String created_by = obj.optString("created_by");
                                String landline = obj.optString("landline");
                                String order_address_id = obj.optString("order_address_id");
                                String name_of_person = obj.optString("name_of_person");
                                String landmark = obj.optString("landmark");
                                String mobile = obj.optString("mobile");
                                String lat = obj.optString("lat");
                                String lng = obj.optString("lng");
                            }
                            bundle = new Bundle();
                            bundle.putString("addressList", data.toString());

                            AddressList.addressesBundle = data.toString();

                        }
                        catch (Exception ex)
                        {
                            ex.toString();
                        }
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable(){
                            @Override
                            public void run(){
                                Intent address_Book = new Intent(Activity_Address_page.this, Activity_AddressBook.class);
                                address_Book.putExtra("message", bundle);
                                address_Book.putExtra("mode", 0);

                                startActivity(address_Book);
                                try {
                                    String errorMsg = jObj.getString("message");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(Activity_Address_page.this,
                                        "succesful", Toast.LENGTH_LONG).show();
                            }
                        }, 2000);
                        alertSave.setVisibility(View.VISIBLE);


                    } /*else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getActivity(),
                                errorMsg, Toast.LENGTH_LONG).show();

                        Intent address_page = new Intent(getActivity(), Activity_Address_page.class);
                        startActivity(address_page);
                    }*/
                    else if (status == 0)
                    {
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(Activity_Address_page.this,
                                errorMsg, Toast.LENGTH_LONG).show();

                       /* Intent address_page = new Intent(Activity_AddressBook.this, Activity_AddressBook.class);
                        address_page.putExtra("message", bundle);
                        address_page.putExtra("mode", 0);
                        address_page.putExtra("type", whichType);
                        startActivity(address_page);*/
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
                params.put("tag", "AddressBook");

                params.put("user_id", userId);
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


    private void LoadingTimePopUp() {
        AppController.getInstance().hideSoftKeyboard(this);
        timePopup.setVisibility(View.VISIBLE);
    }
    public void setTime(View view) {
        int hour1 = timePicker1.getCurrentHour();
        int min1 = timePicker1.getCurrentMinute();

        int hour2 = timePicker2.getCurrentHour();
        int min2 = timePicker2.getCurrentMinute();

         time1= hour1 +  ":"+ min1;
         time2= hour2 +  ":"+ min2;

        Log.e("selectedTime", ""+time1 + " "+time2);
        timePopup.setVisibility(View.GONE);
        String amPm = "AM";
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("From " + hour1 + ":" +min1);
        strBuilder.append(" to " + hour2 + ":" +min2);
        Loading_EditText.setText(strBuilder.toString());

    }

    public void SaveAdd(final String access_token, final String state_name, final String city_name, final String survey_name,
                        final String name_firm, final String address, final String landline, final String pincode,
                        final String landmark, final String loading_to, final String loading_from,
                        final String name_contact, final String mobile, final String email, final String firmType, final String order_add_id, final String address_id) {

        // Tag used to cancel the request
        String tag_string_req = "req_saveAddress";

//        pDialog.setMessage("Saving");
        //  showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.URL_SAVE_ADDRESS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                //        hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    //  boolean error = jObj.getBoolean("error");
                    String error = jObj.getString("message");
                    int status = jObj.getInt("status");
                    try {
                        JSONObject data = jObj.getJSONObject("data");
                        String lat = data.optString("lat");
                        String lng = data.optString("lng");
                        String address_id = data.optString("address_id");
                        String order_address_id = data.optString("order_address_id");
                    } catch (Exception ex) {
                        ex.toString();
                    }
                    // Check for error node in json
                  /*  if (error!=null)*/
                    if (status == 1) {
                        // Need to Work
                        Toast.makeText(Activity_Address_page.this, error.toString(), Toast.LENGTH_SHORT).show();
                        /*Intent address_book = new Intent(Activity_Address_page.this, Address_Book.class);
                        startActivity(address_book);*/
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
                params.put("firm_name", name_firm);

                params.put("access_token", access_token);
                params.put("working_hour_from","10" /*loading_from*//*loading_from*/);
                params.put("working_hour_to", "21"/*loading_to*//*loading_to*/);
                params.put("survey_no", survey_name);
                params.put("address", address);
                params.put("city", city_name);
                params.put("state", state_name);
                params.put("pincode", pincode);
                params.put("address_id", "0");
                params.put("name_of_person", name_contact);
                params.put("mobile", mobile);
                params.put("email", email);
                params.put("user_id", "28");
                params.put("landline", landline);
                params.put("landmark", landmark);
                params.put("firm_type", name_firm);
                params.put("order_address_id", order_add_id);
                params.put("firm_type", "0");
                Log.e("@saveAdd", params.toString());
                return params;

            }
            private Map<String, String> checkParams(Map<String, String> map){
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
                    if(pairs.getValue()==null){
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
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
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
         }
    public void Show_Toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    public void Reset_Text() {
        Name_of_firm.getText().clear();
        Mobile.getText().clear();
        Email_id.getText().clear();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        AppController.getInstance().hideSoftKeyboard(this);
        timePopup.setVisibility(View.VISIBLE);
        Log.e("TExtInputClick","Click");
        return true;
    }
}
