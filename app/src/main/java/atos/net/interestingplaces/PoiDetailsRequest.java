package atos.net.interestingplaces;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import atos.net.interestingplaces.interfaces.Constants;
import atos.net.interestingplaces.pojo.PlaceOfInterest;

/**
 * Created by a551481 on 04-08-2015.
 */
public class PoiDetailsRequest extends SpringAndroidSpiceRequest<PlaceOfInterest> implements Constants {

    private int mId;

    public PoiDetailsRequest(final int id) {
        super(PlaceOfInterest.class);
        mId = id;
    }

    @Override
    public PlaceOfInterest loadDataFromNetwork() throws Exception {
        String url = POI_ID_URL + mId;
        return getRestTemplate().getForObject(url,PlaceOfInterest.class);
    }
}
