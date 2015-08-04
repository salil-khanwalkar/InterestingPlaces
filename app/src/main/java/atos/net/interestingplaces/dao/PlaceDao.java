package atos.net.interestingplaces.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PLACE".
*/
public class PlaceDao extends AbstractDao<Place, Long> {

    public static final String TABLENAME = "PLACE";

    /**
     * Properties of entity Place.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PlaceId = new Property(1, int.class, "placeId", false, "PLACE_ID");
        public final static Property Title = new Property(2, String.class, "title", false, "TITLE");
        public final static Property Transport = new Property(3, String.class, "transport", false, "TRANSPORT");
        public final static Property Email = new Property(4, String.class, "email", false, "EMAIL");
        public final static Property Geocoordinates = new Property(5, String.class, "geocoordinates", false, "GEOCOORDINATES");
        public final static Property Description = new Property(6, String.class, "description", false, "DESCRIPTION");
        public final static Property Phone = new Property(7, String.class, "phone", false, "PHONE");
        public final static Property Address = new Property(8, String.class, "address", false, "ADDRESS");
    };


    public PlaceDao(DaoConfig config) {
        super(config);
    }
    
    public PlaceDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PLACE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"PLACE_ID\" INTEGER NOT NULL ," + // 1: placeId
                "\"TITLE\" TEXT," + // 2: title
                "\"TRANSPORT\" TEXT," + // 3: transport
                "\"EMAIL\" TEXT," + // 4: email
                "\"GEOCOORDINATES\" TEXT," + // 5: geocoordinates
                "\"DESCRIPTION\" TEXT," + // 6: description
                "\"PHONE\" TEXT," + // 7: phone
                "\"ADDRESS\" TEXT);"); // 8: address
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PLACE\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Place entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getPlaceId());
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String transport = entity.getTransport();
        if (transport != null) {
            stmt.bindString(4, transport);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(5, email);
        }
 
        String geocoordinates = entity.getGeocoordinates();
        if (geocoordinates != null) {
            stmt.bindString(6, geocoordinates);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(7, description);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(8, phone);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(9, address);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Place readEntity(Cursor cursor, int offset) {
        Place entity = new Place( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // placeId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // title
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // transport
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // email
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // geocoordinates
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // description
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // phone
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // address
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Place entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPlaceId(cursor.getInt(offset + 1));
        entity.setTitle(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTransport(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setEmail(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setGeocoordinates(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDescription(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setPhone(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setAddress(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Place entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Place entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}