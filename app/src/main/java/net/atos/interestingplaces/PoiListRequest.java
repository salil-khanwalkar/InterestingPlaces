package net.atos.interestingplaces;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import net.atos.interestingplaces.dto.POIList;
import net.atos.interestingplaces.interfaces.Constants;

/**
 * Created by a551481 on 03-08-2015.
 */
public class PoiListRequest extends SpringAndroidSpiceRequest<POIList> implements Constants {

    public PoiListRequest() {
        super(POIList.class);
    }

    @Override
    public POIList loadDataFromNetwork() throws Exception {
        return getRestTemplate().getForObject(POI_LIST_URL,POIList.class);
    }
}
