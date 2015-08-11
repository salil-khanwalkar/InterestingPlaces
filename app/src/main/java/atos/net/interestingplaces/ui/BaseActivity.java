package atos.net.interestingplaces.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.octo.android.robospice.Jackson2SpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

import java.util.ArrayList;
import java.util.List;

import atos.net.interestingplaces.R;
import atos.net.interestingplaces.dto.POIList;
import atos.net.interestingplaces.helper.POIHelper;
import atos.net.interestingplaces.pojo.PlaceOfInterest;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String       TAG           = BaseActivity.class.getSimpleName();
    private SpiceManager mSpiceManager = new SpiceManager(Jackson2SpringAndroidSpiceService.class);
    private MenuItem mProgressMenuItem;

    protected static final String NULL_STRING = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }


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
     * Show the progress bar
     */
    protected void showProgress(){
        if(null != mProgressMenuItem) {
            mProgressMenuItem.setActionView(R.layout.action_view_progress);
            mProgressMenuItem.setVisible(true);
        }
    }

    /**
     * Hide the progress bar
     */
    protected void hideProgress(){
        if(null != mProgressMenuItem) {
            mProgressMenuItem.setActionView(null);
            mProgressMenuItem.setVisible(false);
        }
    }

    /**
     * Method to read all records from DB.
     * @return Returns list of {@link PlaceOfInterest} objects.
     */
    protected List<PlaceOfInterest> readAll(){
        ArrayList<PlaceOfInterest> list = (ArrayList<PlaceOfInterest>) POIHelper.readAll(this);
        return list;
    }

    /**
     * Inserts (@link PlaceOfInterest} records in the database.
     * @param poiList
     */
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
     * Update the {@link PlaceOfInterest} object in the database.
     * @param placeOfInterest
     */
    protected void update(final PlaceOfInterest placeOfInterest){
        POIHelper.update(this,placeOfInterest);
    }

    /**
     * Compares a string to 'null'. The webservice returns 'null' if
     * no data is available with the value of a json key-value pair
     * @param stringToCompare String to compare with
     * @return true if the string is 'null' , false otherwise.
     */
    protected boolean isNullString(final String stringToCompare){
        return stringToCompare.equalsIgnoreCase(NULL_STRING);
    }

    /**
     * Returns the SpiceManager instance.
     * {@link SpiceManager}
     * @return
     */
    protected SpiceManager getSpiceManager() {
        return mSpiceManager;
    }

    /**
     * Sets the MenuItem for the progressbar to be shown in the action bar.
     * @param progressMenuItem
     */
    public void setProgressMenuItem(final MenuItem progressMenuItem) {
        mProgressMenuItem = progressMenuItem;
    }

}
