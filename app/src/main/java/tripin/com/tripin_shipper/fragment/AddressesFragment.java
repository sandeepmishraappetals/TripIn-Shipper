package tripin.com.tripin_shipper.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Map;

import tripin.com.tripin_shipper.R;
import tripin.com.tripin_shipper.activity.Activity_AddressBook;
import tripin.com.tripin_shipper.activity.Activity_Address_page;
import tripin.com.tripin_shipper.activity.Activity_Dashboard;
import tripin.com.tripin_shipper.model.AddressList;
import tripin.com.tripin_shipper.model.AddressObj;
import tripin.com.tripin_shipper.volley.AppController;
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
    private static final String TAG = AddressesFragment.class.getSimpleName();
    String UserId,Access_Token; private Bundle bundle;


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
        setVisiblityOfBottomBtns(2);
        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity());
        Access_Token=(mSharedPreference.getString("Token", null));

       // setAlertDialog("test", "yes", "no");
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
            TextView idTv = (TextView) pickUpRow.findViewById(R.id.idTv);
            TextView nameTv = (TextView) pickUpRow.findViewById(R.id.nameTv);
            TextView addressTv = (TextView) pickUpRow.findViewById(R.id.addressTv);
            TextView addTv = (TextView) pickUpRow.findViewById(R.id.addTv);
            container.addView(pickUpRow);
            idTv.setText("");
            nameTv.setText(addressObj.getName());
            addressTv.setText(addressObj.getAddress());

            addTv.setVisibility(View.GONE);

            if( pickUpList.size() < 3 && i >= pickUpList.size()-1 ){
                addTv.setVisibility(View.VISIBLE);
            }

            if(dropList.size() > 1 && pickUpList.size() == 1){
                addTv.setVisibility(View.GONE);
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
                    Log.e("deleting", "deleting pickup " +finalId  );
                    deleteEntry(0, finalId);
                    return false;
                }
            });
        }
    }

    public void setAlertDialog(String headingText, String positiveText, String negativeText){
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

            }
        });

        negativeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void addSelectedDropAddresses(){
       // secondRouteDialogContainer.removeAllViewsInLayout();
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
            TextView nameTv = (TextView) pickUpRow.findViewById(R.id.nameTv);
            TextView addressTv = (TextView) pickUpRow.findViewById(R.id.addressTv);
            TextView addTv = (TextView) pickUpRow.findViewById(R.id.addTv);
            container.addView(pickUpRow);
            idTv.setText("");
            nameTv.setText(addressObj.getName());
            addressTv.setText(addressObj.getAddress());

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
                    Log.e("deleting", "deleting drop " +finalId  );
                    deleteEntry(1, finalId);
                    return false;
                }
            });
        }
    }

    private void deleteEntry(int whichType, int listId){

        Log.e("dleting", ""+listId);
            if(whichType == 0){
                AddressList.pickUpList.remove(listId);
                listPickUpUpdated(AddressList.pickUpList);
            }else  if(whichType == 1){
                AddressList.dropList.remove(listId);
                listDropUpUpdated(AddressList.dropList);
            }
    }
    private void showCheapestRoute(){
        setRouteChangeFunc();
    }

    private void setRouteChangeFunc(){
        firstRouteDialogContainer.setVisibility(View.GONE);
        secondRouteDialogContainer.setVisibility(View.GONE);
        addressSwapVg = (ViewGroup) currFragment.findViewById(R.id.addressSwapVg);
        addressSwapVg.setVisibility(View.VISIBLE);
        setCheapestRoutePickUpFragment();
        setCheapestRouteDropUpFragment();
        setVisiblityOfBottomBtns(0);
        swipeIndicationVg.setVisibility(View.VISIBLE);
    }

    private void setInstances(){
        activity = (Activity_Dashboard) getActivity();
        addressesAlert = (ViewGroup) currFragment.findViewById(R.id.common_alert);
        swipeIndicationVg = (ViewGroup) currFragment.findViewById(R.id.swipeIndicationVg);
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
    }

    public void checkUserInputs(){
        ArrayList<AddressObj> pickUpList = AddressList.pickUpList;
        ArrayList<AddressObj> dropList = AddressList.dropList;
        Log.e("AddressedList", ""+AddressList.pickUpList.size() + " " + AddressList.dropList.size());
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

        if(whichType == 0){
            if(dropList.size() == 0 && pickUpList.size() > 0){
                Toast.makeText(activity, "Please add Consignee first", Toast.LENGTH_LONG).show();
            }else{
               /* Intent intent  = new Intent(activity, Activity_AddressBook.class);
                intent.putExtra("type", whichType);
                startActivity(intent);*/
                final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity());
                UserId=(mSharedPreference.getString("UserId", null));
                address_Book(Access_Token, UserId, whichType);
            }
        }else if(whichType == 1){
              if(pickUpList.size() == 0 && dropList.size() > 0){
                Toast.makeText(activity, "Please add Consigner first", Toast.LENGTH_LONG).show();
            }else{
                  /*Intent intent  = new Intent(activity, Activity_AddressBook.class);
                  intent.putExtra("type", whichType);
                  startActivity(intent);*/

                  final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity());
                  UserId=(mSharedPreference.getString("UserId", null));
                  address_Book(Access_Token, UserId, whichType);
              }
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

            }
        });
    }

    public void showOriginalContainers(){
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
                showOriginalContainers();
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
    }

    private void setCheapestRouteDropUpFragment(){
        cheapestRoteDropUpFragment = new AddressSwapDropFragment();
        dropContainer = (ViewGroup) currFragment.findViewById(R.id.content2);
        dropContainer.removeAllViews();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(dropContainer.getId(), cheapestRoteDropUpFragment)
                .addToBackStack(null)
                .commit();
    }

    public void listPickUpUpdated(List<AddressObj> updatedList){
        for(int i=0;i<updatedList.size();i++){
            Log.e("items", ""+updatedList.get(i));
        }

        AddressList.pickUpList = new ArrayList<>();
        AddressList.pickUpList.addAll(updatedList);

       cheapestRoutePickUpFragment.updateList();

       // setVisiblityOfBottomBtns(0);

    }

    public void listDropUpUpdated(List<AddressObj> updatedList){
        for(int i=0;i<updatedList.size();i++){
            Log.e("items", ""+updatedList.get(i));
        }

        AddressList.dropList = new ArrayList<>();
        AddressList.dropList.addAll(updatedList);
        cheapestRoteDropUpFragment.updateList();

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

                        Intent address_page = new Intent(getActivity(), Activity_Address_page.class);
                        address_page.putExtra("message", bundle);
                        address_page.putExtra("mode", 0);
                        address_page.putExtra("type", whichType);
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
