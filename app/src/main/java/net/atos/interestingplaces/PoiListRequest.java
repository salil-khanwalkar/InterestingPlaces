package net.atos.interestingplaces;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import net.atos.interestingplaces.dto.POIList;
import net.atos.interestingplaces.interfaces.Constants;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by a551481 on 03-08-2015.
 */
public class PoiListRequest extends SpringAndroidSpiceRequest<POIList> implements Constants {

    public PoiListRequest() {
        super(POIList.class);
    }

    @Override
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = super.getRestTemplate();
        ClientHttpRequestFactory requestFactory = restTemplate
                .getRequestFactory();
        if(requestFactory instanceof SimpleClientHttpRequestFactory) {
            SimpleClientHttpRequestFactory simpleClientHttpRequestFactory =
                    (SimpleClientHttpRequestFactory)requestFactory;
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
    public POIList loadDataFromNetwork() {
        return getRestTemplate().getForObject(POI_LIST_URL, POIList.class);
    }
}
