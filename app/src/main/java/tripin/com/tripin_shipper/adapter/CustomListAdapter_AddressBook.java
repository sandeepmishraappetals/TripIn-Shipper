package tripin.com.tripin_shipper.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import tripin.com.tripin_shipper.R;
import tripin.com.tripin_shipper.activity.Activity_AddressBook;
import tripin.com.tripin_shipper.activity.Activity_Address_page;
import tripin.com.tripin_shipper.model.AddressBook;
import tripin.com.tripin_shipper.model.AddressList;
import tripin.com.tripin_shipper.model.AddressObj;
import tripin.com.tripin_shipper.AppController;

/**
 * Created by Android on 08/09/16.
 */
public class CustomListAdapter_AddressBook extends BaseAdapter {
    private Activity_AddressBook activity;
    private LayoutInflater inflater;
    private List<AddressBook> addressBook;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private String whichList;
    public CustomListAdapter_AddressBook(Activity activity, List<AddressBook> addressBook) {
        this.activity = (Activity_AddressBook) activity;
        this.addressBook = addressBook;
    }
    @Override
    public int getCount() {
        return addressBook.size();
    }

    @Override
    public Object getItem(int position) {
        Log.e("currentItem", ""+position +  " " + addressBook.get(position).getAddress());
        return addressBook.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_item_addressbook, null);

      /*  if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();*/

        TextView title = (TextView) convertView.findViewById(R.id.firm_name);
        final TextView address = (TextView) convertView.findViewById(R.id.address);
        ImageButton edit_bt = (ImageButton) convertView.findViewById(R.id.edit_bt);

        // getting movie data for the row
        final AddressBook m = addressBook.get(position);


        // title
        title.setText((CharSequence) m.getFirm_name()+","+m.getCity());
        address.setText(m.getAddress()+","+m.getSurvey_no()+","+m.getPincode());

        final View finalConvertView = convertView;
        edit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  if ((m.getAddress_id().trim().contains("0"))) {

                    Log.e("@@ address_adapter", m.getAddress_id());
                }

                else {*/
                    // Send single item click data to SingleItemView Class
                    Intent intent = new Intent(activity, Activity_Address_page.class);
                    // Pass all data rank
                    /*intent.putExtra("Firm_name",(addressBook.get(position).getFirm_name()));
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("address", (addressBook.get(position).getAddress()));
                    editor.commit();
                    // Pass all data country
                    intent.putExtra("address_id",(addressBook.get(position).getAddress_id()));*/



                    Bundle bundle = new Bundle();
                    bundle.putSerializable("obj", addressBook.get(position));

                    intent.putExtra("mode", 1);
                    intent.putExtra("addressBundle", bundle);
                intent.putExtra("AddressId",addressBook.get(position).getAddress_id());
                Log.e("AddressId", addressBook.get(position).getAddress_id());

                    // Pass all data flag
                    // Start SingleItemView Class

                    activity.startActivity(intent);
             //   }
                Log.e("Edit click", "click");
            }
        });

        Log.e("@address_adaper-name", String.valueOf(m.getFirm_name()) + " "+ getCount());

// Listen for ListView Item Click
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

           /*    if ((m.getAddress_id().trim().contains("0"))) {

                    Log.e("@@ address_adapter", m.getAddress_id());
                }

                else {
                    // Send single item click data to SingleItemView Class
                    Intent intent = new Intent(activity, Activity_Address_page.class);
                    // Pass all data rank
                    intent.putExtra("Firm_name",(addressBook.get(position).getFirm_name()));
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("address", (addressBook.get(position).getAddress()));
                    editor.commit();
                    // Pass all data country
                    intent.putExtra("address_id",(addressBook.get(position).getAddress_id()));

                    // Pass all data flag
                    // Start SingleItemView Class
                    activity.startActivity(intent);
                }*/
                // Send single item click data to SingleItemView Class

                int id = Integer.parseInt(addressBook.get(position).getAddress_id());
                String name = addressBook.get(position).getFirm_name();
                String address = addressBook.get(position).getAddress();
                /*String lat = "1.000";
                String lon = "1.000";*/
                String lat = addressBook.get(position).getLat();
                String lon = addressBook.get(position).getLng();
                AddressObj addressSelected = new AddressObj(id, name, address, lat, lon );
                Log.e("addBook_Adapter", lat+lon);
                // boolean allow = activity.allowAddressAddition(addressSelected);

                if(finalConvertView.getAlpha() >= 1.0){
                    // if(allow){
                    if(activity.SELECTED_TYPE == activity.PICKUP){
                        AddressList.pickUpList.add(addressSelected);
                    }else  if(activity.SELECTED_TYPE == activity.DROP){
                        AddressList.dropList.add(addressSelected);
                    }



                    activity.sendResultToCallingActivity(addressSelected);
               }else{
                    Toast.makeText(activity, "Address already selected in "+whichList, Toast.LENGTH_LONG).show();
                }

                Log.e("convertView", "alpha is " + finalConvertView.getAlpha());

            }
        });



        Log.e("pickup", ""+position );
        for(int i = 0; i< AddressList.pickUpList.size(); i++){
            String currFirmName = addressBook.get(position).getFirm_name();
            String listFirmName = AddressList.pickUpList.get(i).getName();
            String currAddress   = addressBook.get(position).getAddress();
            String listAddress = AddressList.pickUpList.get(i).getAddress();

            Log.e("pickup", ""+currAddress + "-------- " +listAddress );
            if(currFirmName.equals(listFirmName) && currAddress.equals(listAddress) ){
                Log.e("pickup", "matched " +position);
                finalConvertView.setAlpha(AddressList.diabledAlphaValue);
                whichList = "Pick Up List";
            }
        }

        for(int i = 0; i< AddressList.dropList.size(); i++){
            String currFirmName = addressBook.get(position).getFirm_name();
            String listFirmName = AddressList.dropList.get(i).getName();
            String currAddress   = addressBook.get(position).getAddress();
            String listAddress = AddressList.dropList.get(i).getAddress();

            Log.e("pickup", ""+currAddress + "-------- " +listAddress );
            if(currFirmName.equals(listFirmName) && currAddress.equals(listAddress) ){
                Log.e("pickup", "matched " +position);
                finalConvertView.setAlpha(AddressList.diabledAlphaValue);
                whichList = "Drop List";
            }
        }


        //title.setText(""+position);



        return convertView;
    }

    private class ViewHolder{

        int position;

    }
}


