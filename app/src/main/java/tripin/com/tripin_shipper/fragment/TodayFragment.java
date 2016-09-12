package tripin.com.tripin_shipper.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import tripin.com.tripin_shipper.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TodayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayFragment extends Fragment implements OnFragmentInteractionListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    // TODO: Externalize string-array
    String wheelMenu1[] = new String[]{"Country 1", "Country 2", "Country 3", "Country 4", "Country 5"};
    String wheelMenu2[] = new String[]{"state 1", "state 2", "state 3"};
    String wheelMenu3[] = new String[]{"Ahmedabad", "Surat","Pune","Bangalore","Mumbai","Delhi"};
    NumberPicker np,np1,np2;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v= inflater.inflate(R.layout.fragment_today, container, false);

//Get the widgets reference from XML layout
        final TextView tv = (TextView) v.findViewById(R.id.tv);
        np = (NumberPicker) v.findViewById(R.id.np);
        np1 = (NumberPicker) v.findViewById(R.id.np1);
        np2 = (NumberPicker) v.findViewById(R.id.np2);

        //Set TextView text color
        tv.setTextColor(Color.parseColor("#FF2C834F"));

        //Initializing a new string array with elements

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("hello");
        stringBuilder.append("hi");
        stringBuilder.toString();

        final String[] values= {stringBuilder.toString(),"Green", "Blue", "Yellow", "Magenta"};
        final String[] values1= {"A","B", "C", "D", "E"};
        final String[] values2= {"Red","Green", "Blue", "Yellow", "Magenta"};

        //Populate NumberPicker values from String array values
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
        np2.setWrapSelectorWheel(true);

        //Set a value change listener for NumberPicker
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected value from picker
                tv.setText("Selected value : " + values[newVal]);
                Log.e("Picker1", "Selected value :" + values[newVal]);
            }
        });
        //Set a value change listener for NumberPicker
        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected value from picker
                Log.e("Picker1", "Selected value :" + values1[newVal]);
            }
        });
        //Set a value change listener for NumberPicker
        np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected value from picker
                Log.e("Picker2", "Selected value :" + values2[newVal]);
            }
        });


        return v;
    }

    @Override
    public void onTodayFragmentInteraction(String string) {

    }

    @Override
    public void TomorrowFragmentInteraction() {

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
   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
    public interface OnFragmentInteractionListener {
        public void onTodayFragmentInteraction(String string);

        void onTomorrowFragmentInteraction(String string);
    }

}
