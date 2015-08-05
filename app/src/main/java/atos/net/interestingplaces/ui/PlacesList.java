package atos.net.interestingplaces.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

import atos.net.interestingplaces.PoiDetailsRequest;
import atos.net.interestingplaces.PoiListRequest;
import atos.net.interestingplaces.R;
import atos.net.interestingplaces.adapters.POIListAdapter;
import atos.net.interestingplaces.dto.POIList;
import atos.net.interestingplaces.helper.POIHelper;
import atos.net.interestingplaces.pojo.PlaceOfInterest;

public class PlacesList extends BaseActivity {

    private static final String TAG = PlacesList.class.getSimpleName();
    private ListView mListView;
    private POIListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);
        init();
        getPOIList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_places_list, menu);
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
     * Get the POI list from the server.
     */
    private void performPOIListRequest(){
        PoiListRequest request = new PoiListRequest();
        mSpiceManager.execute(request, new POIListRequestListener());

    }

    /**
     * Get the POI list from either the database or
     * via the webservice.
     */
    private void getPOIList(){
        long count = POIHelper.getCount(this);
        Log.d(TAG, "Found " + count + " Records");
        if(count > 0){
            ArrayList<PlaceOfInterest> list = (ArrayList<PlaceOfInterest>) readAll();
            updateView(list);
        }else {
            performPOIListRequest();
        }
    }

    /**
     * Initialize the UI components
     */
    private void init(){
        mListView = (ListView) findViewById(R.id.lv_places);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view,
                                    final int position, final long id) {
                PlaceOfInterest obj = (PlaceOfInterest) parent.getItemAtPosition(position);
                /**
                 * Read the record from the DB as we need to
                 * check the data level.
                 */
                ArrayList<PlaceOfInterest> list = (ArrayList<PlaceOfInterest>)
                        POIHelper.read(PlacesList.this, obj);
                if(null != list){
                    /**
                     * Assuming we have a unique record for this POI.
                     */
                    PlaceOfInterest temp = list.get(0);
                    int level = temp.getLevel();
                    switch (level){
                        case PlaceOfInterest.RECORD_LEVEL_PARTIAL:
                            performPOIDetailsRequest(obj);
                            break;
                        case PlaceOfInterest.RECORD_LEVEL_COMPLETE:
                            /**
                             * Pass the record to the details activity
                             */
                            gotoDetails(temp);
                            break;
                        default:
                            /**
                             * Always fetch the record from the server in case
                             * of any failure.
                             */
                            performPOIDetailsRequest(obj);

                    }
                }else {
                    /**
                     * No record found , fetch it from the server.
                     */
                    performPOIDetailsRequest(obj);
                }

            }
        });
        mAdapter = new POIListAdapter(this,null);
        mListView.setAdapter(mAdapter);
    }

    /**
     * Get details of the place.
     * @param placeOfInterest Place to get the details of.
     */
    private void performPOIDetailsRequest(PlaceOfInterest placeOfInterest){
        PoiDetailsRequest poiDetailsRequest = new PoiDetailsRequest(placeOfInterest.getId());
        mSpiceManager.execute(poiDetailsRequest, new POIDetailsRequestListener());
    }

    /**
     * Update the UI with the POI list.
     * @param poiList - List of points of interest.
     */
    private void updateView(final POIList poiList){
        ArrayList<PlaceOfInterest> list = (ArrayList<PlaceOfInterest>) poiList.getList();
        updateView(list);
    }

    /**
     * Update the list of places.
     * @param placeOfInterests - List of points of interest.
     */
    private void updateView(List<PlaceOfInterest> placeOfInterests){
        mAdapter.setList(placeOfInterests);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Start the PlaceDetails activity with the PlaceOfInterest data.
     * @param placeOfInterest
     */
    private void gotoDetails(PlaceOfInterest placeOfInterest){
        Intent intent = new Intent(PlacesList.this,PlaceDetails.class);
        intent.putExtra("PlaceDetails",placeOfInterest);
        startActivity(intent);
    }

    /**
     * Class to handle the data returned by .
     * @see com.octo.android.robospice.request.listener.RequestListener
     */
    private class POIListRequestListener implements RequestListener<POIList>{

        @Override
        public void onRequestFailure(final com.octo.android.robospice.persistence.exception.SpiceException e) {
            // TODO : Handle failure

        }

        @Override
        public void onRequestSuccess(final POIList poiList) {
            /**
             * Insert in the DB.
             */
            insert(poiList);
            /**
             * Update the UI.
             */
            updateView(poiList);
        }
    }

    /**
     * Class to handle the data returned by .
     * @see com.octo.android.robospice.request.listener.RequestListener
     */
    private class POIDetailsRequestListener implements RequestListener<PlaceOfInterest> {

        @Override
        public void onRequestFailure(final SpiceException e) {
            // TODO : Handle Failure
        }

        @Override
        public void onRequestSuccess(final PlaceOfInterest placeOfInterest) {
            Log.d(TAG, placeOfInterest.toString());
            /**
             * We got the complete data for this POI.
             */
            placeOfInterest.setLevel(PlaceOfInterest.RECORD_LEVEL_COMPLETE);
            /**
             * Update the record in DB
             */
            POIHelper.update(PlacesList.this,placeOfInterest);
            /**
             * Start the details activity
             */
            gotoDetails(placeOfInterest);

        }
    }
}
