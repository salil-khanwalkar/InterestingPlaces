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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);
        init();
        performRequest();
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

    private void performRequest(){
        PoiListRequest request = new PoiListRequest();
        mSpiceManager.execute(request,new POIListRequestListener());

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
                getPlaceDetails(obj);
            }
        });
    }

    /**
     * Get the
     * @param placeOfInterest
     */
    private void getPlaceDetails(PlaceOfInterest placeOfInterest){
        PoiDetailsRequest poiDetailsRequest = new PoiDetailsRequest(placeOfInterest.getId());
        mSpiceManager.execute(poiDetailsRequest, new POIDetailsRequestListener());
    }

    /**
     * Update the UI with the POI list.
     * @param poiList - List of points of interest.
     */
    private void update(final POIList poiList){
        ArrayList<PlaceOfInterest> list = (ArrayList<PlaceOfInterest>) poiList.getList();
        long id = 0;
        for(PlaceOfInterest placeOfInterest : list){
            id = POIHelper.insert(PlacesList.this,placeOfInterest);
            Log.d(TAG,"Inserted " + id + " Records");
        }
        POIListAdapter adapter = new POIListAdapter(this,list);
        mListView.setAdapter(adapter);
    }

    /**
     * Class to handle the data returned by .
     * @see com.octo.android.robospice.request.listener.RequestListener
     */
    private class POIListRequestListener implements RequestListener<POIList>{

        @Override
        public void onRequestFailure(final com.octo.android.robospice.persistence.exception.SpiceException e) {

        }

        @Override
        public void onRequestSuccess(final POIList poiList) {
            update(poiList);
            // TODO : Remove test code
            // Start of test code
            ArrayList<PlaceOfInterest> placeOfInterestArrayList = (ArrayList<PlaceOfInterest>)
                    POIHelper.readAll(PlacesList.this);
            for(PlaceOfInterest obj : placeOfInterestArrayList){
                Log.d(TAG,"Read " + obj.toString());
            }

            // End of test code
        }
    }

    private class POIDetailsRequestListener implements RequestListener<PlaceOfInterest> {

        @Override
        public void onRequestFailure(final SpiceException e) {

        }

        @Override
        public void onRequestSuccess(final PlaceOfInterest placeOfInterest) {
            Log.d(TAG, placeOfInterest.toString());

            Intent intent = new Intent(PlacesList.this,PlaceDetails.class);
            intent.putExtra("PlaceDetails",placeOfInterest);
            startActivity(intent);
        }
    }
}
