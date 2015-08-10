package atos.net.interestingplaces.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import atos.net.interestingplaces.R;
import atos.net.interestingplaces.pojo.PlaceOfInterest;

public class PlaceDetails extends BaseActivity {

    private static final String TAG = PlaceDetails.class.getSimpleName();

    private TextView mTitle;
    private TextView mAddress;
    private TextView mTransport;
    private TextView mDescription;
    private PlaceOfInterest mPlaceOfInterest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        init();
        if(null != savedInstanceState){
            if(savedInstanceState.containsKey("PlaceDetails")){
                mPlaceOfInterest = (PlaceOfInterest) savedInstanceState.getSerializable("PlaceDetails");
            }
        }else {
            mPlaceOfInterest = (PlaceOfInterest) getIntent().
                    getSerializableExtra("PlaceDetails");
        }
        update(mPlaceOfInterest);
    }

    /**
     * Save all appropriate fragment state.
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        outState.putSerializable("PlaceDetails",mPlaceOfInterest);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_place_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialize the UI components
     */
    private void init(){
        mTitle = (TextView) findViewById(R.id.tv_place_title);
        mAddress = (TextView) findViewById(R.id.tv_place_address);
        mTransport = (TextView) findViewById(R.id.tv_place_transport);
        mDescription = (TextView) findViewById(R.id.tv_place_description);
    }

    /**
     * Update the UI
     * @param placeOfInterest object to use to update the UI.
     */
    private void update(PlaceOfInterest placeOfInterest){
        String value = "Not available";
        setTitle(placeOfInterest.getTitle());
        /**
         * Assuming that the title will never be null.
         */
        mTitle.setText(placeOfInterest.getTitle());

        value = placeOfInterest.getTransport();
        if(isNullString(value)){
            value = "Transport not available";
        }
        mTransport.setText(value);

        value = placeOfInterest.getAddress();
        if(isNullString(value)){
            value = "Address not available";
        }
        mAddress.setText(value);

        value = placeOfInterest.getDescription();
        if(isNullString(value)){
            value = "Description not available";
        }
        mDescription.setText(value);
    }
}
