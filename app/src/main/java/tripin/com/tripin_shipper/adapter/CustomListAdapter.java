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

import tripin.com.tripin_shipper.R;
import tripin.com.tripin_shipper.activity.Activity_Address_page;
import tripin.com.tripin_shipper.model.Countries;
import tripin.com.tripin_shipper.volley.AppController;

/**
 * Created by SUMEET on 03-09-2016.
 */
public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Countries> movieItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public CustomListAdapter(Activity activity, List<Countries> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
    }
    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int position) {
        return movieItems.get(position);
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

        // getting movie data for the row
      Countries m = movieItems.get(position);


        // title
        title.setText((CharSequence) m.getName());

        Log.e("@adaper-name", String.valueOf(m.getName()));

// Listen for ListView Item Click
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single item click data to SingleItemView Class
                Intent intent = new Intent(activity, Activity_Address_page.class);
                // Pass all data rank
                intent.putExtra("state_name",(movieItems.get(position).getName()));
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("State", (movieItems.get(position).getName()));
                editor.commit();
                // Pass all data country
                intent.putExtra("state_id",(movieItems.get(position).getCode()));

                // Pass all data flag
                // Start SingleItemView Class
                activity.startActivity(intent);
            }
        });
        return convertView;
    }
}
