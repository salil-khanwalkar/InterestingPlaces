package net.atos.interestingplaces.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import net.atos.interestingplaces.PoiDetailsRequest;
import net.atos.interestingplaces.PoiListRequest;
import net.atos.interestingplaces.R;
import net.atos.interestingplaces.adapters.POIListAdapter;
import net.atos.interestingplaces.dto.POIList;
import net.atos.interestingplaces.helper.POIHelper;
import net.atos.interestingplaces.pojo.PlaceOfInterest;
import net.atos.interestingplaces.utils.NetworkUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlacesList extends BaseActivity {

    private static final String TAG = PlacesList.class.getSimpleName();
    private ListView mListView;
    private POIListAdapter mAdapter;
    private Bundle mSavedInstanceState;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);
        mSavedInstanceState = savedInstanceState;
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
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

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
        if(NetworkUtil.isConnected(this)){
            PoiListRequest request = new PoiListRequest();
            getSpiceManager().execute(request, new POIListRequestListener());
        }else {
            Toast.makeText(this,getString(R.string.no_internet),Toast.LENGTH_LONG).show();
            hideProgress();
        }
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
            long count = POIHelper.getCount(PlacesList.this);
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
//        mListView.setEmptyView(findViewById(android.R.id.empty));

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary_dark, R.color.accent, R.color.primary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(NetworkUtil.isConnected(PlacesList.this)){
                    /**
                     * Refresh the list view data, discard all data from the
                     * Bundle or the Database.
                     */
                    deleteAll();
                    getPOIList(null);
                }else {
                    Toast.makeText(PlacesList.this,
                            PlacesList.this.getString(R.string.no_internet),
                            Toast.LENGTH_LONG).show();
                    hideProgress();
                }

            }
        });
    }

    /**
     * Get details of the place.
     * @param placeOfInterest Place to get the details of.
     */
    private void performPOIDetailsRequest(PlaceOfInterest placeOfInterest){
        if(NetworkUtil.isConnected(this)) {
            PoiDetailsRequest poiDetailsRequest = new PoiDetailsRequest(placeOfInterest.getId());
            getSpiceManager().execute(poiDetailsRequest, new POIDetailsRequestListener());
        }else{
            Toast.makeText(this,getString(R.string.no_internet),Toast.LENGTH_LONG).show();
            hideProgress();
        }
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
     * Hide the progress bar
     */
    @Override
    protected void hideProgress() {
        super.hideProgress();
        if(null != mSwipeRefreshLayout){
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * Start the PlaceDetails activity with the {@link PlaceOfInterest} data.
     * The data is either fetched from the database or from the web service and then
     * passed to the details view in a bundle.
     * @param placeOfInterest POI to be shown
     */
    private void gotoDetails(PlaceOfInterest placeOfInterest){
        Intent intent = new Intent(PlacesList.this,PlaceDetails.class);
        intent.putExtra("PlaceDetails", placeOfInterest);
        startActivity(intent);

    /*    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            String transition = PlacesList.this.getString(R.string.place_transition);
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                            mListView,transition);
            ActivityCompat.startActivity(this,intent,options.toBundle());
        }else {
            startActivity(intent);
        }*/


    }

    /**
     * Class to handle the data returned by
     * {@link RequestListener}
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
