package tripin.com.tripin_shipper.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import tripin.com.tripin_shipper.R;

/**
 * Created by Android on 26/08/16.
 */
public class Activity_Search1 extends Activity {

    ArrayAdapter<String> dataAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search1);

        //Generate list View from ArrayList
        displayListView();

    }

    private void displayListView() {

        //Array list of countries
        List<String> countryList = new ArrayList<String>();
        countryList.add("Maharashtra");
        countryList.add("Andhrapradesh");
        countryList.add("Goa");
        countryList.add("Gujrath");
        countryList.add("Tamilnadu");
     /*   countryList.add("Belize");
        countryList.add("Bermuda");
        countryList.add("Barbados");
        countryList.add("Canada");
        countryList.add("Costa Rica");
        countryList.add("Cuba");
        countryList.add("Cayman Islands");
        countryList.add("Dominica");
        countryList.add("Dominican Republic");
        countryList.add("Guadeloupe");
        countryList.add("Grenada");
        countryList.add("Greenland");
        countryList.add("Guatemala");
        countryList.add("Honduras");
        countryList.add("Haiti");
        countryList.add("Jamaica");*/

        //create an ArrayAdaptar from the String Array
        dataAdapter = new ArrayAdapter<String>(this,
                R.layout.country_list, countryList);
        /*ListView listView = (ListView) findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        //enables filtering for the contents of the given ListView
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Toast.makeText(getApplicationContext(),
                        ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Activity_Search1.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("state", (String) ((TextView) view).getText());
                editor.commit();
                Log.e("@position", (String) ((TextView) view).getText());
            }
        });

        EditText myFilter = (EditText) findViewById(R.id.myFilter);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });*/
    }
}