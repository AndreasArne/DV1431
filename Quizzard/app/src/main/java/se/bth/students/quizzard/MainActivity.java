package se.bth.students.quizzard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends Activity {

    private SensorManager mSensorManager;
    private ShakeListener mSensorListener;
    private Calendar last;
    private Calendar now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for shake event
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeListener();
        last = Calendar.getInstance();

        mSensorListener.setOnShakeListener(new ShakeListener.OnShakeListener() {

            public void onShake() {
                now = Calendar.getInstance();
                if( (now.getTimeInMillis() - last.getTimeInMillis()) >750) {
                    last = now;
                    Toast.makeText(getApplicationContext(), "Shake!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Go to list quizzes, button listener
        Button list = (Button) findViewById(R.id.list_Quizzes);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"List quizzes!",Toast.LENGTH_SHORT).show();
                Intent listInt = new Intent(MainActivity.this,ListQuizzes.class);
                startActivity(listInt);
            }
        });

        //Go to create Quiz, button listener
        Button createQ = (Button) findViewById(R.id.create_Quiz);
        createQ.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Create quiz!",Toast.LENGTH_SHORT ).show();
                Intent createInt = new Intent(getApplicationContext(),CreateQuiz.class);
                startActivity(createInt);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }



  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.menu_main, menu);
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
    }*/
}
