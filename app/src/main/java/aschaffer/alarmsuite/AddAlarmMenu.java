package aschaffer.alarmsuite;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Parcel;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;

public class AddAlarmMenu extends ActionBarActivity {

    private static final String ALARM_SCREEN_ACTION = "aschaffer.alarmsuite.ALARM_SCREEN";
    private AlarmManager alarmMgr;
    private AlarmDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mDatabase = new AlarmDatabase();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_alarm_menu);

        final Button saveButton = (Button) findViewById(R.id.saveAlarmButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            private View v;

            @Override
            public void onClick(View v) {
                setAlarm(getApplicationContext());
            }
        });
    }

    private void setAlarm(Context context){
        Alarm alarm = new Alarm(context);
        alarmMgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        final TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        calendar.set(Calendar.SECOND, 0);

        if(BuildConfig.DEBUG) calendar.setTimeInMillis(System.currentTimeMillis()+1000);


        alarm.set(Ref._id, 1);//FIXME: get an actual id;
        alarm.set(Ref.enabled, 1);

        EditText titleWidget = (EditText) findViewById(R.id.title);
        alarm.set(Ref.title,titleWidget.getText());

        EditText messageWidget = (EditText) findViewById(R.id.message);
        alarm.set(Ref.message,messageWidget.getText());

        alarm.set(Ref.timeInMillis,calendar.getTimeInMillis());

        int alarmType = AlarmManager.RTC_WAKEUP;

        Parcel alarmParcel = Parcel.obtain();
        alarm.writeToParcel(alarmParcel, 0);
        alarmParcel.setDataPosition(0);
        Intent saveAlarmIntent = new Intent(ALARM_SCREEN_ACTION);
        saveAlarmIntent.putExtra(mDatabase.ALARM_RAW_DATA, alarmParcel.marshall());
        PendingIntent alarmIntent = PendingIntent.getBroadcast(
                context, 0, saveAlarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        mDatabase.add(getApplicationContext(),alarm);

        Number id = (Number) alarm.get(Ref._id);
        Alarm placedAlarm = mDatabase.get(context,getContentResolver(),id.intValue());
        if (placedAlarm != null){
            Log.d("AlarmDatabase", "Successfully placed in database" + placedAlarm.toString());
        } else {
            Log.d("AlarmDatabase", "Alarm not placed in database" + placedAlarm.toString());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmMgr.setExact(alarmType, calendar.getTimeInMillis(), alarmIntent);
            if (!BuildConfig.DEBUG) Toast.makeText(this,alarm.timeTo(), Toast.LENGTH_LONG).show();
        } else {
            alarmMgr.set(alarmType, calendar.getTimeInMillis(), alarmIntent);
            if (!BuildConfig.DEBUG) Toast.makeText(this,alarm.timeTo(), Toast.LENGTH_LONG).show();
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_alarm_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
