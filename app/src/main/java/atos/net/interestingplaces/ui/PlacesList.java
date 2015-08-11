package atos.net.interestingplaces.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.io.Serializable;
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
    private Bundle mSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);
        init();
//        getPOIList(savedInstanceState);
    }

    /**
     * Save all appropriate fragment state.
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        outState.putSerializable("PlacesList", (Serializable) mAdapter.getFilteredList());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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
    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_places_list, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);

        SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                mAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                mAdapter.getFilter().filter(newText);
                return true;
            }
        };

        searchView.setOnQueryTextListener(onQueryTextListener);

        MenuItem progressMenuItem = menu.findItem(R.id.action_progress);
        setProgressMenuItem(progressMenuItem);
        getPOIList(mSavedInstanceState);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Get the POI list from the server.
     */
    private void performPOIListRequest(){
        PoiListRequest request = new PoiListRequest();
        getSpiceManager().execute(request, new POIListRequestListener());

    }

    /**
     * Get the POI list from either the Bundle , database or
     * via the webservice.
     */
    private void getPOIList(final Bundle savedInstanceState){
        ArrayList<PlaceOfInterest> list = null;
        showProgress();
        if(null != savedInstanceState){
            /**
             * Check if we can get the data from the bundle
             */
            if(savedInstanceState.containsKey("PlacesList")){
                list = (ArrayList<PlaceOfInterest>) savedInstanceState.getSerializable("PlacesList");
                updateView(list);
                hideProgress();
            }
        }else {
            /**
             * Check if we can get the data from the database
             */
            long count = POIHelper.getCount(this);
            Log.d(TAG, "Found " + count + " Records");
            if(count > 0){
                list = (ArrayList<PlaceOfInterest>) readAll();
                updateView(list);
                hideProgress();
            }else {
                /**
                 * Get the data from the webservice
                 */
                performPOIListRequest();
            }
        }
/*        long count = POIHelper.getCount(this);
        Log.d(TAG, "Found " + count + " Records");
        if(count > 0){
            list = (ArrayList<PlaceOfInterest>) readAll();
            updateView(list);
            hideProgress();
        }else {
            performPOIListRequest();
        }*/
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
                            showProgress();
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
        getSpiceManager().execute(poiDetailsRequest, new POIDetailsRequestListener());
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
        mAdapter.setFilteredList(placeOfInterests);
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
            hideProgress();
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
            hideProgress();
        }
    }

    /**
     * Class to handle the data returned by .
     * @see com.octo.android.robospice.request.listener.RequestListener
     */
    private class POIDetailsRequestListener implements RequestListener<PlaceOfInterest> {

        @Override
        public void onRequestFailure(final SpiceException e) {
            hideProgress();
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
            update(placeOfInterest);
            /**
             * Start the details activity
             */
            gotoDetails(placeOfInterest);
            hideProgress();
        }
    }
}
