package aschaffer.alarmsuite;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Calendar;


public final class Alarm implements Parcelable{

    private static Context mContext;
    private Attributes atts;

    public static final Uri CONTENT_URI = Uri.parse("content://aschaffer.alarmsuite/alarms");
    public static final String[] ALARM_QUERY_COLUMNS = { Ref.enabled.name(),
            Ref.title.name(), Ref.message.name(), Ref.timeInMillis.name()};

    public static final Parcelable.Creator<Alarm> CREATOR
            = new Parcelable.Creator<Alarm>() {
        public Alarm createFromParcel(Parcel p) {
            return new Alarm(mContext,p);
        }

        @Override
        public Alarm[] newArray(int size) {
            Log.d("Alarm","newArray Constructor, size: " + size);
            return new Alarm[size];
        }
    };

    public Alarm(Context context, Cursor c) {
        mContext = context;
        atts = new Attributes(context, c);
    }

    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        atts.writeToParcel(dest,flags);
    }

    public Alarm(Context context){
        mContext = context;
        atts = new Attributes(mContext);
    }

    public boolean set(Ref att, Object val){
        return atts.set(att,val);
    }

    public Alarm(Context context, Parcel p){
        mContext = context;
        atts = new Attributes(mContext, p);
    }

    public String timeTo(){
        return atts.timeTo();
    }

    public Calendar getCalendar() {
        return atts.getCalendar();
    }

    public Object getValue(Ref which){
        switch (which) {
            case _id:
                Number tempId = (Number) atts.get(which);
                return tempId.longValue();
            case enabled:
                Number tempEnabled = (Number) atts.get(which);
                return tempEnabled.intValue();
            case title:
                return atts.get(which).toString();
            case message:
                return atts.get(which).toString();
            case timeInMillis:
                Number tempTime = (Number) atts.get(which);
                return tempTime.longValue();
            default:
                return null;
        }
    }

    //

    public Object get(Ref which){
        return atts.get(which);
    }

    public String toString(){
        Number time = (Number) atts.get(Ref.timeInMillis);
        String ret =
                "\nId:      " + atts.get(Ref._id) +
                "\nTitle:   " + atts.get(Ref.title).toString() +
                "\nEnabled: " + atts.get(Ref.enabled).toString() +
                "\nMessage: " + atts.get(Ref.message).toString() +
                "\nTimeInM: " + time.toString();

        return ret;
    }
}
