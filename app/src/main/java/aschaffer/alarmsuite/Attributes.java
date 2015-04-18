package aschaffer.alarmsuite;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Vector;

public class Attributes implements Parcelable {

    private Vector<Attribute> list;
    private Context mContext;

    public Attributes(Context context){
        mContext = context;
        list = new Vector<>();
        add(Ref._id);
        add(Ref.enabled);
        add(Ref.title);
        add(Ref.message);
        add(Ref.timeInMillis);
    }

    public Attributes(Context context, Parcel p){
        mContext = context;
        list = new Vector<>();
        add(Ref._id, p.readInt());
        add(Ref.enabled,p.readInt());
        add(Ref.title,p.readString());
        add(Ref.message,p.readString());
        add(Ref.timeInMillis, p.readLong());
    }

    public Attributes(Context context, Cursor c) {
        mContext = context;
        list = new Vector<>();
        add(Ref._id);
        add(Ref.enabled, c.getInt(Ref.enabled.number()-1));
        add(Ref.title, c.getString(Ref.title.number()-1));
        add(Ref.message, c.getString(Ref.message.number()-1));
        add(Ref.timeInMillis, c.getLong(Ref.timeInMillis.number()-1));
    }

    public boolean add(Ref ref){
        list.add(new Attribute(ref));
        return list.contains(ref);
    }

    public boolean add(Ref ref, Object val){
        list.add(new Attribute(ref, val));
        return list.contains(ref);
    }

    public Object get(Ref ref){
        int intType = ref.number();
        return list.get(intType).value;
    }

    public boolean set(Ref att, Object newVal){
        Attribute newAtt = new Attribute(att,newVal);
        list.set(att.number(), newAtt);
        return list.contains(newAtt);
    }

    public Calendar getCalendar(){
        Calendar ret = getCalendar();
        ret.setTimeInMillis((Long) list.get(Ref.timeInMillis.number()).value);
        return ret;
    }

    public String timeTo(){
        //TODO: improve timeTo string
        String ret = (String) list.get(Ref.title.number()).value;
        ret = ret.concat(" set for ");
        long setTime = (long) list.get(Ref.timeInMillis.number()).value;
        long diff = setTime - System.currentTimeMillis();
        ret = ret.concat(String.valueOf(diff / 1000));
        String plural;
        plural = diff < 2000 ? "" : "s";
        ret = ret + " second" + plural + " from now";
        return ret;
    }

    @Override
    public int describeContents() {return 0;}

    @Override
    public void writeToParcel(Parcel p, int flags) {
        p.writeInt((int) list.get(Ref._id.number()).value);
        p.writeInt((int) list.get(Ref.enabled.number()).value);
        p.writeString(list.get(Ref.title.number()).value.toString());
        p.writeString(list.get(Ref.message.number()).value.toString());
        Number temp = (Number) list.get(Ref.timeInMillis.number()).value;
        p.writeLong(temp.longValue());
    }
}