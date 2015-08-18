package net.atos.interestingplaces;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import net.atos.interestingplaces.interfaces.Constants;
import net.atos.interestingplaces.pojo.PlaceOfInterest;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by a551481 on 04-08-2015.
 */
public class PoiDetailsRequest extends SpringAndroidSpiceRequest<PlaceOfInterest> implements Constants {

    /**
     * ID for whom we have to fetch the details.
     */
    private final int mId;

    public PoiDetailsRequest(final int id) {
        super(PlaceOfInterest.class);
        mId = id;
    }

    @Override
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = super.getRestTemplate();
        ClientHttpRequestFactory requestFactory = restTemplate
                .getRequestFactory();
        if (requestFactory instanceof SimpleClientHttpRequestFactory) {
            SimpleClientHttpRequestFactory simpleClientHttpRequestFactory =
                    (SimpleClientHttpRequestFactory) requestFactory;
            simpleClientHttpRequestFactory.setConnectTimeout(CONNECTION_TIMEOUT);
            simpleClientHttpRequestFactory.setReadTimeout(READ_TIMEOUT);
            restTemplate.setRequestFactory(simpleClientHttpRequestFactory);
        } else if(requestFactory instanceof HttpComponentsClientHttpRequestFactory){
            HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory =
                    (HttpComponentsClientHttpRequestFactory) requestFactory;
            httpComponentsClientHttpRequestFactory.setConnectTimeout(CONNECTION_TIMEOUT);
            httpComponentsClientHttpRequestFactory.setReadTimeout(READ_TIMEOUT);
            restTemplate.setRequestFactory(httpComponentsClientHttpRequestFactory);
        }
        return restTemplate;
    }

    @Override
    public PlaceOfInterest loadDataFromNetwork() {
        String url = POI_ID_URL + mId;
        return getRestTemplate().getForObject(url, PlaceOfInterest.class);
    }
}
