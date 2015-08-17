package net.atos.interestingplaces.interfaces;

/**
 * Created by a551481 on 03-08-2015.
 */
public interface Constants {

    String BASE_URL     = "http://t21services.herokuapp.com/points";
    String POI_LIST_URL = BASE_URL;
    String POI_ID_URL   = BASE_URL + "/";

    String DATABASE_NAME = "poi.db";

    int ONE_MILLISECOND    = 1000;
    int CONNECTION_TIMEOUT = 20 * ONE_MILLISECOND;
    int READ_TIMEOUT       = 20 * ONE_MILLISECOND;


}
