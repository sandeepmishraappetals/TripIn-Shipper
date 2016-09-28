package tripin.com.tripin_shipper.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import tripin.com.tripin_shipper.AppController;
import tripin.com.tripin_shipper.R;
import tripin.com.tripin_shipper.activity.Activity_Address_page;
import tripin.com.tripin_shipper.model.City;

/**
 * Created by SUMEET on 03-09-2016.
 */
public class CustomListAdapter_city extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<City> city;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public CustomListAdapter_city(Activity activity, List<City> city) {
        this.activity = activity;
        this.city = city;
    }

    @Override
    public int getCount() {
        return city.size();
    }

    @Override
    public Object getItem(int position) {
        return city.get(position);
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
            convertView = inflater.inflate(R.layout.list_item, null);

      /*  if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();*/

        TextView title = (TextView) convertView.findViewById(R.id.product_name);
        TextView rating = (TextView) convertView.findViewById(R.id.tv2);
        View rowView = (View) convertView.findViewById(R.id.rowView);

        // getting movie data for the row
      City m = city.get(position);


        // title
        title.setText((CharSequence) m.getCity_name());

        Log.e("@adaper-name", String.valueOf(m.getCity_name()) + " "+ getCount());
        if (position == 2)
        {
            rowView.setVisibility(View.VISIBLE);

        }


// Listen for ListView Item Click
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Send single item click data to SingleItemView Class
                Intent intent = new Intent(activity, Activity_Address_page.class);
                // Pass all data rank
                intent.putExtra("city_name",(city.get(position).getCity_name()));
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("city_name", (city.get(position).getCity_name()));
                editor.commit();
                // Pass all data country
                intent.putExtra("city_id",(city.get(position).getCity_id()));

                // Pass all data flag
                // Start SingleItemView Class
                activity.startActivity(intent);
            }
        });
        return convertView;
    }
}
