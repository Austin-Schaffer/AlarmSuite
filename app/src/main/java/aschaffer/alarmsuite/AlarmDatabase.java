package aschaffer.alarmsuite;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class AlarmDatabase {

    public static final String ALARM_RAW_DATA = "intent.extra.alarm_raw";
    public static final String ALARM_INTENT_EXTRA = "intent.extra.alarm";

    public static Uri add(Context context, Alarm alarm){
        ContentValues vals = new ContentValues(6);
        ContentResolver resolver = context.getContentResolver();

        //vals.put(Ref._id.name(),(Long) alarm.getValue(Ref._id));
        vals.put(Ref.enabled.name(),(int) alarm.get(Ref.enabled));
        vals.put(Ref.title.name(),alarm.get(Ref.title).toString());
        vals.put(Ref.message.name(),alarm.get(Ref.message).toString());
        Number temp = (Number) alarm.get(Ref.timeInMillis);
        vals.put(Ref.timeInMillis.name(),temp.longValue());

        Number resolverId = (Number) alarm.get(Ref._id);
        resolver.update(ContentUris.withAppendedId(
                alarm.CONTENT_URI,resolverId.longValue()),vals,null,null);

        return Uri.EMPTY;
    }

    public static Alarm get(Context context, ContentResolver resolver, int id) {
        Number temp = id;
        Cursor c = resolver.query(ContentUris.withAppendedId(Alarm.CONTENT_URI,temp.longValue()),
                Alarm.ALARM_QUERY_COLUMNS, null, null, null);
        Alarm alarm = null;
        if (c != null){
            if (c.moveToFirst()){
                alarm = new Alarm(context, c);
            }
            c.close();
        }

        return alarm;
    }
}
