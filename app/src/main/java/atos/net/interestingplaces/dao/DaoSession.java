package atos.net.interestingplaces.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import atos.net.interestingplaces.dao.Place;

import atos.net.interestingplaces.dao.PlaceDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig placeDaoConfig;

    private final PlaceDao placeDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        placeDaoConfig = daoConfigMap.get(PlaceDao.class).clone();
        placeDaoConfig.initIdentityScope(type);

        placeDao = new PlaceDao(placeDaoConfig, this);

        registerDao(Place.class, placeDao);
    }
    
    public void clear() {
        placeDaoConfig.getIdentityScope().clear();
    }

    public PlaceDao getPlaceDao() {
        return placeDao;
    }

}