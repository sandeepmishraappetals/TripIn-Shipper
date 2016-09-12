package tripin.com.tripin_shipper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tripin.com.tripin_shipper.R;
import tripin.com.tripin_shipper.adapter.AddressBookAdapter;
import tripin.com.tripin_shipper.adapter.CustomListAdapter_AddressBook;
import tripin.com.tripin_shipper.fragment.FirstFragment;
import tripin.com.tripin_shipper.model.AddressBook;
import tripin.com.tripin_shipper.model.AddressList;
import tripin.com.tripin_shipper.model.AddressObj;

/**
 * Created by Android on 29/08/16.
 */
public class Activity_AddressBook extends Activity {

    Button btnAdd,btnGetAll;
    TextView student_Id;
    TextView student_name;
    ImageButton Edit;
    Button addAddress;

    ArrayList<HashMap<String, String>> address_list ;
    private ListView listView;
    private CustomListAdapter_AddressBook adapter;
    String Access_Token, UserId;
    private List<AddressBook> addressList = new ArrayList<AddressBook>();
    private static final String TAG = Activity_AddressBook.class.getSimpleName();


    public Activity_Dashboard mainActivity;
    public final int PICKUP = 0;
    public final int DROP = 1;
    public int SELECTED_TYPE;
    View header;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        mainActivity = AddressList.mainActivity;

