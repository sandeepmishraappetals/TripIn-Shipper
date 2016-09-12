package tripin.com.tripin_shipper.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tripin.com.tripin_shipper.R;
import tripin.com.tripin_shipper.activity.Activity_AddressBook;
import tripin.com.tripin_shipper.activity.Activity_Address_page;
import tripin.com.tripin_shipper.activity.Activity_Dashboard;
import tripin.com.tripin_shipper.model.AddressBook;
import tripin.com.tripin_shipper.model.User;
import tripin.com.tripin_shipper.volley.AppController;
import tripin.com.tripin_shipper.volley.Config_URL;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FirstFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment  implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private Activity_Dashboard activityDashboard;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = FirstFragment.class.getSimpleName();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MapView mMapView;
    private GoogleMap googleMap;
    private OnFragmentInteractionListener mListener;
    boolean dialogShown;
    String Access_Token, UserId; User user = new User();
    private List<AddressBook> addressList = new ArrayList<AddressBook>();
    private Bundle bundle;
    private ViewGroup currFragment;
    public FirstFragment() {
        // Required empty public constructor
        this.getActivity();

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
     //   args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       /* if(dialogShown){

        }
        else {
            dialogShown = true;
            //Custom dailog
            Dialog mDialog = null;
            mDialog=new Dialog(getActivity());
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //  if (mDialog == null){
            mDialog.setContentView(R.layout.alertcustomlayout);
            Window window = mDialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();

            mDialog.show();

            TextView ok;
            ok=(TextView) mDialog.findViewById(R.id.textView_add);
            // cancel=(TextView) mDialog.findViewById(R.id.dialogno);
            //  final Dialog finalMDialog = mDialog;
            final Dialog finalMDialog = mDialog;
            ok.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity());
                    UserId=(mSharedPreference.getString("UserId", null));
                    address_Book(Access_Token, UserId);
                    *//*Intent i = new Intent(getActivity(), Activity_Address_page.class);
                    startActivity(i);*//*

                    // mDialog.cancel();

                    finalMDialog.dismiss();
                }
            });
        }*/

        //end Custom Dailog
        mMapView = (MapView) currFragment.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

      /*  googleMap = mMapView.getMapAsync();*/
        mMapView.getMapAsync(this);
        googleMap = mMapView.getMap();
        // latitude and longitude
        double latitude = 17.385044;
        double longitude = 78.486671;

        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title("Hello Maps");

        // Changing marker icon
        //   marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        // adding marker
        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(17.385044, 78.486671)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        // Perform any camera updates here


        activityDashboard = (Activity_Dashboard) getActivity();
        activityDashboard.setAddressesFragment(R.id.addressPlacer);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.activity_booknow_map1, container, false);
        View v = inflater.inflate(R.layout.fragment_location, container,false);
        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity());
        Access_Token=(mSharedPreference.getString("Token", null));

        currFragment = (ViewGroup) v;

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void address_Book(final String access_Token, final String userId) {
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

                    JSONObject jObj = new JSONObject(response);
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
                                //String
                               /* AddressBook ab = new AddressBook();
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
                                addressList.add(ab);*/

                            }
                            bundle = new Bundle();
                            bundle.putString("addressList", data.toString());

                        }
                        catch (Exception ex)
                        {
                            ex.toString();
                        }

                        Intent address_Book = new Intent(getActivity(), Activity_AddressBook.class);
                        address_Book.putExtra("message", bundle);
                        address_Book.putExtra("mode", 0);
                        startActivity(address_Book);
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
                        Toast.makeText(getActivity(),
                                errorMsg, Toast.LENGTH_LONG).show();

                        Intent address_page = new Intent(getActivity(), Activity_Address_page.class);
                        address_page.putExtra("message", bundle);
                        address_page.putExtra("mode", 0);
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
    }

}
