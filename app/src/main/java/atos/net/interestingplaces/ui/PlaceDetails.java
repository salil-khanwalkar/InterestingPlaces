package atos.net.interestingplaces.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import atos.net.interestingplaces.R;
import atos.net.interestingplaces.pojo.PlaceOfInterest;

public class PlaceDetails extends BaseActivity {

    private static final String TAG = PlaceDetails.class.getSimpleName();

    private TextView mTitle;
    private TextView mAddress;
    private TextView mTransport;
    private TextView mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        PlaceOfInterest placeOfInterest = (PlaceOfInterest) getIntent().
                getSerializableExtra("PlaceDetails");
        init();
        update(placeOfInterest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_place_details, menu);
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
     * Initialize the UI components
     */
    private void init(){
        mTitle = (TextView) findViewById(R.id.tv_place_title);
        mAddress = (TextView) findViewById(R.id.tv_place_address);
        mTransport = (TextView) findViewById(R.id.tv_place_transport);
        mDescription = (TextView) findViewById(R.id.tv_place_description);
    }

    /**
     * Update the UI
     * @param placeOfInterest object to use to update the UI.
     */
    private void update(PlaceOfInterest placeOfInterest){
        mTitle.setText(placeOfInterest.getTitle());
        mTransport.setText(placeOfInterest.getTransport());
        mAddress.setText(placeOfInterest.getAddress());
        mDescription.setText(placeOfInterest.getDescription());
    }
}
