package atos.net.interestingplaces.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import atos.net.interestingplaces.dao.DaoMaster;
import atos.net.interestingplaces.dao.DaoSession;
import atos.net.interestingplaces.dao.Place;
import atos.net.interestingplaces.dao.PlaceDao;
import atos.net.interestingplaces.pojo.PlaceOfInterest;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by a551481 on 04-08-2015.
 */
public class POIHelper {


    private static PlaceDao getPOIDao(Context context){
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context,"poi.db",null);
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        PlaceDao placeDao = daoSession.getPlaceDao();
        return placeDao;
    }

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

    public static long insert(Context context,PlaceOfInterest placeOfInterest){
        PlaceDao placeDao = getPOIDao(context);
        Place place = convertToGreenDao(placeOfInterest);
        return placeDao.insert(place);
    }

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

    public static void update(Context context,PlaceOfInterest placeOfInterest){
        Place place = convertToGreenDao(placeOfInterest);
        PlaceDao placeDao = getPOIDao(context);

        QueryBuilder builder = placeDao.queryBuilder();
        builder.where(PlaceDao.Properties.Id.eq(placeOfInterest.getId()));

        List<Place> placeList = builder.list();

        placeDao.update(place);

    }

    public static long getCount(Context context){
        PlaceDao dao = getPOIDao(context);
        return dao.count();
    }

}
