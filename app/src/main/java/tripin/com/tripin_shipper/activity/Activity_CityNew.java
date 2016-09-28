package tripin.com.tripin_shipper.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tripin.com.tripin_shipper.AppController;
import tripin.com.tripin_shipper.R;
import tripin.com.tripin_shipper.adapter.CustomListAdapter_city;
import tripin.com.tripin_shipper.model.City;
import tripin.com.tripin_shipper.model.Countries;
import tripin.com.tripin_shipper.volley.Config_URL;

/**
 * Created by SUMEET on 04-09-2016.
 */
public class Activity_CityNew extends Activity {
    public String Access_Token;
    public String name,state_id,State_ID;
    private static final String TAG = Activity_CityNew.class.getSimpleName();
    private ProgressDialog pDialog;
    private List<City> movieList = new ArrayList<City>();
    ArrayList<HashMap<String, String>> city_list ;
    private ListView listView;
    private CustomListAdapter_city adapter;
    View header;
    private Activity activity;
    TextView head_label; private ImageButton back;
    private EditText MyFilter_ed;
    private Button addAddress;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        init();

    }

    private void init() {
        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(this);
        Access_Token=(mSharedPreference.getString("Token", null));
        Countries c = new Countries();
        final SharedPreferences mSharedPreference1= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        State_ID=(mSharedPreference.getString("state_id", null));
        header = (View) findViewById(R.id.header_search);
        head_label = (TextView) header.findViewById(R.id.text_label) ;
        head_label.setText("State List");
        MyFilter_ed = (EditText) findViewById(R.id.myFilter);

/*if (state_id!=null) {
    state_id = c.getCode().toString();
}
        else {
    Toast.makeText(this,"Please select State First", Toast.LENGTH_SHORT).show();
}*/
        StateList(Access_Token);
        listView = (ListView) findViewById(R.id.list);

        adapter = new CustomListAdapter_city(Activity_CityNew.this, movieList);
        listView.setAdapter(adapter);
        /**
         * Enabling Search Filter
         * */
        MyFilter_ed.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                //     Activity_CityNew.this.adapter.getFilter().filter(cs);


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = MyFilter_ed.getText().toString().toLowerCase(Locale.getDefault());
            }
        });



        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        back = (ImageButton) header.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_CityNew.this, Activity_Address_page.class);
                startActivity(i);
            }
        });
        addAddress = (Button) findViewById(R.id.bt_address) ;
        addAddress.setVisibility(View.GONE);
    }

    private void StateList(final String access_token)  {
        // Tag used to cancel the request
        String tag_string_req = "State";

//        pDialog.setMessage("State Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.URL_CITY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "State Response: " + response.toString());
                hideDialog();

                try {
                    /*ArrayList<HashMap<String, String>>*/ city_list = new ArrayList<HashMap<String, String>>();
                    JSONObject jObj = new JSONObject(response);

                    //  boolean error = jObj.getBoolean("error");
                    String error = jObj.getString("message");
                    int status = jObj.getInt("status");

                    try {
                        JSONArray data = jObj.getJSONArray("data");

                        for(int i=0 ; i< data.length(); i++)
                        {
                            JSONObject obj = data.getJSONObject(i);
                           String stateid = obj.optString("state_id");

                            String name = obj.getString("name");
                            String city_id = obj.getString("city_id");

                         //   city_name.add(name);
                            Log.e("@@name",name );
                         /*   c.setName(name);
                            c.setCode(stateid);
                            c.setCity_id(city_id);
                            c.setCity_name(name);*/
                            HashMap<String, String> city_map = new HashMap<String, String>();
                            city_map.put("name",name);
                            city_map.put("city_id", city_id);
                            city_map.put("state_id", stateid);
                            city_list.add(city_map);

                            City c = new City();
                            c.setCity_name(name);
                            c.setCity_id(city_id);


                            movieList.add(c);

                        }


                        //   c.setCode(state_id);
// adding movie to movies array




                    }

                    catch (Exception ex)
                    {
                        ex.toString();
                    }
                    // Check for error node in json
                  /*  if (error!=null)*/
                    if ( status == 1)
                    {
                        //Work need to be done
                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                    adapter.notifyDataSetChanged();
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
                params.put("tag", "city");
                params.put("access_token",Access_Token);
                params.put("state_id",State_ID);
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
        /*if (!pDialog.isShowing())
            pDialog.show();*/
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