        SELECTED_TYPE = getIntent().getIntExtra("type", -1);
        header = (View) findViewById(R.id.header_search);
        //init();
        Bundle bundle = (Bundle) getIntent().getExtras().get("message");
        //Log.e("mybundle", ""+bundle.getString("addressList"));
        back = (ImageButton) header.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_AddressBook.this, FirstFragment.class);
                startActivity(i);
            }
        });
        JSONArray jsonArray = null;
        if(bundle != null){
            try {
                jsonArray = new JSONArray(bundle.getString("addressList"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = null;
                try {
                    obj  = (JSONObject) jsonArray.get(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AddressBook ab = new AddressBook();
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
                String mobile = obj.optString("mobile");

                ab.setAddress_id(address_id);
                ab.setFirm_name(firm_name);
                ab.setWorking_hour_from(working_hour_from);
                ab.setWorking_hour_to(working_hour_to);
                ab.setSurvey_no(survey_no);
                ab.setAddress(address);
                ab.setState(state);
                ab.setCity(city);
                ab.setPincode(pincode);
                ab.setCreated_by(created_by);
                ab.setLandline(landline);
                ab.setOrder_address_id(order_address_id);
                ab.setName_of_person(name_of_person);
                ab.setMobile(mobile);
                addressList.add(ab);

                Log.e("add", ""+ab.getAddress() + " " + i + " " + ab.getAddress_id() + " " +ab.getOrder_address_id());
                listView = (ListView) findViewById(R.id.list);

                adapter = new CustomListAdapter_AddressBook(Activity_AddressBook.this, addressList);
                listView.setAdapter(adapter);

                addAddress = (Button) findViewById(R.id.bt_address);
                addAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Activity_AddressBook.this, Activity_Address_page.class);
                        i.putExtra("mode",0);
                        startActivity(i);
                    }
                });
        }


        }


      /*  final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        UserId=(mSharedPreference.getString("UserId", null));
        Access_Token=(mSharedPreference.getString("Token", null));
        address_Book(Access_Token, UserId);
     *//*   btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnGetAll = (Button) findViewById(R.id.btnGetAll);
        btnGetAll.setOnClickListener(this);*//*

        listView = (ListView) findViewById(R.id.list);

        adapter = new CustomListAdapter_AddressBook(Activity_AddressBook.this, addressList);
        listView.setAdapter(adapter);
*/

    }
  /*  private void address_Book(final String access_Token, final String userId) {
        // Tag used to cancel the request
        String tag_string_req = "req_AddressBook";

       *//* pDialog.setMessage("Logging in ...");
        showDialog();*//*

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.URL_ADDRESS_BOOK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Address Book Response: " + response.toString());
                //   hideDialog();

                try {

                    JSONObject jObj = new JSONObject(response);
                    //  boolean error = jObj.getBoolean("error");
                    String error = jObj.getString("message");
                    int status = jObj.getInt("status");


                    // Check for error node in json
                  *//*  if (error!=null)*//*
                    if ( status == 1)
                    {
                        try {
                            JSONArray data = new JSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);
                                String address_id = obj.optString("address_id");
                                String firm_name = obj.optString("firm_name");
                                String working_hour_from = obj.optString("working_hour_from");
                                String working_hour_to = obj.optString("working_hour_to");
                                String survey_no = obj.optString("survey_no");
                                String address = obj.optString("address");
                                String state = obj.optString("state");
                                String city = obj.optString("city");
                                String pincode = obj.optString("pincode");
                                String created_by = obj.optString("created_by");
                                String landline = obj.optString("landline");
                                String order_address_id = obj.optString("order_address_id");
                                String name_of_person = obj.optString("name_of_person");
                                AddressBook ab = new AddressBook();
                                ab.setAddress_id(address_id);
                                ab.setFirm_name(firm_name);
                                ab.setWorking_hour_from(working_hour_from);
                                ab.setWorking_hour_to(working_hour_to);
                                ab.setSurvey_no(survey_no);
                                ab.setAddress(address);
                                ab.setState(state);
                                ab.setCity(city);
                                ab.setPincode(pincode);
                                ab.setCreated_by(created_by);
                                ab.setLandline(landline);
                                ab.setOrder_address_id(order_address_id);
                                ab.setName_of_person(name_of_person);
                                addressList.add(ab);

                            }
                        }
                        catch (Exception ex)
                        {
                            ex.toString();
                        }

                        Intent address_Book = new Intent(Activity_AddressBook.this, Activity_AddressBook.class);
                        startActivity(address_Book);
                    } *//*else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getActivity(),
                                errorMsg, Toast.LENGTH_LONG).show();

                        Intent address_page = new Intent(getActivity(), Activity_Address_page.class);
                        startActivity(address_page);
                    }*//*
                    else if (status == 0)
                    {
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(Activity_AddressBook.this,
                                errorMsg, Toast.LENGTH_LONG).show();

                        Intent address_page = new Intent(Activity_AddressBook.this, Activity_Address_page.class);
                        startActivity(address_page);
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
               *//* Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();*//*
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

                params.put("user_id", UserId);
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
    }*/

    public void sendResultToCallingActivity(){
        finish();
        mainActivity.addressesFragment.checkUserInputs();
    }

    private void init(){
        // Get ListView object from xml
        final ListViewCompat listView = (ListViewCompat) findViewById(R.id.addressListView);

        // Defined Array values to show in ListView
        // String[] values = new String[] { "Address1", "Address2", "Address3", "Address4", "Address5"

        ArrayList<AddressObj> addressList = new ArrayList<>();

        addressList.add(new AddressObj(0, "name1", "address1", "1.723333", "1.723333"));
        addressList.add(new AddressObj(1, "name2", "address2", "1.723333", "1.723333"));
        addressList.add(new AddressObj(2, "name3", "address3", "1.723333", "1.723333"));
        addressList.add(new AddressObj(3, "name4", "address4", "1.723333", "1.723333"));
        addressList.add(new AddressObj(4, "name5", "address5", "1.723333", "1.723333"));

        AddressObj[] values = new AddressObj[addressList.size()];
        for(int i=0;i<addressList.size();i++){
            values[i] = addressList.get(i);
        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        //android.R.layout.simple_list_item_1, android.R.id.text1, values);

        AddressBookAdapter adapter = new AddressBookAdapter(this,addressList);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert
                if(SELECTED_TYPE == PICKUP){
                    AddressList.pickUpList.add(itemValue);
                }else  if(SELECTED_TYPE == DROP){
                    AddressList.dropList.add(itemValue);
                }

                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();
                sendResultToCallingActivity();

            }

        });
*/
    }
}
