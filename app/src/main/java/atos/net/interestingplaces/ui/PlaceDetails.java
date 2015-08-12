package atos.net.interestingplaces.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
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
    private ShareActionProvider mShareActionProvider;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        init();
        if (null != savedInstanceState) {
            if (savedInstanceState.containsKey("PlaceDetails")) {
                mPlaceOfInterest = (PlaceOfInterest) savedInstanceState.getSerializable("PlaceDetails");
            }
        }
        else {
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
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        mShareActionProvider.setShareIntent(createShareIntent());
        return true;
    }

    /**
     * Creates an intent for the ShareActionProvider
     * @return
     */
    private Intent createShareIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "Visit : " + mPlaceOfInterest.getTitle() + "at Address : " + mPlaceOfInterest.getAddress());
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            /*case R.id.action_locate:
                Log.d(TAG, "Locate");
                // Uri gmmIntentUri = Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia");
                try {
                    String scheme = "google.navigation:q=";
                    String address = URLEncoder.encode(mPlaceOfInterest.getAddress(), "UTF-8");
                    String mode = "&mode=d";
                    Utils.launchMap(PlaceDetails.this, Uri.parse(scheme + address + mode));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return true;*/
            case R.id.action_call:
                Log.d(TAG, "Call");
                Utils.launchCall(PlaceDetails.this, mPlaceOfInterest.getPhone());
                return true;
            case R.id.action_email:
                Log.d(TAG, "Email");
                Utils.launchEmail(PlaceDetails.this, mPlaceOfInterest.getEmail(),
                        mPlaceOfInterest.getTitle(), "");
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialize the UI components
     */
    private void init() {
        mTitle = (TextView) findViewById(R.id.tv_place_title);
        mAddress = (TextView) findViewById(R.id.tv_place_address);
        mTransport = (TextView) findViewById(R.id.tv_place_transport);
        mDescription = (TextView) findViewById(R.id.tv_place_description);

        mToolbar = (Toolbar) findViewById(R.id.tb_nav_mode);
        mToolbar.inflateMenu(R.menu.menu_place_details_toolbar);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            /**
             * This method will be invoked when a menu item is clicked if the item itself did
             * not already handle the event.
             *
             * @param item {@link MenuItem} that was clicked
             *
             * @return <code>true</code> if the event was handled, <code>false</code> otherwise.
             */
            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                int id = item.getItemId();
                String scheme = "google.navigation:q=";
                String address = null;
                String mode = "&mode=";
                try {
                    address = URLEncoder.encode(mPlaceOfInterest.getAddress(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                switch (id){
                    case R.id.action_drive_car:
                        /**
                         * Launch the map with the directions for driving.
                         * Assuming that the driving directions are for a car.
                         */
                        Utils.launchMap(PlaceDetails.this, Uri.parse(scheme + address + mode+"d"));
                        return true;
                    case R.id.action_walk:
                        /**
                         * Launch the map with the directions for walking.
                         */
                        Utils.launchMap(PlaceDetails.this, Uri.parse(scheme + address + mode+"w"));
                        return true;
                    case R.id.action_bike:
                        /**
                         * Launch the map with the directions for a bike.
                         */
                        Utils.launchMap(PlaceDetails.this, Uri.parse(scheme + address + mode+"b"));
                        return true;
                }

                return false;
            }
        });
    }

    /**
     * Update the UI
     */
    private void updateView() {
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
        mToolbar.setTitle("Directions ");
    }
}
