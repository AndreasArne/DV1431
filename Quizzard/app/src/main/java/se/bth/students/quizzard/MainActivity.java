package se.bth.students.quizzard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class MainActivity extends Activity {

    private SensorManager mSensorManager;
    private ShakeListener mSensorListener;
    private Calendar last;
    private Calendar now;
    private ArrayList<Quiz> quizzes;
    static public final int GET_NEW_QUIZ_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        quizzes = new ArrayList<Quiz>();

        //deleteQuizzesFromDisk(); // DEBUGGING! DELETE LATER
        readQuizzes();

        //for shake event
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeListener();
        last = Calendar.getInstance();
        //writeQuizzes();


        mSensorListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            public void onShake() {
                now = Calendar.getInstance();
                if( (now.getTimeInMillis() - last.getTimeInMillis()) >750) {
                    last = now;
                    Toast.makeText(getApplicationContext(), "Shake!", Toast.LENGTH_SHORT).show();
                    if(quizzes.size() == 1){
                        Intent i = new Intent(getApplicationContext(), DoQuiz.class);
                        i.putExtra("Quiz", quizzes.get(0));
                        startActivity(i);

                    }
                    else if(quizzes.size() > 1){
                        Random r = new Random();
                        int index = r.nextInt(quizzes.size()) ;
                        Intent i = new Intent(getApplicationContext(), DoQuiz.class);
                        i.putExtra("Quiz", quizzes.get(index));
                        startActivity(i);
                    }
                }
            }
        });

        //Go to list quizzes, button listener
        Button list = (Button) findViewById(R.id.list_Quizzes);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"List quizzes!",Toast.LENGTH_SHORT).show();
                Intent listInt = new Intent(MainActivity.this,ListQuizzes.class);
                listInt.putExtra("quizzes",quizzes);
                startActivity(listInt);
            }
        });

        //Go to create Quiz, button listener
        Button createQ = (Button) findViewById(R.id.create_Quiz);
        createQ.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"Create quiz!",Toast.LENGTH_SHORT ).show();
                //writeQuizzes();
                //readQuizzes();
                Intent createInt = new Intent(getApplicationContext(),CreateQuiz.class);
                createInt.putExtra("quizzes",quizzes);
                startActivityForResult(createInt, GET_NEW_QUIZ_CODE);
            }
        });


        // TILLFÄLLIG KNAPP, ta bort när vi har ListQuizzes -> DoQuiz-länken
     /*   Button btn_doQuiz = (Button) findViewById(R.id.test_do_Quiz);
        btn_doQuiz.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Do Quiz",Toast.LENGTH_SHORT ).show();
                Intent doQuizInt = new Intent(MainActivity.this,DoQuiz.class);
                doQuizInt.putExtra("quizzes",quizzes);
                startActivity(doQuizInt);
            }
        });*/

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
        writeQuizzes();
        super.onPause();
    }

    private void writeQuizzes(){
        //////////
        //Quiz q1 = new Quiz("andreas 1","hej 1","koll 1");
        //Quiz q2 = new Quiz("andreas 2","hej 2","koll 2");
        //this.quizzes.add(q1);
        //this.quizzes.add(q2);
        //////////

        File dir = getFilesDir();
        File file = new File (dir, "test");


        try{

        if(!file.exists())
           file.createNewFile();


        FileOutputStream fout = getApplicationContext().openFileOutput(file.getName(),Context.MODE_PRIVATE);
        ObjectOutputStream out = new ObjectOutputStream(fout);

        //writes searialized arraylist, containing quiz objects, to file
        out.writeObject(quizzes);

        out.close();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }
     private void readQuizzes(){

         try {
             File dir = getFilesDir();
             File file = new File(dir, "test");

             FileInputStream fin = getApplicationContext().openFileInput(file.getName());
             ObjectInputStream in = new ObjectInputStream(fin);

            //reads in an arraylist, containing quiz objects.
             quizzes = (ArrayList<Quiz>) in.readObject();

             ///////
             //Toast.makeText(getApplicationContext(),quizzes.get(0).getAuthor() + quizzes.get(1).getCourse(),Toast.LENGTH_LONG).show();
            // Toast.makeText(getApplicationContext(),quizzes.get(1).getName(),Toast.LENGTH_SHORT).show();
             ////////

             in.close();
         }
         catch(Exception ex){

         }
     }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GET_NEW_QUIZ_CODE) { // new quiz to be saved
                Quiz quizToBeSaved = (Quiz) data.getSerializableExtra("Quiz");
                this.quizzes.add(quizToBeSaved);
            }
        }



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


    // DEBUGGING PURPOSES ONLY
    private void deleteQuizzesFromDisk() {
        File dir = getFilesDir();
        File file = new File(dir, "test");
        boolean deleted = file.delete();
    }
}
