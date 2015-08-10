package atos.net.interestingplaces.ui;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.octo.android.robospice.Jackson2SpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

import java.util.ArrayList;
import java.util.List;

import atos.net.interestingplaces.R;
import atos.net.interestingplaces.dto.POIList;
import atos.net.interestingplaces.helper.POIHelper;
import atos.net.interestingplaces.pojo.PlaceOfInterest;

public class BaseActivity extends ActionBarActivity {

    private static final String       TAG           = BaseActivity.class.getSimpleName();
    protected            SpiceManager mSpiceManager = new SpiceManager(Jackson2SpringAndroidSpiceService.class);
    private ProgressBar mProgressBar;
    private MenuItem mProgressMenuItem;

    protected static final String NULL_STRING = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        init();
    }


    /**
     * Prepare the Screen's standard options menu to be displayed.  This is
     * called right before the menu is shown, every time it is shown.  You can
     * use this method to efficiently enable/disable items or otherwise
     * dynamically modify the contents.
     * <p/>
     * <p>The default implementation updates the system menu items based on the
     * activity's state.  Deriving classes should always call through to the
     * base class implementation.
     *
     * @param menu The options menu as last shown or first initialized by
     *             onCreateOptionsMenu().
     *
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     *
     * @see #onCreateOptionsMenu
     */
    /*@Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base, menu);
        mProgressMenuItem = menu.findItem(R.id.action_progress);
        ProgressBar pb = (ProgressBar) MenuItemCompat.getActionView(mProgressMenuItem);
        pb.setVisibility(View.VISIBLE);
        return super.onPrepareOptionsMenu(menu);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        mProgressMenuItem = menu.findItem(R.id.action_progress);
        ProgressBar pb = (ProgressBar) MenuItemCompat.getActionView(mProgressMenuItem);
        return true;
    }

        /*
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
    }*/

    /**
     * Dispatch onStart() to all fragments.  Ensure any created loaders are
     * now started.
     */
    @Override
    protected void onStart() {
        super.onStart();
        mSpiceManager.start(this);
    }

    @Override
    protected void onStop() {
        mSpiceManager.shouldStop();
        super.onStop();
    }

    /**
     * Initialize the UI components.
     */
    private void init(){
//        mProgressBar = (ProgressBar) findViewById(R.id.pb_progress);
    }

    /**
     * Show the progress bar
     */
    protected void showProgress(){
//        mProgressBar.setVisibility(View.VISIBLE);
        if(null != mProgressMenuItem) {
            mProgressMenuItem.setActionView(R.layout.action_view_progress);
            mProgressMenuItem.setVisible(true);
        }
    }

    /**
     * Hide the progress bar
     */
    protected void hideProgress(){
//        mProgressBar.setVisibility(View.GONE);
        if(null != mProgressMenuItem) {
            mProgressMenuItem.setActionView(null);
            mProgressMenuItem.setVisible(false);
        }
    }

    /**
     * Method to read all records from DB.
     * @return Returns list of @see{PlaceOfInterest} objects.
     */
    protected List<PlaceOfInterest> readAll(){
        ArrayList<PlaceOfInterest> list = (ArrayList<PlaceOfInterest>) POIHelper.readAll(this);
        return list;
    }

    protected void insert(final POIList poiList){
        /**
         * Insert the records in DB
         */
        long id = 0;
        List<PlaceOfInterest> placeOfInterests = poiList.getList();
        for(PlaceOfInterest placeOfInterest : placeOfInterests){
            /**
             * Assuming that we haven't fetched the entire record so setting the record's
             * data level to partial
             */
            placeOfInterest.setLevel(PlaceOfInterest.RECORD_LEVEL_PARTIAL);
            id = POIHelper.insert(this,placeOfInterest);
            Log.d(TAG, "Inserted " + id + " Records");
        }
    }

    /**
     * Compares a string to 'null'. The webservice returns 'null' if
     * no data is available with the value of a json key-value pair
     * @param stringToCompare
     * @return {@see String#equalsIgnoreCase}
     */
    protected boolean isNullString(final String stringToCompare){
        return stringToCompare.equalsIgnoreCase(NULL_STRING);
    }
}
