package aschaffer.alarmsuite;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity //implements {@link OnFragmentInteractionListener}
{
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            getSupportFragmentManager().beginTransaction().add(
                    R.id.alarmListContainer, new AlarmListFragment()).commit();
        }

        final Button addAlarmButton = (Button) findViewById(R.id.addAlarm);
        addAlarmButton.setOnClickListener(new View.OnClickListener()
        {
            public Intent addAlarmMenu;

            @Override
            public void onClick(View v)
            {
                addAlarmMenu = new Intent(getApplicationContext(),AddAlarmMenu.class);
                startActivity(addAlarmMenu);
            }
        });

        AlarmListFragment listFragment;
        View v = findViewById(R.id.alarmList);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
