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
import android.widget.RadioButton;
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
    static public final int GET_UPDATED_QUIZZES = 2;
    static public final String FILE_QUIZZES = "quizzes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        quizzes = new ArrayList<Quiz>();

        if (savedInstanceState == null) {
            readQuizzes(); // only read from disk at first creation
            // load some pre-made quizzes (Sports, Languages) to try the app without needing to create own quizzes
        }
        else {
            ArrayList<Quiz> tempQuizzes = (ArrayList<Quiz>) savedInstanceState.getSerializable("quizzes");
            if (tempQuizzes != null && tempQuizzes.size() > 0)
                this.quizzes = tempQuizzes;
        }

        if (quizzes.size() == 0)
            loadStartupQuizzes(); // comment this out to skip loading pre-made quizzes

        //for shake event
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeListener();
        last = Calendar.getInstance();
        
        mSensorListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            public void onShake() {
                now = Calendar.getInstance();
                if( (now.getTimeInMillis() - last.getTimeInMillis()) >750) {
                    last = now;
                    if(quizzes.size() == 1){
                        Intent i = new Intent(getApplicationContext(), DoQuiz.class);
                        i.putExtra("Quiz", quizzes.get(0));
                        Toast.makeText(getApplicationContext(), "Random quiz started!", Toast.LENGTH_SHORT).show();
                        startActivity(i);

                    }
                    else if(quizzes.size() > 1){
                        Random r = new Random();
                        int index = r.nextInt(quizzes.size()) ;
                        Intent i = new Intent(getApplicationContext(), DoQuiz.class);
                        i.putExtra("Quiz", quizzes.get(index));
                        Toast.makeText(getApplicationContext(), "Random quiz started!", Toast.LENGTH_SHORT).show();
                        startActivity(i);
                    }
                    else Toast.makeText(getApplicationContext(), "No quizzes here, why don't you create one?", Toast.LENGTH_SHORT).show();
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
                //listInt.putExtra("quizzes",quizzes);
                startActivityForResult(listInt, GET_UPDATED_QUIZZES);
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
                //createInt.putExtra("quizzes",quizzes);
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

        Toast.makeText(getApplicationContext(), "Shake phone to start random quiz!", Toast.LENGTH_LONG).show();
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


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("firstLoad", false);
        savedInstanceState.putSerializable("quizzes", this.quizzes);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void writeQuizzes(){
        //////////
        //Quiz q1 = new Quiz("andreas 1","hej 1","koll 1");
        //Quiz q2 = new Quiz("andreas 2","hej 2","koll 2");
        //this.quizzes.add(q1);
        //this.quizzes.add(q2);
        //////////

        File dir = getFilesDir();
        File file = new File (dir, FILE_QUIZZES);


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
             File file = new File(dir, FILE_QUIZZES);

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

            if (requestCode == GET_UPDATED_QUIZZES) { // quizzes object may have been changed by ListQuizzes, gets updated
               ArrayList<Quiz> updated_quizzes = (ArrayList<Quiz>) data.getSerializableExtra("Quizzes");
               // update quizzes
                this.quizzes = updated_quizzes;
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
        File file = new File(dir, FILE_QUIZZES);
        boolean deleted = file.delete();
    }

    // start-up quizzes (to be able to test the app without first manually creating quizzes)
    private void loadStartupQuizzes() {
        ArrayList<Quiz> startQuizzes = null;
        startQuizzes = getStartQuizzes();
        if (startQuizzes != null) {
            this.quizzes = startQuizzes;
        }

    }

    private ArrayList<Quiz> getStartQuizzes() {
        ArrayList<Quiz> ret = new ArrayList<Quiz>();
        Quiz q1 = new Quiz("Sports", "DV1234", "A sports fan");
        ArrayList<Question> questions = new ArrayList<Question>();
        Question question = new Question("How many players does a soccer team have on field?");
        ArrayList<Answer> answers = new ArrayList<Answer>();
        Answer a1 = new Answer("15", false);
        Answer a2 = new Answer("7", false);
        Answer a3 = new Answer("11", true);
        Answer a4 = new Answer ("10", false);
        question.setQuestionType(Question.UNIQUE);
        answers.add(a1);
        answers.add(a2);
        answers.add(a3);
        answers.add(a4);
        question.attachAnswers(answers);
        q1.addQuestion(question);

        question = new Question("Which player has scored most goals ever in Champions League?");
        answers = new ArrayList<Answer>();
        a1 = new Answer("Diego Maradona", false);
        a2 = new Answer("Lionel Messi", true);
        a3 = new Answer("Cristiano Ronaldo", false);
        a4 = new Answer("Steven Gerrard", false);
        question.setQuestionType(Question.UNIQUE);
        answers.add(a1);
        answers.add(a2);
        answers.add(a3);
        answers.add(a4);
        question.attachAnswers(answers);
        q1.addQuestion(question);

        question = new Question("Which of the following countries have organized the World Cup at least once?");
        answers = new ArrayList<Answer>();
        a1 = new Answer("Portugal", false);
        a2 = new Answer("Mexico", true);
        a3 = new Answer("Peru", false);
        a4 = new Answer("Brazil", true);
        Answer a5 = new Answer("Italy", true);
        question.setQuestionType(Question.MULTIPLE);
        answers.add(a1);
        answers.add(a2);
        answers.add(a3);
        answers.add(a4);
        answers.add(a5);
        question.attachAnswers(answers);
        q1.addQuestion(question);

        question = new Question("Which Romanian was the first female gymnast to ever get maximum grade 10 at the Olympic Games, in 1976?");
        answers = new ArrayList<Answer>();
        a1 = new Answer("Simona Halep", false);
        a2 = new Answer("Nadia Comaneci", true);
        a3 = new Answer("Monica Seles", false);
        a4 = new Answer("Elena Ceausescu", false);
        question.setQuestionType(Question.UNIQUE);
        answers.add(a1);
        answers.add(a2);
        answers.add(a3);
        answers.add(a4);
        question.attachAnswers(answers);
        q1.addQuestion(question);

        ret.add(q1);

        q1 = new Quiz("Languages", "DV1235", "A language-interested person");
        questions = new ArrayList<Question>();
        question = new Question("Which two languages in the list have common roots?");
        answers = new ArrayList<Answer>();
        a1 = new Answer("English", false);
        a2 = new Answer("Russian", false);
        a3 = new Answer("Estonian", true);
        a4 = new Answer ("Italian", false);
        a5 = new Answer("Hungarian", true);
        question.setQuestionType(Question.MULTIPLE);
        answers.add(a1);
        answers.add(a2);
        answers.add(a3);
        answers.add(a4);
        answers.add(a5);
        question.attachAnswers(answers);
        q1.addQuestion(question);

        question = new Question("What does the Latin expression 'Veni, vidi, vici' mean?");
        answers = new ArrayList<Answer>();
        a1 = new Answer("Came, seen, won", true);
        a2 = new Answer("Ate, drank, slept", false);
        a3 = new Answer("Came, won, seen", false);
        a4 = new Answer("Drank, ate, slept", false);
        question.setQuestionType(Question.UNIQUE);
        answers.add(a1);
        answers.add(a2);
        answers.add(a3);
        answers.add(a4);
        question.attachAnswers(answers);
        q1.addQuestion(question);

        question = new Question("What language is spoken in the Spanish city of Barcelona?");
        answers = new ArrayList<Answer>();
        a1 = new Answer("Spanish", false);
        a2 = new Answer("Gallic", false);
        a3 = new Answer("Catalan", true);
        a4 = new Answer("Arabic", false);

        question.setQuestionType(Question.UNIQUE);
        answers.add(a1);
        answers.add(a2);
        answers.add(a3);
        answers.add(a4);
        question.attachAnswers(answers);
        q1.addQuestion(question);

        question = new Question("In which two countries are dialects of the same language spoken by the majority of the population?");
        answers = new ArrayList<Answer>();
        a1 = new Answer("France", false);
        a2 = new Answer("Saudi Arabia", false);
        a3 = new Answer("Portugal", true);
        a4 = new Answer("India", false);
        a5 = new Answer("Brazil", true);
        question.setQuestionType(Question.UNIQUE);
        answers.add(a1);
        answers.add(a2);
        answers.add(a3);
        answers.add(a4);
        answers.add(a5);
        question.attachAnswers(answers);
        q1.addQuestion(question);

        ret.add(q1);

        return ret;
    }
}
