package tripin.com.tripin_shipper.fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
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
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tripin.com.tripin_shipper.AppController;
import tripin.com.tripin_shipper.R;
import tripin.com.tripin_shipper.activity.Activity_Tuck12;
import tripin.com.tripin_shipper.adapter.BodyListAdapter;
import tripin.com.tripin_shipper.adapter.CapacityListAdapter;
import tripin.com.tripin_shipper.adapter.LengthListAdapter;
import tripin.com.tripin_shipper.model.TruckBody;
import tripin.com.tripin_shipper.model.TruckCartList;
import tripin.com.tripin_shipper.model.TruckCartObj;
import tripin.com.tripin_shipper.model.TruckDataList;
import tripin.com.tripin_shipper.model.TruckLength;
import tripin.com.tripin_shipper.model.TruckPayload;
import tripin.com.tripin_shipper.volley.Config_URL;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayFragment extends Fragment implements OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "TodayFragment";

    // TODO: Rename and change types of parameters
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    NumberPicker np, np1, np2;
    public static SlidingUpPanelLayout mLayout;
    String access_token, pickup_address, dropoff_address, booking_date;
    String user_id, date, capacity, body_type, length, per_ton_price;
    private ProgressDialog pDialog;
    private TextView txtCapacity, txtBody, txtLength;

    private List<TruckPayload> capacityList = new ArrayList<TruckPayload>();
    private List<TruckBody> bodyList = new ArrayList<TruckBody>();
    private List<TruckLength> lengthList = new ArrayList<TruckLength>();
    private ListView listView;
    public static ViewGroup rel;
    private int myType;
    public static ListView lToday, ltomorrow;
    public static TodayFragment fragment;
    private int rowSelection1, rowSelection2, rowSelection3;
    private Button addTruckData;
    private Button buttonNext;
    public static ViewGroup todayContainer, tomorrowContainer;
    private ArrayList<TruckCartObj> truckPayloadsList;
    private ArrayList<TruckCartObj> truckbodyList;
    private ArrayList<TruckCartObj> truckLengthList;


    public TodayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodayFragment newInstance(String param1, String param2) {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        fragment = this;


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppController.getInstance().hideSoftKeyboard(getActivity());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_today12, container, false);
        todayContainer = (ViewGroup) v.findViewById(R.id.todayContainer);
        tomorrowContainer = (ViewGroup) v.findViewById(R.id.tomorrowContainer);
        addTruckData = (Button) v.findViewById(R.id.button_addtruck);
        addTruckData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTruck();
            }
        });
