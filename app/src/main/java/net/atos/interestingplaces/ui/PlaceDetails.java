package net.atos.interestingplaces.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import net.atos.interestingplaces.R;
import net.atos.interestingplaces.pojo.PlaceOfInterest;
import net.atos.interestingplaces.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
        return true;
    }

    /**
     * Creates an intent for the ShareActionProvider
     * @return
     */
    private Intent createShareIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String address = "";
        try {
            address = URLEncoder.encode(mPlaceOfInterest.getAddress(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String temp = new StringBuilder().
                append(mPlaceOfInterest.getTitle()).
                append(" : ").
                append("http://maps.google.com/maps?").
                append("saddr=").
                append(address).
                toString();
       /* shareIntent.putExtra(Intent.EXTRA_TEXT,
                "Visit : " + mPlaceOfInterest.getTitle() + "at Address : " + mPlaceOfInterest.getAddress());*/
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                temp);
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share_via)));
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
            case R.id.action_share:
                mShareActionProvider.setShareIntent(createShareIntent());
                return true;
            case R.id.action_call:
                Utils.launchCall(PlaceDetails.this, mPlaceOfInterest.getPhone());
                return true;
            case R.id.action_email:
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
        String value ;
        setTitle(mPlaceOfInterest.getTitle());
        /**
         * Assuming that the title will never be null.
         */
        mTitle.setText(mPlaceOfInterest.getTitle());

        value = mPlaceOfInterest.getTransport();
        if(isNullString(value)){
            value = getString(R.string.transport_not_available);
        }
        mTransport.setText(value);

        value = mPlaceOfInterest.getAddress();
        if(isNullString(value)){
            value = getString(R.string.address_not_available);
        }
        mAddress.setText(value);

        value = mPlaceOfInterest.getDescription();
        if(isNullString(value)){
            value = getString(R.string.description_not_available);
        }
        mDescription.setText(value);
        mToolbar.setTitle(getString(R.string.place_details_toolbar_title));
    }
}
