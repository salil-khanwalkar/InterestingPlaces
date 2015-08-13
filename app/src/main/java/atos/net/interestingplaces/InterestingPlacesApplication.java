package atos.net.interestingplaces;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import atos.net.interestingplaces.dao.DaoMaster;
import atos.net.interestingplaces.dao.DaoSession;
import atos.net.interestingplaces.dao.PlaceDao;
import atos.net.interestingplaces.interfaces.Constants;

/**
 * Created by a551481 on 04-08-2015.
 */
public class InterestingPlacesApplication extends Application
        implements Constants {

    private SQLiteDatabase mDb = null;
    private PlaceDao   mPlaceDao;
    private DaoSession mDaoSession;
    private DaoMaster mDaoMaster;


    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     * If you override this method, be sure to call super.onCreate().
     */
    @Override
    public void onCreate() {
        super.onCreate();

//        registerComponentCallbacks(this);

        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this,DATABASE_NAME,null);
        mDb = devOpenHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mDb);
        mDaoSession = mDaoMaster.newSession();
        mPlaceDao = mDaoSession.getPlaceDao();
    }

    /**
     * Return the dao object
     * @return
     */
    public PlaceDao getPlaceDao(){
        return mPlaceDao;
    }

}
