package atos.net.interestingplaces.ui;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
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

    protected float calculateDistance(Location from,Location to){
        float distance = 0;
        return distance;
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
}
