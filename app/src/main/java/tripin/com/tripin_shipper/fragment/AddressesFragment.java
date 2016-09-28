package tripin.com.tripin_shipper.fragment;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tripin.com.tripin_shipper.AppController;
import tripin.com.tripin_shipper.R;
import tripin.com.tripin_shipper.activity.Activity_AddressBook;
import tripin.com.tripin_shipper.activity.Activity_AddressPort;
import tripin.com.tripin_shipper.activity.Activity_Dashboard;
import tripin.com.tripin_shipper.activity.Activity_Tuck12;
import tripin.com.tripin_shipper.model.AddressList;
import tripin.com.tripin_shipper.model.AddressObj;
import tripin.com.tripin_shipper.volley.Config_URL;

/**
 * Created by designtripin on 09/09/16.
 */
public class AddressesFragment extends Fragment {
    private ViewGroup currFragment;
    private Activity_Dashboard activity;
    public AddressSwapPickUpFragment cheapestRoutePickUpFragment;
    public AddressSwapDropFragment cheapestRoteDropUpFragment;
    private ViewGroup showCheapestRoadTvParent, confirmRouteTvParent, trucksAvailableTvParent;
    private TextView showCheapestRoadTv, confirmRouteTv, trucksAvailableTv;
    private ViewGroup firstRouteDialogContainer, secondRouteDialogContainer;
    private ArrayList<AddressObj> pickUpList, dropList;
    private LayoutInflater inflater;
    private ViewGroup addressSwapVg;
    private ViewGroup pickUpContainer, dropContainer;
    public ViewGroup addressesAlert, swipeIndicationVg;
    private TextView pickUpOptionDialogTv, dropOptionDialogTv;
    private static final String TAG = AddressesFragment.class.getSimpleName();
    String UserId,Access_Token; private Bundle bundle;
    private int deleteSingleType, deleteSingleId;
    private ProgressDialog pDialog;
    public int cheapestRouteDeleteType, cheapestRouteDeletePosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currFragment = (ViewGroup) inflater.inflate(R.layout.fragment_addresses, container, false);
        return currFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init(){
        setInstances();
        //setRouteChangeFunc();
        setVisiblityOfBottomBtns(-1);
        //setAlertDialog(0, "test", "yes", "no");
        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity());
        Access_Token=(mSharedPreference.getString("Token", null));

    }

    private void SwapImage(ImageView imageView, int imageId){
        Drawable res = getResources().getDrawable(imageId);
        imageView.setImageDrawable(res);
    }

    private void addSelectedPickUpAddresses(){
      firstRouteDialogContainer.removeAllViewsInLayout();
        ViewGroup pickUpAddressesContainer = (ViewGroup) inflater.inflate(R.layout.selected_pickup_addresses, null);
        firstRouteDialogContainer.addView(pickUpAddressesContainer);
        pickUpAddressesContainer.setVisibility(View.VISIBLE);

        ViewGroup container = (ViewGroup) pickUpAddressesContainer.findViewById(R.id.container);

        pickUpList = AddressList.pickUpList;
        for(int i=0;i<pickUpList.size();i++){
            AddressObj addressObj = pickUpList.get(i);
            Log.e("original", ""+ i + " " + addressObj.getName());
            final int finalId = i;
            String id = String.valueOf(finalId+1 );

            ViewGroup pickUpRow = (ViewGroup) inflater.inflate(R.layout.single_pick_up_drop_item, null);
            View vertLine1 = pickUpRow.findViewById(R.id.vertLine1);
            View vertLine2 = pickUpRow.findViewById(R.id.vertLine2);
            TextView idTv = (TextView) pickUpRow.findViewById(R.id.idTv);
            TextView nameTv = (TextView) pickUpRow.findViewById(R.id.nameTv);
            TextView addressTv = (TextView) pickUpRow.findViewById(R.id.addressTv);
            ImageView addTv = (ImageView) pickUpRow.findViewById(R.id.addTv);
            ImageView imageCircle = (ImageView) pickUpRow.findViewById(R.id.imageCircle);

            container.addView(pickUpRow);
            idTv.setText("");
            nameTv.setText(addressObj.getName());
            addressTv.setText(addressObj.getAddress());
            SwapImage(imageCircle, R.mipmap.greencircle);
            addTv.setVisibility(View.GONE);

            if(i==0){
                vertLine1.setVisibility(View.INVISIBLE);
            }


            if( pickUpList.size() < 3 && i >= pickUpList.size()-1 ){
                addTv.setVisibility(View.VISIBLE);
            }

            if(dropList.size() > 1 && pickUpList.size() == 1){
                addTv.setVisibility(View.GONE);
            }

            if(i>0){
                ViewGroup pickUpRowLine = (ViewGroup) inflater.inflate(R.layout.single_pick_up_drop_item_only_vert_line, null);
                //container.addView(pickUpRowLine);
            }



            addTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callAddressBook(0);
                }
            });

            pickUpRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(AddressList.pickUpList.size() > 1){
                        Log.e("deleting", "deleting pickup " +finalId  );
                        //deleteEntry(0, finalId);
                        deleteSingleType = 0;
                        deleteSingleId = finalId;
                        setAlertDialog(2, "DELETE ADDRESS","DELETE", "CANCEL");
                    }

                    return false;
                }
            });
        }
        TextView showCheapestRouteTv = (TextView) pickUpAddressesContainer.findViewById(R.id.pickUpOptionDialogTv);
        if(AddressList.pickUpList.size() == 1 && AddressList.dropList.size() > 1){
            if(AddressList.cheapestRoutePickUpList.size() > 0 && AddressList.cheapestRouteDropList.size() > 0){
                showCheapestRouteTv.setVisibility(View.VISIBLE);
            }else{
            }

        }else{
            showCheapestRouteTv.setVisibility(View.GONE);
        }

        showCheapestRouteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCheapestRoute();
            }
        });
    }

    public void cheapestRoutePickUpAddressDeleted(int position){
        AddressList.pickUpList.remove(position);
        cheapestRoutePickUpFragment.adapter.notifyDataSetChanged();
        showCheapestRoute();

        if(AddressList.pickUpList.size() <= 1){
            showOriginalContainers();
            checkUserInputs();
        }

    }

    public void cheapestRouteDropAddressDeleted(int position){
        AddressList.dropList.remove(position);
        cheapestRoteDropUpFragment.adapter.notifyDataSetChanged();
        showCheapestRoute();

        if(AddressList.dropList.size() <= 1){
            showOriginalContainers();
            checkUserInputs();
        }

    }

    public void setAlertDialog(final int type, String headingText, String positiveText, String negativeText){
        addressesAlert.setVisibility(View.VISIBLE);
        TextView headingTv = (TextView) addressesAlert.findViewById(R.id.heading);
        TextView positiveTv = (TextView) addressesAlert.findViewById(R.id.positiveBtn);
        TextView negativeTv = (TextView) addressesAlert.findViewById(R.id.negativeBtn);


        headingTv.setText(headingText);
        positiveTv.setText(positiveText);
        negativeTv.setText(negativeText);

        positiveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressesAlert.setVisibility(View.GONE);
                pickUpOptionDialogTv.setVisibility(View.GONE);
                dropOptionDialogTv.setVisibility(View.GONE);
                if(type == 0){
                    swipeIndicationVg.setVisibility(View.VISIBLE);
                }else  if(type == 1){
                    confirmChangedRoute();
                }else  if(type == 2){
                    deleteEntry(deleteSingleType,deleteSingleId );
                }else  if(type == 2){
                    deleteEntry(deleteSingleType,deleteSingleId );
                }else  if(type == 3){
                    if(cheapestRouteDeleteType == 0){
                        cheapestRoutePickUpAddressDeleted(cheapestRouteDeletePosition);
                    }else  if(cheapestRouteDeleteType == 1){
                        cheapestRouteDropAddressDeleted(cheapestRouteDeletePosition);
                    }
                }
            }
        });

        negativeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressesAlert.setVisibility(View.GONE);

            }
        });


    }
    private void changeOriginalListsAccordingToSelection(){
        AddressList.pickUpList = new ArrayList<>();
        AddressList.dropList = new ArrayList<>();

        AddressList.pickUpList.addAll(AddressList.cheapestRoutePickUpList);
        AddressList.dropList.addAll(AddressList.cheapestRouteDropList);
    }

    private void confirmChangedRoute(){
        changeOriginalListsAccordingToSelection();
        showOriginalContainers();
        setVisiblityOfBottomBtns(0);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setBackGroundOfView(View targetView, int imageToSet){
        final int sdk = Build.VERSION.SDK_INT;
        if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
            targetView.setBackgroundDrawable( getResources().getDrawable(imageToSet) );
        } else {
            targetView.setBackground( getResources().getDrawable(imageToSet));
        }
    }

    private void addSelectedDropAddresses(){
       secondRouteDialogContainer.removeAllViewsInLayout();
        ViewGroup pickUpAddressesContainer = (ViewGroup) inflater.inflate(R.layout.selected_pickup_addresses, null);
        secondRouteDialogContainer.addView(pickUpAddressesContainer);
        pickUpAddressesContainer.setVisibility(View.VISIBLE);

        ViewGroup container = (ViewGroup) pickUpAddressesContainer.findViewById(R.id.container);

        dropList = AddressList.dropList;
        for(int i=0;i<dropList.size();i++){
            AddressObj addressObj = dropList.get(i);
            final int  finalId = i;
            String id = String.valueOf(finalId+1);

            ViewGroup pickUpRow = (ViewGroup) inflater.inflate(R.layout.single_pick_up_drop_item, null);
            TextView idTv = (TextView) pickUpRow.findViewById(R.id.idTv);
            View vertLine1 = pickUpRow.findViewById(R.id.vertLine1);
            View vertLine2 = pickUpRow.findViewById(R.id.vertLine2);
            if(i==dropList.size()-1){
                vertLine2.setVisibility(View.INVISIBLE);
            }
            TextView nameTv = (TextView) pickUpRow.findViewById(R.id.nameTv);
            TextView addressTv = (TextView) pickUpRow.findViewById(R.id.addressTv);
            ImageView addTv = (ImageView) pickUpRow.findViewById(R.id.addTv);
            ImageView imageCircle = (ImageView) pickUpRow.findViewById(R.id.imageCircle);
            container.addView(pickUpRow);
            idTv.setText("");
            nameTv.setText(addressObj.getName());
            addressTv.setText(addressObj.getAddress());
            SwapImage(imageCircle, R.mipmap.red_circle);
            addTv.setVisibility(View.GONE);

            if(pickUpList.size() == 1 && dropList.size() < 3 && i >= dropList.size()-1 ){
                addTv.setVisibility(View.VISIBLE);
            }


            if(pickUpList.size() > 1 && dropList.size() == 1){
                addTv.setVisibility(View.GONE);
            }

            addTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    callAddressBook(1);
                }
            });



            pickUpRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(AddressList.dropList.size() > 1){
                        Log.e("deleting", "deleting drop " +finalId  );
                        //deleteEntry(1, finalId);
                        deleteSingleType = 1;
                        deleteSingleId = finalId;
                        setAlertDialog(2, "DELETE ADDRESS", "DELETE", "CANCEL");
                    }

                    return false;
                }
            });
        }

        TextView showCheapestRouteTv = (TextView) pickUpAddressesContainer.findViewById(R.id.pickUpOptionDialogTv);
        if(AddressList.dropList.size() == 1 && AddressList.pickUpList.size() > 1){
            if(AddressList.cheapestRoutePickUpList.size() > 0 && AddressList.cheapestRouteDropList.size() > 0){
                showCheapestRouteTv.setVisibility(View.VISIBLE);
            }else{
                showCheapestRouteTv.setVisibility(View.GONE);
            }

        }else{
            showCheapestRouteTv.setVisibility(View.GONE);
        }


        showCheapestRouteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCheapestRoute();
            }
        });
    }

    private void deleteEntry(int whichType, int listId){

        Log.e("dleting", whichType + " "+listId);
            if(whichType == 0){
                if(AddressList.pickUpList.size() > 1){
                    AddressList.pickUpList.remove(listId);
                    //listPickUpUpdated(AddressList.pickUpList);
                    addSelectedPickUpAddresses();
                    addSelectedDropAddresses();
                }

            }else  if(whichType == 1){
                if(AddressList.dropList.size() > 1){
                    AddressList.dropList.remove(listId);
                    //listDropUpUpdated(AddressList.dropList);
                    addSelectedPickUpAddresses();
                    addSelectedDropAddresses();
                }

            }

        setLogicForBottomBtnsVisibility();
    }
    private void showCheapestRoute(){
        setCheapestRouteList();

    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public void setCheapestRouteList(){
        AddressList.cheapestRoutePickUpList = new ArrayList<>();
        AddressList.cheapestRouteDropList = new ArrayList<>();
        //addDataToCheapestRoute();

        JSONArray pickUpArrayIdList = new JSONArray();
        JSONArray dropUpArrayIdList = new JSONArray();

        for(int i=0;i<AddressList.pickUpList.size();i++){
            pickUpArrayIdList.put(AddressList.pickUpList.get(i).getId());
        }

        for(int i=0;i<AddressList.dropList.size();i++){
            dropUpArrayIdList.put(AddressList.dropList.get(i).getId());
        }
        //final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity());
        //Access_Token=(mSharedPreference.getString("Token", null));
        //getCheapestRouteFromApi( pickUpArrayIdList.toString(), dropUpArrayIdList.toString(), Access_Token/*, getDate(System.currentTimeMillis(), "dd-MM-yyyy") */);

        gotCheapestRoute();
    }


    /**
     * function to verify login details in mysql db
     * */
    private void getCheapestRouteFromApi(final String pickup_address, final String dropoff_address, final String access_Token/*, final String booking_date*/) {
        if (AppController.getInstance().isConnected(getActivity()))
        {
            // Tag used to cancel the request
            String tag_string_req = "req_login";

           /* pDialog.setMessage("Logging in ...");
            showDialog();*/

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Config_URL.URL_CHEPEST_ROUTE, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "CHEPEST Response: " + response.toString());
                //    hideDialog();

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

                        } else {
                            // Error in login. Get the error message
                            String errorMsg = jObj.getString("message");
                            Toast.makeText(getActivity(),
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
                    params.put("tag", "chepestRoute");
                    params.put("pickup_address", pickup_address);
                    params.put("dropoff_address", dropoff_address);
                    params.put("access_token",access_Token);
                  //  params.put("booking_date", booking_date);

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
    }
*/



    private void gotCheapestRoute(){
        addDataToCheapestRoute();
        setRouteChangeFunc();
    }

    public void addDataToCheapestRoute(){
        for(int i=0;i<AddressList.pickUpList.size();i++){
            AddressList.cheapestRoutePickUpList.add(AddressList.pickUpList.get(i));
        }

        for(int i=0;i<AddressList.dropList.size();i++){
            AddressList.cheapestRouteDropList.add(AddressList.dropList.get(i));
        }

        Collections.shuffle(AddressList.cheapestRoutePickUpList);
        Collections.shuffle(AddressList.cheapestRouteDropList);
    }

    private void setRouteChangeFunc(){
        addressSwapVg = (ViewGroup) currFragment.findViewById(R.id.addressSwapVg);
        addressSwapVg.setVisibility(View.VISIBLE);
        firstRouteDialogContainer.setVisibility(View.GONE);
        secondRouteDialogContainer.setVisibility(View.GONE);
        setCheapestRoutePickUpFragment();
        setCheapestRouteDropUpFragment();
        setVisiblityOfBottomBtns(0);
        swipeIndicationVg.setVisibility(View.GONE);
    }

    private void setInstances(){
        activity = (Activity_Dashboard) getActivity();
        addressesAlert = (ViewGroup) currFragment.findViewById(R.id.common_alert);
        swipeIndicationVg = (ViewGroup) currFragment.findViewById(R.id.swipeIndicationVg);
        pickUpOptionDialogTv = (TextView) currFragment.findViewById(R.id.pickUpOptionDialogTv);
        dropOptionDialogTv = (TextView) currFragment.findViewById(R.id.dropOptionDialogTv);
        addressesAlert.setVisibility(View.GONE);
        swipeIndicationVg.setVisibility(View.GONE);
        inflater = (LayoutInflater)  activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pickUpList = new ArrayList<>();
        dropList = new ArrayList<>();
        setCheapestRouteBtn();
        setTrucksAvailableBtn();
        setConfirmRouteBtn();
        addFirstRouteDialog();
        addSecondRouteDialog();
        checkUserInputs();

        setEventsForDialogues();
    }



    public void toggleVisibilityOfpickUpOptionDialogTv(String text, int visibility){
        pickUpOptionDialogTv.setVisibility(visibility);
        pickUpOptionDialogTv.setText(text);
    }

    public void toggleVisibilityOfdropOptionDialogTv(String text, int visibility){
        dropOptionDialogTv.setVisibility(visibility);
        dropOptionDialogTv.setText(text);
    }

    private void setEventsForDialogues(){
        pickUpOptionDialogTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pickUpOptionDialogTv.getText().toString().equals(AddressList.SELECT_DIFFERENT_ROUTE) ){
                        AddressList.allowDropSwipe = true;
                    setAlertDialog(0, "Choose Different Route ", "CANCEL","YES");
                }else if(pickUpOptionDialogTv.getText().toString().equals(AddressList.SHOW_CHEAPEST_ROUTE) ){
                    showCheapestRoute();
                }
            }
        });

        dropOptionDialogTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dropOptionDialogTv.getText().toString().equals(AddressList.SELECT_DIFFERENT_ROUTE) ){
                    AddressList.allowDropSwipe = true;
                    setAlertDialog(0, "Choose Different Route ", "CANCEL", "YES");
                }else if(dropOptionDialogTv.getText().toString().equals(AddressList.SHOW_CHEAPEST_ROUTE) ){
                    showCheapestRoute();
                }
            }
        });
    }

    public void checkUserInputs(){
        ArrayList<AddressObj> pickUpList = AddressList.pickUpList;
        ArrayList<AddressObj> dropList = AddressList.dropList;
        Log.e("AddressedList", ""+ AddressList.pickUpList.size() + " " + AddressList.dropList.size());
        if(pickUpList.size() > 0){
            Log.e("pckup", "pick up has items");
            secondRouteDialogContainer.setVisibility(View.VISIBLE);
            firstRouteDialogContainer.setVisibility(View.VISIBLE);
            addSelectedPickUpAddresses();
        }else{
            secondRouteDialogContainer.setVisibility(View.GONE);
        }

        if(dropList.size() > 0){
            addSelectedDropAddresses();
        }else{

        }

        if(pickUpList.size()  > 0 && dropList.size() > 0){
            activity.setMapPoints(pickUpList, dropList);
        }

        setLogicForBottomBtnsVisibility();
    }

    private void setLogicForBottomBtnsVisibility(){
        if(pickUpList.size() == 0 || dropList.size() == 0){
            setVisiblityOfBottomBtns(-1);
        }else if(pickUpList.size() == 1 && dropList.size() == 1){
            setVisiblityOfBottomBtns(0);
        }else if(pickUpList.size() > 1 || dropList.size() > 1){
            setVisiblityOfBottomBtns(2);
        }
    }



    private void addFirstRouteDialog(){
        firstRouteDialogContainer = (ViewGroup) currFragment.findViewById(R.id.addFirstAddressConatiner);
        ViewGroup firstRouteDialog = (ViewGroup) inflater.inflate(R.layout.add_route_dialog_common, null);
        firstRouteDialogContainer.addView(firstRouteDialog);
        firstRouteDialog.setVisibility(View.VISIBLE);

        TextView firstLineTv = (TextView) firstRouteDialog.findViewById(R.id.firstLineTV);
        TextView secondLineTv = (TextView) firstRouteDialog.findViewById(R.id.secondLineTV);

        firstLineTv.setText("Where do you want the transport from");
        secondLineTv.setText("ADD CONSIGNER ADDRESS");

        secondLineTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddressBook(0);
            }
        });
    }

    private void callAddressBook(int whichType){
        /*if(pickUpList.size() > 0 && dropList.size() > 0){
            Intent intent  = new Intent(activity, AddressBookActivity.class);
            intent.putExtra("type", whichType);
            startActivity(intent);
        }else{
            if(pickUpList.size() == 0 && dropList.size() > 0){
                Toast.makeText(activity, "Please add Consigner first", Toast.LENGTH_LONG).show();
            }else  if(dropList.size() == 0 && pickUpList.size() > 0){
                Toast.makeText(activity, "Please add Consignee first", Toast.LENGTH_LONG).show();
            }
        }*/

       /* if(whichType == 0){
            if(dropList.size() == 0 && pickUpList.size() > 0){
                Toast.makeText(activity, "Please add Consignee first", Toast.LENGTH_LONG).show();
            }else{
                Intent intent  = new Intent(activity, AddressBookActivity.class);
                intent.putExtra("type", whichType);
                startActivity(intent);
            }
        }else if(whichType == 1){
              if(pickUpList.size() == 0 && dropList.size() > 0){
                Toast.makeText(activity, "Please add Consigner first", Toast.LENGTH_LONG).show();
            }else{
                  Intent intent  = new Intent(activity, AddressBookActivity.class);
                  intent.putExtra("type", whichType);
                  startActivity(intent);
              }
        }*/

        if(whichType == 0){
   /*         Intent intent  = new Intent(activity, Activity_AddressBook.class);
            intent.putExtra("type", whichType);
            startActivity(intent);*/

            final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity());
            UserId=(mSharedPreference.getString("UserId", null));
            address_Book(Access_Token, UserId, whichType);

        }else if(whichType == 1){
/*            Intent intent  = new Intent(activity, Activity_AddressBook.class);
            intent.putExtra("type", whichType);
            startActivity(intent);*/

            final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity());
            UserId=(mSharedPreference.getString("UserId", null));
            address_Book(Access_Token, UserId, whichType);
        }

    }


    private void addSecondRouteDialog(){
        secondRouteDialogContainer = (ViewGroup) currFragment.findViewById(R.id.addSecondFiAddressConatiner);
        ViewGroup firstRouteDialog = (ViewGroup) inflater.inflate(R.layout.add_route_dialog_common, secondRouteDialogContainer);
        firstRouteDialog.setVisibility(View.VISIBLE);

        TextView firstLineTv = (TextView) firstRouteDialog.findViewById(R.id.firstLineTV);
        TextView secondLineTv = (TextView) firstRouteDialog.findViewById(R.id.secondLineTV);

        firstLineTv.setText("Where do you want to transport it to");
        secondLineTv.setText("ADD CONSIGNEE ADDRESS");

        secondLineTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddressBook(1);
            }
        });
    }

    public void setVisiblityOfBottomBtns(int whichBtn){
        ViewGroup[] vgList = new ViewGroup[]{trucksAvailableTvParent, confirmRouteTvParent, showCheapestRoadTvParent};
        for(int i=0;i<vgList.length;i++){
            ViewGroup currVg = vgList[i];
            Log.e("Visibility",i + " " + ((TextView)currVg.getChildAt(0)).getText().toString());
            currVg.setVisibility(View.GONE);
            if(i== whichBtn){
                currVg.setVisibility(View.VISIBLE);
                Log.e("Visibility", "setting visibility " + i + " "+ whichBtn);

                if(i == 0){
                    String pickup_address = "[84]";
                    String dropoff_address = "[85]";
                    String booking_date = "19-09-2016";
                    //api call
                    available_TruckCount(Access_Token, pickup_address,dropoff_address,booking_date);
                }

            }
        }
    }

    private void setCheapestRouteBtn(){
        showCheapestRoadTvParent = (ViewGroup) currFragment.findViewById(R.id.showCheapestRoadTvParent);
        showCheapestRoadTv = (TextView) currFragment.findViewById(R.id.showCheapestRoadTv);
        showCheapestRoadTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCheapestRoute();
            }
        });
    }

    private void setTrucksAvailableBtn(){
        trucksAvailableTvParent = (ViewGroup) currFragment.findViewById(R.id.trucksAvailableTvParent);
        trucksAvailableTv = (TextView) currFragment.findViewById(R.id.trucksAvailableTv);

        trucksAvailableTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, Activity_Tuck12.class);
                startActivity(i);
            }
        });
    }

    public void showOriginalContainers(){
        AddressList.allowDropSwipe = false;
        addressSwapVg.setVisibility(View.GONE);
        pickUpContainer.removeAllViews();
       // dropContainer.removeAllViews();
        checkUserInputs();
        firstRouteDialogContainer.setVisibility(View.VISIBLE);
        secondRouteDialogContainer.setVisibility(View.VISIBLE);


    }

    private void setConfirmRouteBtn(){
        confirmRouteTvParent = (ViewGroup) currFragment.findViewById(R.id.confirmRouteTvParent);
        confirmRouteTv = (TextView) currFragment.findViewById(R.id.confirmRouteTv);
        confirmRouteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showOriginalContainers();

                setAlertDialog(1, "Reset Route", "YES", "CANCEL");
            }
        });
    }

    private void setCheapestRoutePickUpFragment(){
        cheapestRoutePickUpFragment = new AddressSwapPickUpFragment();
        pickUpContainer = (ViewGroup) currFragment.findViewById(R.id.content);
        pickUpContainer.removeAllViews();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(pickUpContainer.getId(), cheapestRoutePickUpFragment)
                .addToBackStack(null)
                .commit();
        pickUpContainer.setVisibility(View.VISIBLE);

        if(AddressList.pickUpList.size() == 1){
            activity.addressesFragment.toggleVisibilityOfpickUpOptionDialogTv(AddressList.SELECT_DIFFERENT_ROUTE, View.GONE);
            activity.addressesFragment.toggleVisibilityOfdropOptionDialogTv(AddressList.SHOW_CHEAPEST_ROUTE, View.VISIBLE);
        }else{
            activity.addressesFragment.toggleVisibilityOfpickUpOptionDialogTv(AddressList.SELECT_DIFFERENT_ROUTE, View.GONE);
        }

    }

    public void setVisibilityOfCheapestRouteDialogueVisibility(){
        Log.e("DialogTv", "setting visibility od dialogTv");
        if(AddressList.pickUpList.size() == 1){
            activity.addressesFragment.toggleVisibilityOfpickUpOptionDialogTv(AddressList.SHOW_CHEAPEST_ROUTE, View.GONE);
            activity.addressesFragment.toggleVisibilityOfdropOptionDialogTv(AddressList.SHOW_CHEAPEST_ROUTE, View.VISIBLE);

        }else{
            activity.addressesFragment.toggleVisibilityOfpickUpOptionDialogTv(AddressList.SHOW_CHEAPEST_ROUTE, View.GONE);
        }

        if(AddressList.dropList.size() == 1){
            activity.addressesFragment.toggleVisibilityOfdropOptionDialogTv(AddressList.SHOW_CHEAPEST_ROUTE, View.VISIBLE);
        }else{
            activity.addressesFragment.toggleVisibilityOfdropOptionDialogTv(AddressList.SHOW_CHEAPEST_ROUTE, View.GONE);
        }
    }

    private void setCheapestRouteDropUpFragment(){
        cheapestRoteDropUpFragment = new AddressSwapDropFragment();
        dropContainer = (ViewGroup) currFragment.findViewById(R.id.content2);
        dropContainer.removeAllViews();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(dropContainer.getId(), cheapestRoteDropUpFragment)
                .addToBackStack(null)
                .commit();

        if(AddressList.dropList.size() == 1){
            activity.addressesFragment.toggleVisibilityOfdropOptionDialogTv(AddressList.SELECT_DIFFERENT_ROUTE, View.VISIBLE);
        }else{
            activity.addressesFragment.toggleVisibilityOfdropOptionDialogTv(AddressList.SELECT_DIFFERENT_ROUTE, View.GONE);
        }

    }




    public void listPickUpUpdated(List<AddressObj> updatedList){
        /*for(int i=0;i<updatedList.size();i++){
            Log.e("items", ""+updatedList.get(i));
        }

        AddressList.pickUpList = new ArrayList<>();
        AddressList.pickUpList.addAll(updatedList);



       cheapestRoutePickUpFragment.updateList();
        //setVisiblityOfBottomBtns(0);
        setVisibilityOfCheapestRouteDialogueVisibility();*/

        for(int i=0;i<updatedList.size();i++){
            Log.e("items", ""+updatedList.get(i));
        }

        AddressList.cheapestRoutePickUpList = new ArrayList<>();
        AddressList.cheapestRoutePickUpList.addAll(updatedList);



       cheapestRoutePickUpFragment.updateList();
        //setVisiblityOfBottomBtns(0);
        setVisibilityOfCheapestRouteDialogueVisibility();

    }

    public void listDropUpUpdated(List<AddressObj> updatedList){
/*        for(int i=0;i<updatedList.size();i++){
            Log.e("items", ""+updatedList.get(i));
        }

        AddressList.dropList = new ArrayList<>();
        AddressList.dropList.addAll(updatedList);
        cheapestRoteDropUpFragment.updateList();
        setVisibilityOfCheapestRouteDialogueVisibility();*/

        for(int i=0;i<updatedList.size();i++){
            Log.e("items", ""+updatedList.get(i));
        }

        AddressList.cheapestRouteDropList = new ArrayList<>();
        AddressList.cheapestRouteDropList.addAll(updatedList);
        cheapestRoteDropUpFragment.updateList();
        setVisibilityOfCheapestRouteDialogueVisibility();





    }

    private void address_Book(final String access_Token, final String userId, final int whichType) {
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

                        Intent address_Book = new Intent(getActivity(), Activity_AddressBook.class);
                        address_Book.putExtra("message", bundle);
                        address_Book.putExtra("mode", 0);
                        address_Book.putExtra("type", whichType);
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
//Need to work

                        Intent address_page = new Intent(getActivity(), Activity_AddressPort.class);
                       /* address_page.putExtra("message", bundle);
                        address_page.putExtra("mode", 0);
                        address_page.putExtra("type", whichType);*/
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

    private void setTruckDetailsForTruckCountView(String trucksAvailable){
       TextView trucksAvailableTv = (TextView) trucksAvailableTvParent.findViewById(R.id.trucksAvailableTv);
        if(Integer.parseInt(trucksAvailable) < 10){
            trucksAvailable = "0"+ trucksAvailable;
        }

        String label = "TRUCKS";
        StringBuilder stringBuilder = new StringBuilder(trucksAvailable);
        stringBuilder.append(" ");
        stringBuilder.append(label);
        trucksAvailableTv.setText(stringBuilder.toString());

    }

    private void available_TruckCount(final String access_Token, final String pickup_address, final String dropoff_address, final String booking_date)
    {
        // Tag used to cancel the request
        String tag_string_req = "req_TruckCount";

       /* pDialog.setMessage("Logging in ...");
        showDialog();*/

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.URL_TRUCKCOUNT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Address Book Response: " + response.toString());
                //   hideDialog();

                try {

                    JSONObject jObj = new JSONObject(response);
                    //  boolean error = jObj.getBoolean("error");
                    String error = jObj.getString("message");
                    int status = jObj.getInt("status");
                    //   String data = jObj.optString("data");
                    JSONObject data = jObj.getJSONObject("data");
                    // Check for error node in json
                  /*  if (error!=null)*/
                    if ( status == 1)
                    {
                        try {
                            //    JSONArray Arraydata = new JSONArray();
                            String total_trucks_available = data.optString("total_trucks_available");
                            Log.e("@@Truck Count", total_trucks_available.toString());
                            setTruckDetailsForTruckCountView(total_trucks_available);


                        }
                        catch (Exception ex)
                        {
                            ex.toString();
                        }


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
                params.put("access_token",Access_Token );
                params.put("pickup_address",pickup_address);
                params.put("dropoff_address",dropoff_address);
                params.put("booking_date",booking_date);

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
