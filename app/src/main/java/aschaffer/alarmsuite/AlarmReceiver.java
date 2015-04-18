package aschaffer.alarmsuite;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.content.BroadcastReceiver;
import android.util.Log;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Alarm alarm = null;

        final byte[] data = intent.getByteArrayExtra(AlarmDatabase.ALARM_RAW_DATA);
        if(data != null){
            Parcel in = Parcel.obtain();
            in.unmarshall(data,0,data.length);
            in.setDataPosition(0);
            alarm = Alarm.CREATOR.createFromParcel(in);
        }
        Log.d("AlarmReceiver","Received: " + alarm.toString());
        if(alarm == null){
            return;
        }

        Intent alarmScreen = new Intent(context,AlarmScreen.class);
        alarmScreen.putExtra(AlarmDatabase.ALARM_INTENT_EXTRA, alarm);
        alarmScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        context.startActivity(alarmScreen);
    }
}