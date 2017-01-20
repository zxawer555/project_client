package self.edu.project_client.method.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Creasant on 7/8/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static DatabaseHandler _Instance;
    public static final String DATABASE_NAME = "database.db";
    public static final String TABLE_NAME = "ELDERLY";
    public static final String ELDERLY_COLUMN_ID = "id";
    public static final String ELDERLY_COLUMN_EID = "eid";
    public static final String ELDERLY_COLUMN_TOKEN = "firebase_token";
    public static final String ELDERLY_COLUMN_IDENTIFIER = "identifier";
    public static final String ELDERLY_COLUMN_MESSAGE = "message";
    public static final String ELDERLY_COLUMN_PHOTOLINK = "photo_link";
    public static final String ELDERLY_COLUMN_GPSStatus = "gps_status";

    public static DatabaseHandler getInstance(Context context) {
        _Instance = new DatabaseHandler(context);

        return _Instance;
    }


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated constructor stub

        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                "id                 INTEGER PRIMARY KEY, " +
                "eid                TEXT, " +
                "firebase_token     TEXT, " +
                "identifier         TEXT, " +
                "message            TEXT, " +
                "photo_link         TEXT, " +
                "gps_status         TEXT)";

        db.execSQL(sql);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated constructor stub

        String sql = "DROP TABLE IF EXISTS TICKET";

        // Drop older table if existed
        db.execSQL(sql);

        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public boolean addContact(String eid, String firebase_token, String identifier,
                              String message, String photo_link) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ELDERLY_COLUMN_EID, eid);
        contentValues.put(ELDERLY_COLUMN_TOKEN, firebase_token);
        contentValues.put(ELDERLY_COLUMN_IDENTIFIER, identifier);
        contentValues.put(ELDERLY_COLUMN_MESSAGE, message);
        contentValues.put(ELDERLY_COLUMN_PHOTOLINK, photo_link);
        contentValues.put(ELDERLY_COLUMN_GPSStatus, "off");

        db.insert(TABLE_NAME, null, contentValues);

        return true;
    }

    // Getting single contact
    public List<Map<String, Object>> getContact(int id) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;

        SQLiteDatabase db = this.getReadableDatabase();

        String key_id = String.valueOf(id);

        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id=" + key_id + "";

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        map = new HashMap<String, Object>();
        map.put(ELDERLY_COLUMN_ID, cursor.getString(0));
        map.put(ELDERLY_COLUMN_EID, cursor.getString(1));
        map.put(ELDERLY_COLUMN_TOKEN, cursor.getString(2));
        map.put(ELDERLY_COLUMN_IDENTIFIER, cursor.getString(3));
        map.put(ELDERLY_COLUMN_MESSAGE, cursor.getString(4));
        map.put(ELDERLY_COLUMN_PHOTOLINK, cursor.getString(5));
        map.put(ELDERLY_COLUMN_GPSStatus, cursor.getString(6));

        list.add(map);

        return list;
    }

    // Check column is not exist
    public boolean checkContact(String identifier) {
        boolean isExist = false;
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE identifier='" + identifier + "'";

        Cursor cursor = db.rawQuery(sql, null);

        if (!cursor.moveToFirst()) {
            isExist = true;
        }

        return isExist;

    }

    // Getting All Contacts
    public List<Map<String, Object>> getAllContact() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;

        // hp = new HashMap();
        String sql = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            map = new HashMap<String, Object>();
            map.put(ELDERLY_COLUMN_ID, cursor.getString(0));
            map.put(ELDERLY_COLUMN_EID, cursor.getString(1));
            map.put(ELDERLY_COLUMN_TOKEN, cursor.getString(2));
            map.put(ELDERLY_COLUMN_IDENTIFIER, cursor.getString(3));
            map.put(ELDERLY_COLUMN_MESSAGE, cursor.getString(4));
            map.put(ELDERLY_COLUMN_PHOTOLINK, cursor.getString(5));
            map.put(ELDERLY_COLUMN_GPSStatus, cursor.getString(6));

            list.add(map);
            cursor.moveToNext();
        }

        return list;
    }

    // Getting contacts Count
    public int getContactsCount() {

        // Select All Query
        String sql = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        int contactsCount = cursor.getCount();

        db.close();

        return contactsCount;
    }

    // Updating single contact
    public boolean updateContact(int id, String gps_status) {
        SQLiteDatabase db = this.getWritableDatabase();

        String key_id = String.valueOf(id);

        ContentValues contentValues = new ContentValues();
        contentValues.put(ELDERLY_COLUMN_GPSStatus, gps_status);

        db.update(TABLE_NAME, contentValues, "id = ? ", new String[]{key_id});

        Log.d("result", "finish");

        return true;
    }

    public int deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String key_id = String.valueOf(id);

        return db.delete(TABLE_NAME,
                "id = ? ",
                new String[]{key_id});
    }

    public void deleteAllContact() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, null, null);
    }

}
