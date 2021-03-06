package net.atos.interestingplaces.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.octo.android.robospice.Jackson2SpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

import net.atos.interestingplaces.R;
import net.atos.interestingplaces.dto.POIList;
import net.atos.interestingplaces.helper.POIHelper;
import net.atos.interestingplaces.pojo.PlaceOfInterest;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String       TAG           = BaseActivity.class.getSimpleName();
    private final SpiceManager mSpiceManager = new SpiceManager(Jackson2SpringAndroidSpiceService.class);
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
     * @param poiList Object which contains the list of POIs to be
     *                inserted in the database.
     */
    protected void insert(final POIList poiList){
        /**
         * Insert the records in DB
         */
        List<PlaceOfInterest> placeOfInterests = poiList.getList();
        // TODO : Bulk insert instead of one by one.
        for(PlaceOfInterest placeOfInterest : placeOfInterests){
            /**
             * Assuming that we haven't fetched the entire record so setting the record's
             * data level to partial
             */
            placeOfInterest.setLevel(PlaceOfInterest.RECORD_LEVEL_PARTIAL);
            POIHelper.insert(this,placeOfInterest);
        }
    }

    /**
     * Update the {@link PlaceOfInterest} object in the database.
     * @param placeOfInterest {@link PlaceOfInterest} to be updated.
     */
    protected void update(final PlaceOfInterest placeOfInterest){
        POIHelper.update(this,placeOfInterest);
    }

    /**
     * Deletes all records in the database
     */
    protected void deleteAll(){
        POIHelper.deleteAll(this);
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
     * @return Returns an instance of {@link SpiceManager}
     */
    protected SpiceManager getSpiceManager() {
        return mSpiceManager;
    }

    /**
     * Sets the MenuItem for the progressbar to be shown in the action bar.
     * @param progressMenuItem Sets the {@link MenuItem} as a progress bar.
     */
    public void setProgressMenuItem(final MenuItem progressMenuItem) {
        mProgressMenuItem = progressMenuItem;
    }



}
