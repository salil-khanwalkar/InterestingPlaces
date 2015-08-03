package atos.net.interestingplaces;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import atos.net.interestingplaces.dto.POIList;
import atos.net.interestingplaces.interfaces.Hosts;

/**
 * Created by a551481 on 03-08-2015.
 */
public class PoiListRequest extends SpringAndroidSpiceRequest<POIList> implements Hosts {

    public PoiListRequest() {
        super(POIList.class);
    }

    @Override
    public POIList loadDataFromNetwork() throws Exception {
        return getRestTemplate().getForObject(POI_LIST_URL,POIList.class);
    }
}
