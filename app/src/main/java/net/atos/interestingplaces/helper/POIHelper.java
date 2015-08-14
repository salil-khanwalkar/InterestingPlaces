package net.atos.interestingplaces.helper;

import android.content.Context;

import net.atos.interestingplaces.InterestingPlacesApplication;
import net.atos.interestingplaces.dao.Place;
import net.atos.interestingplaces.dao.PlaceDao;
import net.atos.interestingplaces.pojo.PlaceOfInterest;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by a551481 on 04-08-2015.
 */
public class POIHelper {


  /*  private static PlaceDao getPOIDao(Context context){
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context,"poi.db",null);
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        PlaceDao placeDao = daoSession.getPlaceDao();
        return placeDao;
    }*/

    /**
     * Get the PlaceDao reference
     * @param context
     * @return {@link PlaceDao} object.
     */
    private static PlaceDao getPOIDao(Context context){
        InterestingPlacesApplication interestingPlacesApplication =
                (InterestingPlacesApplication) context.getApplicationContext();
        return interestingPlacesApplication.getPlaceDao();
    }

    /**
     * Convert GreenDao {@link Place} object to our {@link PlaceOfInterest} object.
     * The {@link PlaceOfInterest} object is used to update the UI and to parse the JSON response.
     * and {@link Place} object is used to update the database.
     * @param placeOfInterest Object to convert
     * @return {@link Place} object.
     */
    // TODO : Explore more on GreenDAO KEEP Sections so that we can use the same {@link Place}
    // object for database operations and JSON parsing.

    public static Place convertToGreenDao(PlaceOfInterest placeOfInterest){
        Place place = new Place();
        place.setId((long) placeOfInterest.getId());
        place.setPlaceId(placeOfInterest.getId());
        place.setAddress(placeOfInterest.getAddress());
        place.setDescription(placeOfInterest.getDescription());
        place.setEmail(placeOfInterest.getEmail());
        place.setGeocoordinates(placeOfInterest.getGeoCoordinates());
        place.setPhone(placeOfInterest.getPhone());
        place.setTransport(placeOfInterest.getTransport());
        place.setTitle(placeOfInterest.getTitle());
        place.setLevel(placeOfInterest.getLevel());
        return  place;
    }

    /**
     * Convert {@link PlaceOfInterest} object to GreenDao {@link Place} object.
     * The {@link PlaceOfInterest} object is used to update the UI and to parse the JSON response.
     * and {@link Place} object is used to update the database.
     * @param place Object to convert
     * @return {@link PlaceOfInterest} object
     */
    // TODO : Explore more on GreenDAO KEEP Sections so that we can use the same {@link Place}
    // object for database operations and JSON parsing.
    public static PlaceOfInterest convertFromGreenDao(Place place){
        PlaceOfInterest placeOfInterest = new PlaceOfInterest();

        placeOfInterest.setId(place.getPlaceId());
        placeOfInterest.setAddress(place.getAddress());
        placeOfInterest.setTitle(place.getTitle());
        placeOfInterest.setPhone(place.getPhone());
        placeOfInterest.setTransport(place.getTransport());
        placeOfInterest.setDescription(place.getDescription());
        placeOfInterest.setEmail(place.getEmail());
        placeOfInterest.setGeoCoordinates(place.getGeocoordinates());
        placeOfInterest.setLevel(place.getLevel());

        return placeOfInterest;
    }

    /**
     * Insert a single {@link PlaceOfInterest} object to the database.
     * @param context Context
     * @param placeOfInterest
     * @return Number of rows inserted
     */
    public static long insert(Context context,PlaceOfInterest placeOfInterest){
        PlaceDao placeDao = getPOIDao(context);
        Place place = convertToGreenDao(placeOfInterest);
        return placeDao.insert(place);
    }

    /**
     * Reads all {@link Place} objects , converts them to {@link PlaceOfInterest} objects
     * and returns a list.
     * @param context Context
     * @return List of {@link PlaceOfInterest} objects
     */
    public static List<PlaceOfInterest> readAll(Context context){
        List<Place> list = null;
        List<PlaceOfInterest> placeOfInterestList = null;
        PlaceDao placeDao = getPOIDao(context);
        list = placeDao.queryBuilder().list();
        if(null != list){
            placeOfInterestList = new ArrayList<>();
            for(Place place : list){
                PlaceOfInterest placeOfInterest = convertFromGreenDao(place);
                placeOfInterestList.add(placeOfInterest);
            }
        }
        return placeOfInterestList;
    }

    /**
     * Reads a single {@link Place} object and converts to {@link PlaceOfInterest} object.
     * @param context Context
     * @param placeOfInterest {@link PlaceOfInterest} object to be converted.
     * @return Converted {@link Place} object
     */
    public static List<PlaceOfInterest> read(Context context, PlaceOfInterest placeOfInterest){
        List<Place> list = null;
        List<PlaceOfInterest> placeOfInterestList = null;
        Place place = convertToGreenDao(placeOfInterest);
        PlaceDao placeDao = getPOIDao(context);
        list = placeDao.queryBuilder().
                where(PlaceDao.Properties.Id.eq(place.getId())).
                list();
        if(null != list){
            placeOfInterestList = new ArrayList<>();
            for(Place placeObj : list){
                PlaceOfInterest placeOfInterestObj = convertFromGreenDao(placeObj);
                placeOfInterestList.add(placeOfInterestObj);
            }
        }
        return placeOfInterestList;
    }

    /**
     * Updates a database record
     * @param context Context
     * @param placeOfInterest Converts the {@link PlaceOfInterest} to {@link Place} object
     *                        and updates it
     */
    public static void update(Context context,PlaceOfInterest placeOfInterest){
        Place place = convertToGreenDao(placeOfInterest);
        PlaceDao placeDao = getPOIDao(context);
        QueryBuilder builder = placeDao.queryBuilder();
        builder.where(PlaceDao.Properties.Id.eq(placeOfInterest.getId()));
        List<Place> placeList = builder.list();
        placeDao.update(place);
    }

    /**
     * Returns number of records in the database
     * @param context
     * @return
     */
    public static long getCount(Context context){
        PlaceDao dao = getPOIDao(context);
        return dao.count();
    }

    /**
     * Deletes all the records from the Database
     * @param context Context
     */
    public static void deleteAll(Context context){
        PlaceDao placeDao = getPOIDao(context);
        placeDao.deleteAll();
    }

}
