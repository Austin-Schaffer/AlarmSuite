package aschaffer.alarmsuite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.Vector;

public class DatabaseProvider extends ContentProvider {

    private SQLiteOpenHelper mSqlHelper;
    private static final int ALARMS =1;
    private static final int ALARMS_ID =2;
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        mUriMatcher.addURI("aschaffer.alarmsuite","alarms",ALARMS);
        mUriMatcher.addURI("aschaffer.alarmsuite","alarms/#",ALARMS_ID);
    }

    public DatabaseProvider() {
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "alarms.db";
        private static final int DATABASE_VERSION = 5;

        public DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(
                    "CREATE TABLE alarms ("+
                    "_id INTEGER PRIMARY KEY, "+
                    "enabled INTEGER, "+
                    "title TEXT, "+
                    "message TEXT, "+
                    "timeInMillis INTEGER);");

            String in = "INSERT INTO alarms (enabled, title, " +
                    "message, timeInMillis) VALUES ";
            db.execSQL(in + "(1, 'onCreateAlarm', 'onCreateMessage', 1);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS alarms");
            onCreate(db);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mSqlHelper.getWritableDatabase();
        int count;
        long id = 0;
        switch (mUriMatcher.match(uri)){
            case ALARMS:
                count = db.delete("alarms",selection,selectionArgs);
                break;
            case ALARMS_ID:
                String path = uri.getPathSegments().get(1);
                id = Long.parseLong(path);
                if(TextUtils.isEmpty(selection)){
                    selection = "_id=" + path;
                } else {
                    selection = "_id=" + path + " AND (" + selection + ")";
                }
                count = db.delete("alarms",selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Can't delete " + selection + " from " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch(mUriMatcher.match(uri)){
            case ALARMS:
                return "vnd.android.cursor.dir/alarms";
            case ALARMS_ID:
                return "vnd.android.cursor.item/alarms";
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if(mUriMatcher.match(uri) != ALARMS){
            throw new IllegalArgumentException("Cant insert");
        }
        ContentValues retValues;
        if(values != null){
            retValues = new ContentValues(values);
        } else {
            retValues = new ContentValues();
        }
        Vector names = Ref.getAttNames();
        for (Object o : names) {
            if(!retValues.containsKey(o.toString())){
                Toast.makeText(getContext(),o.toString(),Toast.LENGTH_LONG).show();
                retValues.put(o.toString(),-1);
            }
        }

        SQLiteDatabase db = mSqlHelper.getWritableDatabase();
        long id = db.insert("alarms",null,retValues);
        if(id < 0){
            try {
                throw new SQLException("failed to insert row into: " + uri);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("DatabaseProvider", "Put alarm into row: " + id);
        }

        Uri ret = ContentUris.withAppendedId(Alarm.CONTENT_URI,id);
        getContext().getContentResolver().notifyChange(ret,null);
        return ret;
    }

    @Override
    public boolean onCreate() {
        mSqlHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch(mUriMatcher.match(uri)){
            case ALARMS:
                queryBuilder.setTables("alarms");
                break;
            case ALARMS_ID:
                queryBuilder.setTables("alarms");
                queryBuilder.appendWhere("_id=");
                queryBuilder.appendWhere(uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mSqlHelper.getReadableDatabase();
        Cursor ret = queryBuilder.query(
                db, projection, selection, selectionArgs, null, null, sortOrder);

        if(ret != null){
            ret.setNotificationUri(getContext().getContentResolver(), uri);
        } else {
            Log.v("Error","Empty Cursor");
        }

        return ret;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count;
        long rowId = 0;
        SQLiteDatabase db = mSqlHelper.getWritableDatabase();
        switch(mUriMatcher.match(uri)){
            case ALARMS_ID:
                String segment = uri.getPathSegments().get(1);
                rowId = Long.parseLong(segment);
                count = db.update("alarms",values, "_id=" + rowId, null);
                break;
            default:
                throw new UnsupportedOperationException(
                        "uri: " + uri.toString() + ", match: " + mUriMatcher.match(uri));
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