//Get the widgets reference from XML layout
        final TextView tv = (TextView) v.findViewById(R.id.tv);
        np = (NumberPicker) v.findViewById(R.id.np);
        np1 = (NumberPicker) v.findViewById(R.id.np1);
        np2 = (NumberPicker) v.findViewById(R.id.np2);

        //Initializing a new string array with elements

        txtCapacity = (TextView) v.findViewById(R.id.txt_capacity);
        txtBody = (TextView) v.findViewById(R.id.text_body);
        txtLength = (TextView) v.findViewById(R.id.text_length);
        rel = (ViewGroup) v.findViewById(R.id.capacity_pop);
        buttonNext = (Button) getActivity().findViewById(R.id.button_confirm);
        buttonNext.setVisibility(View.GONE);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextData();
            }
        });
        rel.setVisibility(View.GONE);
        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        access_token = (mSharedPreference.getString("Token", null));
        pickup_address = "[84]";
        dropoff_address = "[86]";
        booking_date = "13-09-2016";
        String payload_range = "1";
        String body_type = "1";
        String user_id = "28";
        String date = "17-09-2016";
        String capacity="19" ;
        String length = "20";
        String per_ton_price = "4500";
        TruckData(access_token, pickup_address, dropoff_address, booking_date, payload_range, body_type);

        listView = (ListView) v.findViewById(R.id.list);

        txtCapacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel.setVisibility(View.VISIBLE);
                setAdapterToListVIew(0);
            }
        });

        txtBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel.setVisibility(View.VISIBLE);
                setAdapterToListVIew(1);
            }
        });


        txtLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel.setVisibility(View.VISIBLE);
                setAdapterToListVIew(2);
            }
        });
        //Sliding layout
        mLayout = (SlidingUpPanelLayout) v.findViewById(R.id.sliding_layout);
        panelListener();
        lToday = (ListView) v.findViewById(R.id.listToday);
        ltomorrow = (ListView) v.findViewById(R.id.listTomorrow);
        //End Sliding layout
        return v;
    }

    public static void openSlidingPanel() {
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    public static void closeSlidingPanel() {
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }


    private void addTruck() {
        //API call for TRuck allocation
        //TruckAllocation(access_token, pickup_address, dropoff_address, booking_date, user_id, date, capacity, body_type, length, per_ton_price);


        TruckPayload truckPayLoadObj = TruckDataList.truckPayloadsList.get(rowSelection1);
        TruckBody bodyObj = TruckDataList.truckbodyList.get(rowSelection2);
        TruckLength lengthObj = TruckDataList.truckLengthList.get(rowSelection3);

        TruckCartObj truckCartObj = new TruckCartObj();

        truckCartObj.setPayload(truckPayLoadObj.getPayload());
        truckCartObj.setPayload_id(truckPayLoadObj.getPayload_id());
        truckCartObj.setPayLoadPricePerTon(truckPayLoadObj.getPrice_Per_Ton());
        truckCartObj.setPayLoadTotal(truckPayLoadObj.getTotal());

        truckCartObj.setBodyTotal(bodyObj.getTotal());
        truckCartObj.setBodyType(bodyObj.gettype());
        truckCartObj.setBodyTypeId(bodyObj.getbody_type_id());

        lengthObj.setlength(lengthObj.getlength());
        lengthObj.setlength_id(lengthObj.getlength_id());

        TruckCartList.addToTodayList(truckCartObj);
        Log.e("addingToCart", "" + TruckCartList.getTodayList().size() + " "+truckCartObj.getPayload() +" "+truckPayLoadObj.getPayload_id()+" "+truckCartObj.getPayLoadPricePerTon()+" "+truckCartObj.getPayLoadTotal()+" "+truckCartObj.getBodyType()+" "+truckCartObj.getBodyTypeId()+" "+truckCartObj.getBodyTotal()+" "+truckCartObj.getLengthId()+" "+truckCartObj.getLengthLength()+" "+truckCartObj.getLengthTotal());

        Toast.makeText(getActivity(), "" + TruckCartList.getTodayList().size(), Toast.LENGTH_SHORT).show();
    }

    public void gotSelection(int selctedNum) {

        if (myType == 0) {
            Log.e("GotCapacitor", "" + capacityList.get(selctedNum).getPayload());
            setNumberPicker1Val(selctedNum);
        } else if (myType == 1) {
            Log.e("GotCapacitor", "" + bodyList.get(selctedNum).getbody_type_id());
            setNumberPicker2Val(selctedNum);
        } else if (myType == 2) {
            Log.e("GotCapacitor", "" + lengthList.get(selctedNum).getlength());
            setNumberPicker3Val(selctedNum);
        }

        rel.setVisibility(View.GONE);
    }

    private void setAdapterToListVIew(int type) {
        myType = type;
        if (type == 0) {
            capacityList = TruckDataList.truckPayloadsList;
            CapacityListAdapter Capacityadapter = new CapacityListAdapter(getActivity(), capacityList, TodayFragment.this);
            listView.setAdapter(Capacityadapter);
        } else if (type == 1) {
            bodyList = TruckDataList.truckbodyList;
            BodyListAdapter Capacityadapter = new BodyListAdapter(getActivity(), bodyList, TodayFragment.this);
            listView.setAdapter(Capacityadapter);
        } else if (type == 2) {
            lengthList = TruckDataList.truckLengthList;
            LengthListAdapter Capacityadapter = new LengthListAdapter(getActivity(), lengthList, TodayFragment.this);
            listView.setAdapter(Capacityadapter);
        }
    }

    private void updateData() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("hello");
        stringBuilder.append("hi");
        stringBuilder.toString();

       /* final String[] values= {stringBuilder.toString(),"Green", "Blue", "Yellow", "Magenta"};
        final String[] values1= {"A","B", "C", "D", "E"};
        final String[] values2= {"Red","Green", "Blue", "Yellow", "Magenta"};*/


        //truckPayloadsList = TruckDataList.truckPayloadsList;
       // truckbodyList = TruckDataList.truckPayloadsList;
       // truckLengthList = TruckDataList.truckPayloadsList;

        truckPayloadsList = new ArrayList<>();
        truckbodyList =  new ArrayList<>();
        truckLengthList =  new ArrayList<>();


        Log.e("values"," size is "+TruckDataList.truckPayloadsList.size() + " " +TruckDataList.truckbodyList.size() + " "+TruckDataList.truckLengthList.size());
        for (int i = 0; i < TruckDataList.truckPayloadsList.size(); i++) {
            TruckCartObj obj = new TruckCartObj();
            obj.setPayload(TruckDataList.truckPayloadsList.get(i).getPayload());
            obj.setPayload_id(TruckDataList.truckPayloadsList.get(i).getPayload_id());
            obj.setPayLoadPricePerTon(TruckDataList.truckPayloadsList.get(i).getPrice_Per_Ton());
            obj.setPayLoadTotal(TruckDataList.truckPayloadsList.get(i).getTotal());

            obj.setBodyTypeId(TruckDataList.truckbodyList.get(i).getbody_type_id());
            obj.setBodyType(TruckDataList.truckbodyList.get(i).gettype());
            obj.setBodyTotal(TruckDataList.truckbodyList.get(i).getTotal());

            obj.setLengthId(TruckDataList.truckLengthList.get(i).getlength_id());
            obj.setLengthLength(TruckDataList.truckLengthList.get(i).getlength_id());
            obj.setLengthTotal(TruckDataList.truckLengthList.get(i).getTotal());
            truckPayloadsList.add(obj);
            truckbodyList.add(obj);
            truckLengthList.add(obj);

            Log.e("addingToCart", "" + TruckCartList.getTodayList().size() + " "+obj.getPayload() +" "+obj.getPayload_id()+" "+obj.getPayLoadPricePerTon()+" "+obj.getPayLoadTotal()+" "+obj.getBodyType()+" "+obj.getBodyTypeId()+" "+obj.getBodyTotal()+" "+obj.getLengthId()+" "+obj.getLengthLength()+" "+obj.getLengthTotal());
        }


       /* //Populate NumberPicker values from String array values
        //Set the minimum value of NumberPicker
        np.setMinValue(0); //from array first value
        np1.setMinValue(0);
        np2.setMinValue(0);
        //Specify the maximum value/number of NumberPicker
        np.setMaxValue(values.length-1); //to array last value
        np1.setMaxValue(values1.length-1);
        np2.setMaxValue(values2.length-1);
        //Specify the NumberPicker data source as array elements
        np.setDisplayedValues(values);
        np1.setDisplayedValues(values1);
        np2.setDisplayedValues(values2);
        //Gets whether the selector wheel wraps when reaching the min/max value.
        np.setWrapSelectorWheel(true);
        np1.setWrapSelectorWheel(true);
        np2.setWrapSelectorWheel(true);*/


        String[] val = new String[truckPayloadsList.size()];
        for (int i = 0; i < truckPayloadsList.size(); i++) {
            String newVal = truckPayloadsList.get(i).getPayload();
            val[i] = newVal;
            Log.e("values", i+" list1 "+newVal);
        }

        np.setDisplayedValues(val);

        String[] val2 = new String[truckbodyList.size()];
        for (int i = 0; i < truckbodyList.size(); i++) {
            val2[i] = truckbodyList.get(i).getBodyType();
            Log.e("values", i+ "list2 "+ val2[i]);



        }
        np1.setDisplayedValues(val2);

        String[] val3 = new String[truckLengthList.size()];
        for (int i = 0; i < truckLengthList.size(); i++) {
            val3[i] = truckLengthList.get(i).getLengthLength();
            Log.e("values",  i+"list3 "+ val3[i]);
        }
        np2.setDisplayedValues(val3);

        np.setMinValue(0); //from array first value
        Log.e("@setting Value", "Number Picker1");
        np1.setMinValue(0);
        np2.setMinValue(0);

        np.setMaxValue(val.length - 1); //to array last value
        np1.setMaxValue(val2.length - 1);
        np2.setMaxValue(val3.length - 1);

        np.setWrapSelectorWheel(true);
        np1.setWrapSelectorWheel(true);
        np2.setWrapSelectorWheel(true);


        Log.e("@set Value", "Number Picker1");


        //Set a value change listener for NumberPicker
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected value from picker
                // tv.setText("Selected value : " + values[newVal]);
                Log.e("Picker1", "Selected value :" + newVal + " " + truckPayloadsList.get(newVal).getPayload_id());
                rowSelection1 = newVal;
                filterList2(rowSelection1);


            }
        });
        //Set a value change listener for NumberPicker
        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected value from picker
                //Log.e("Picker1", "Selected value :" + values1[newVal]);
                Log.e("Picker2", "Selected value :" + newVal + " " + truckPayloadsList.get(newVal).getPayload_id());
                rowSelection2 = newVal;
            }
        });
        //Set a value change listener for NumberPicker
        np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected value from picker
                //Log.e("Picker2", "Selected value :" + values2[newVal]);
                rowSelection3 = newVal;
                Log.e("Picker3", "Selected value :" + newVal + " " + truckPayloadsList.get(newVal).getPayload_id());
            }
        });


    }

    private void filterList2(int list1Pos) {
        ArrayList<TruckCartObj> newList2 = new ArrayList<>();

        for (int i = 0; i < truckPayloadsList.size(); i++) {
            TruckCartObj currObj = truckPayloadsList.get(i);
           // String newVal = values[i].getPayload() *//*+ "/n" + values[i]. getPayload_id()*//* + "\n" + values[i].getPayload_id();
           // val[i] = newVal;
           // Log.e("values", i+" list1 "+newVal);
            //if(currObj.getPayload().equals(truckPayloadsList.get(list1Pos).getPayload())){
                newList2.add(currObj);
           // }


        }

        String[] val2 = new String[newList2.size()];
        for (int i = 0; i < newList2.size(); i++) {
            val2[i] = newList2.get(i).getBodyType();
            Log.e("values", i+ "list2 "+ val2[i]);
        }




        if(val2.length > 2){
            np1.setMaxValue(val2.length - 1);
        }else {
            np1.setMaxValue(0);
        }

        np1.setValue(0);
        np1.setDisplayedValues(val2);






        Log.e("list2", "new list is "+newList2.size());


    }

    public void setNumberPicker1Val(int pos){
        np.setValue(pos);
    }

    public void setNumberPicker2Val(int pos){
        np1.setValue(pos);
    }

    public void setNumberPicker3Val(int pos){
        np2.setValue(pos);
    }

    private void nextData() {
        ((Activity_Tuck12)getActivity()).goToNextPage();

    }


    @Override
    public void onTodayFragmentInteraction(String string) {

    }

    @Override
    public void TomorrowFragmentInteraction() {

    }


    public interface OnFragmentInteractionListener {
        public void onTodayFragmentInteraction(String string);

        void onTomorrowFragmentInteraction(String string);
    }

    //New API
    private void TruckData(final String access_token, final String pickup_address, final String dropoff_address,
                           final String booking_date, final String payload_range, final String body_type) {
        if (AppController.getInstance().isConnected(getActivity())) {
            // Tag used to cancel the request
            String tag_string_req = "req_Truck";

            pDialog.setMessage("Truck in ...");
            showDialog();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Config_URL.URL_TRUCK_SELECTION_PRICING_NEW, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("Truck_data", "TruckSelect Response: " + response.toString());
                    Log.d("Truck_data", "TruckSelect Response: " + response.toString());
                    hideDialog();

                    try {

                        JSONObject jObj = new JSONObject(response);
                        //  boolean error = jObj.getBoolean("error");
                        String error = jObj.getString("message");
                        int status = jObj.getInt("status");

                        if (status == 1) {
                            try {
                                // JSONObject data = jObj.getJSONObject("data");

                                  /* JSONArray PayloadArray = data.getJSONArray("trucks_payload");
                                TruckDataList.truckPayloadsList = new ArrayList<>();
                              for(int i=0; i<PayloadArray.length(); i++)
                                {
                                    JSONObject payloadObj = PayloadArray.getJSONObject(i);
                                    String payload_id = payloadObj.optString("payload_id");
                                    String payload = payloadObj.optString("payload");
                                    String total = payloadObj.optString("total");
                                    String price_per_ton = payloadObj.optString("price_per_ton");
                                    TruckPayload tp = new TruckPayload();
                                  tp.setPayload_id(payload_id);
                                    tp.setPayload(payload);
                                    tp.setPrice_Per_Ton(price_per_ton);
                                    tp.setTotal(total);
                                    TruckDataList.truckPayloadsList.add(tp);
                                    Log.e("@TruckPayload", tp.toString());
                                }

                                JSONArray BodyArray = data.getJSONArray("trucks_body_type");
                                TruckDataList.truckbodyList = new ArrayList<>();
                                for(int i=0; i<BodyArray.length(); i++)
                                {
                                    JSONObject payloadObj = BodyArray.getJSONObject(i);
                                    String body_type_id = payloadObj.optString("body_type_id");
                                    String type = payloadObj.optString("type");
                                    String total = payloadObj.optString("total");

                                    TruckBody tb= new TruckBody();
                                    tb.setbody_type_id(body_type_id);
                                    tb.settype(type);
                                    tb.setTotal(total);

                                    TruckDataList.truckbodyList.add(tb);
                                    Log.e("@TruckBody", tb.toString());
                                 }
                                JSONArray Lengtharray = data .getJSONArray("trucks_length");
                                TruckDataList.truckLengthList = new ArrayList<>();
                                for(int i=0; i<Lengtharray.length(); i++)
                                {
                                    JSONObject payloadObj = Lengtharray.getJSONObject(i);
                                    String length_id = payloadObj.optString("length_id");
                                    String length = payloadObj.optString("length");
                                    String total = payloadObj.optString("total");

                                    TruckLength tl = new TruckLength();
                                    tl.setlength_id(length_id);
                                    tl.setlength(length);
                                    tl.setTotal(total);
                                    TruckDataList.truckLengthList.add(tl);
                                    Log.e("@TruckLength", tl.toString());
                                }

                                updateData();*/



                                JSONArray daJsonArray = jObj.optJSONArray("data");
                                TruckDataList.truckPayloadsList = new ArrayList<>();
                                TruckDataList.truckbodyList = new ArrayList<>();
                                TruckDataList.truckLengthList = new ArrayList<>();
                                for (int i = 0; i < daJsonArray.length(); i++) {
                                    JSONObject truckdata = daJsonArray.getJSONObject(i);

                                    String payload_id = truckdata.optString("payload_id");
                                    String payload = truckdata.optString("payload");
                                    String length_id = truckdata.optString("length_id");
                                    String length = truckdata.optString("length");
                                    String body_type_id = truckdata.optString("body_type_id");
                                    String type = truckdata.optString("type");
                                    String isavailable = truckdata.optString("isavailable");
                                    String url = truckdata.optString("url");

                                    TruckPayload tp = new TruckPayload();
                                    tp.setPayload_id(payload_id);
                                    tp.setPayload(payload);
                                    //tp.setPrice_Per_Ton(price_per_ton);
                                    //tp.setTotal(total);
                                    TruckDataList.truckPayloadsList.add(tp);


                                    TruckBody tb= new TruckBody();
                                    tb.setbody_type_id(body_type_id);
                                    tb.settype(type);
                                   // tb.setTotal(total);
                                    TruckDataList.truckbodyList.add(tb);


                                    TruckLength tl = new TruckLength();
                                    tl.setlength_id(length_id);
                                    tl.setlength(length);
                                    //tl.setTotal(total);
                                    TruckDataList.truckLengthList.add(tl);
                                }



                                updateData();



                            } catch (Exception ex) {
                                ex.toString();
                            }
                        } else {

                        }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Truck_data", "Oauth Error: " + error.getMessage());
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
                    params.put("tag", "Truck_select");
                    params.put("access_token", access_token);
                   /* params.put("pickup_address", "[56]");
                    params.put("dropoff_address","[58]");
                    params.put("booking_date","13-09-2016");*/
                    params.put("pickup_address", pickup_address);
                    params.put("dropoff_address", dropoff_address);
                    params.put("booking_date", booking_date);
                    params.put("body_type", "1");
                    params.put("payload_range", "1");
                    return params;
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

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }



    private void setTodayListView() {
        /*final TruckCartadapter truckCartAdapter = new TruckCartadapter(getActivity(), TruckCartList.todayList, TodayFragment.this);
        lToday.setAdapter(truckCartAdapter);*/

        Activity_Tuck12 activity = (Activity_Tuck12) getActivity();
        activity.showLists(0);


    }

    public void panelListener() {


        mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

            // During the transition of expand and collapse onPanelSlide function will be called.
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.e(TAG, "onPanelSlide, offset " + slideOffset);
                setTodayListView();
            }

            // This method will be call after slide up layout
            @Override
            public void onPanelExpanded(View panel) {
                Log.e(TAG, "onPanelExpanded");

               buttonNext.setVisibility(View.VISIBLE);


            }

            // This method will be call after slide down layout.
            @Override
            public void onPanelCollapsed(View panel) {
                Log.e(TAG, "onPanelCollapsed");
               buttonNext.setVisibility(View.GONE);

            }

            @Override
            public void onPanelAnchored(View panel) {
                Log.e(TAG, "onPanelAnchored");
            }

            @Override
            public void onPanelHidden(View panel) {
                Log.e(TAG, "onPanelHidden");
            }
        });
    }


    //New API         TruckAllocation(access_token, pickup_address, dropoff_address, booking_date, user_id, date, capacity, body_type, length, per_ton_price);


    private void TruckAllocation(final String access_token, final String pickup_address, final String dropoff_address,
                                 final String booking_date, final String payload_range, final String body_type, final String user_id, String date, final String capacity,
                                 final String length) {
        if (AppController.getInstance().isConnected(getActivity())) {
            // Tag used to cancel the request
            String tag_string_req = "Truck_Allocation";

            pDialog.setMessage("Truck in ...");
            showDialog();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Config_URL.URL_TRUCK_ALLOCATION, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("Truck_data", "TruckSelect Response: " + response.toString());
                    hideDialog();

                    try {

                        JSONObject jObj = new JSONObject(response);
                        //  boolean error = jObj.getBoolean("error");
                        String error = jObj.getString("message");
                        int status = jObj.getInt("status");

                        if (status == 1) {
                          /*  try {
                                // JSONObject data = jObj.getJSONObject("data");
                                JSONArray daJsonArray = jObj.optJSONArray("data");
                               for (int i = 0; i < daJsonArray.length(); i++) {
                                    JSONObject truckdata = daJsonArray.getJSONObject(i);

                                    String payload_id = truckdata.optString("payload_id");
                                    String payload = truckdata.optString("payload");
                                    String length_id = truckdata.optString("length_id");
                                    String length = truckdata.optString("length");
                                    String body_type_id = truckdata.optString("body_type_id");
                                    String type = truckdata.optString("type");
                                    String isavailable = truckdata.optString("isavailable");
                                    String url = truckdata.optString("url");
                                }


                            } catch (Exception ex) {
                                ex.toString();
                            }*/
                        } else {

                        }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Truck_data", "Oauth Error: " + error.getMessage());
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
                    params.put("tag", "Truck_Allocate");
                    params.put("access_token", access_token);
                    params.put("pickup_address", pickup_address);
                    params.put("dropoff_address", dropoff_address);
                    params.put("booking_date", booking_date);
                    params.put("body_type", "1");
                    params.put("payload_range", "1");
                    params.put("user_id", user_id);
                    params.put("capacity",capacity);
                    params.put("length",length);
                    return params;
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
    }
}
