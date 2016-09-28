package tripin.com.tripin_shipper.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import tripin.com.tripin_shipper.R;
import tripin.com.tripin_shipper.activity.Activity_AddressBook;
import tripin.com.tripin_shipper.model.AddressList;
import tripin.com.tripin_shipper.model.AddressObj;

/**
 * Created by SANJEEV on 11-09-2016.
 */
public class AddressBookAdapter extends BaseAdapter implements SearchView.OnCloseListener{

    private Activity_AddressBook activity;
    private static LayoutInflater inflater=null;
    private ArrayList<AddressObj> data;


    public AddressBookAdapter(Activity a, ArrayList<AddressObj> d) {
        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )a.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.activity = (Activity_AddressBook) a;
        this.data = d;

    }

    @Override
    public int getCount() {
        if(data.size()<=0)
        return 1;
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public boolean onClose() {
        return false;
    }

    public static class ViewHolder{
        public View itemView;
        public TextView idTv;
        public TextView nameTv;
        public TextView addressTv;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.single_pick_up_drop_swap_item, null);
            /****** View Holder Object to contain tabitem.xml file elements ******/


            holder = new ViewHolder();

            Log.e("holder", "initialised "+holder);

            //holder.idTv = (TextView) vi.findViewById(R.id.idTv);
            holder.itemView = vi;
            holder.nameTv = (TextView) vi.findViewById(R.id.text);

            Log.e("pickup", ""+position );
            for(int i = 0; i< AddressList.pickUpList.size(); i++){
                String currAddress   = data.get(position).getAddress();
                String address = AddressList.pickUpList.get(i).getAddress();
                int addressID = AddressList.pickUpList.get(i).getId();

                Log.e("pickup", ""+currAddress + " " +address + " "+ addressID);
                if(currAddress.equals(address) ){
                    holder.itemView.setAlpha(AddressList.diabledAlphaValue);
                }
            }

            for(int i = 0; i< AddressList.dropList.size(); i++){
                String currAddress = data.get(position).getAddress();
                String address = AddressList.dropList.get(i).getAddress();

                Log.e("drop", ""+currAddress + " " +address );

                if(currAddress.equals(address)){
                    holder.itemView.setAlpha(AddressList.diabledAlphaValue);
                }
            }

        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        Log.e("holder", ""+holder);
        holder.nameTv.setText(data.get(position).getName());

        final int itemPosition     = position;

        // ListView Clicked item value
       //s String  itemValue    = (String) listView.getItemAtPosition(position);

        // Show Alert


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.itemView.getAlpha() > AddressList.diabledAlphaValue){
                    AddressObj addressSelected = data.get(itemPosition);
                    // boolean allow = activity.allowAddressAddition(addressSelected);

                    // if(allow){
                    if(activity.SELECTED_TYPE == activity.PICKUP){
                        AddressList.pickUpList.add(addressSelected);
                    }else  if(activity.SELECTED_TYPE == activity.DROP){
                        AddressList.dropList.add(addressSelected);
                    }

                    Toast.makeText(activity.getApplicationContext(),
                            "Position :"+itemPosition+"  ListItem : " +data.get(itemPosition).getName() , Toast.LENGTH_LONG)
                            .show();
                    activity.sendResultToCallingActivity(new AddressObj(0, "", "", "", ""));
               /* }else{
                    Toast.makeText(activity.getApplicationContext(),
                           "Selcted Address alreeady added in list " , Toast.LENGTH_LONG)
                            .show();
                }*/

                }


            }
        });


               return vi;

    }

}
