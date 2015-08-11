package atos.net.interestingplaces.ui;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import atos.net.interestingplaces.R;
import atos.net.interestingplaces.Utils;
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
        updateView();
    }

    /**
     * Save all appropriate fragment state.
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        outState.putSerializable("PlaceDetails", mPlaceOfInterest);
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
        switch (id){
            case R.id.action_locate:
                Log.d(TAG,"Locate");
                // Uri gmmIntentUri = Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia");
                try {
                    String scheme = "google.navigation:q=";
                    String address = URLEncoder.encode(mPlaceOfInterest.getAddress(),"UTF-8");
                    String mode = "&mode=d";
                    Utils.launchMap(PlaceDetails.this,Uri.parse(scheme+address+mode));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.action_call:
                Log.d(TAG, "Call");
                Utils.launchCall(PlaceDetails.this,mPlaceOfInterest.getPhone());
                return true;
            case R.id.action_email:
                Log.d(TAG,"Email");
                Utils.launchEmail(PlaceDetails.this,mPlaceOfInterest.getEmail(),
                        mPlaceOfInterest.getTitle(), "");
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
     */
    private void updateView(){
        String value = "Not available";
        setTitle(mPlaceOfInterest.getTitle());
        /**
         * Assuming that the title will never be null.
         */
        mTitle.setText(mPlaceOfInterest.getTitle());

        value = mPlaceOfInterest.getTransport();
        if(isNullString(value)){
            value = "Transport not available";
        }
        mTransport.setText(value);

        value = mPlaceOfInterest.getAddress();
        if(isNullString(value)){
            value = "Address not available";
        }
        mAddress.setText(value);

        value = mPlaceOfInterest.getDescription();
        if(isNullString(value)){
            value = "Description not available";
        }
        mDescription.setText(value);
    }
}
