package se.bth.students.quizzard;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;

/**
 * Created by mihai on 2014-12-02.
 */
public class CreateQuiz extends Activity {

    Quiz quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_quiz);

        //button listener
        Button addQ_btn = (Button) findViewById(R.id.add_question_btn);
        addQ_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText name = (EditText) findViewById(R.id.quiz_name_txt);
                String nameQuiz = name.getText().toString();
                EditText course = (EditText) findViewById(R.id.course_txt);
                String courseName = course.getText().toString();
                EditText author = (EditText) findViewById(R.id.author_txt);
                String authorName = author.getText().toString();
                quiz = new Quiz(nameQuiz,courseName,authorName);

                Intent i = new Intent(getApplicationContext(),AddQuestion.class);
                i.putExtra("Quiz", quiz);
                startActivity(i);
            }
        });

        //button listener
        Button saveQ_btn = (Button) findViewById(R.id.save_quiz_btn);
       saveQ_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


//                Intent createInt = new Intent(getApplicationContext(),AddQuestion.class);
//                startActivity(createInt);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
     Log.i("mytag", "resume CreateQuiz: quiz has " + quiz.getQuestions().size() + " questions. quiz name is: "+quiz.getName());

    }
}
