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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mihai on 2014-12-02.
 */
public class CreateQuiz extends Activity {

    Quiz quiz;
    //static public final int NEW_QUESTION_CODE = 1;
    //static public final int EDITED_QUESTION_CODE = 2;
    ListView q_list;
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_quiz);
        q_list = (ListView) findViewById(R.id.questions_list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        q_list.setAdapter(adapter);

        AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView arg0, View arg1,
                                           int arg2, long arg3) {
              //  Toast.makeText(getBaseContext(), "Long Clicked:" + adapter.getItem(arg2) , Toast.LENGTH_SHORT).show();
                // get question obj
                Question questionToSend = null;
                String question_txt = adapter.getItem(arg2);
                int found = -1;
                ArrayList<Question> questions = quiz.getQuestions();
                if (questions != null) {
                    for (int i = 0; found == -1 && i < questions.size(); i++) {
                        if (question_txt.equals(questions.get(i).getQuestionText())) {
                            found = i;
                        }
                    }
                }
                if (found != -1 && questions != null) {
                    questionToSend = questions.get(found);
                }

                // start the EditQuestion activity
                if (questionToSend != null) {
                    Intent i = new Intent(getApplicationContext(), EditQuestion.class);
                    i.putExtra("Question", questionToSend);
                    startActivityForResult(i, found);
                }


                return false;
            }
        };
        q_list.setOnItemLongClickListener(itemLongClickListener);

        //button listener
        Button addQ_btn = (Button) findViewById(R.id.add_question_btn);
        addQ_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText name = (EditText) findViewById(R.id.quiz_name_txt);
                String nameQuiz = name.getText().toString();

                if (nameQuiz != null && !nameQuiz.equals("")) {

                    EditText course = (EditText) findViewById(R.id.course_txt);
                    String courseName = course.getText().toString();
                    EditText author = (EditText) findViewById(R.id.author_txt);
                    String authorName = author.getText().toString();
                    if (quiz == null)
                        quiz = new Quiz(nameQuiz, courseName, authorName);


                    Intent i = new Intent(getApplicationContext(), AddQuestion.class);
                    i.putExtra("Quiz", quiz);
                    // find out index of next question
                    int index = quiz.getQuestions().size();
                    startActivityForResult(i, index);
                }
                else Toast.makeText(getBaseContext(), "Your quiz must have a name" , Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int index = quiz.getQuestions().size();
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == index) { // if index is next one after size of questions, it means it is a new question
                if (resultCode == RESULT_OK) {
                    Log.i("mytag", "IM BACK!");
                    this.quiz = (Quiz) data.getSerializableExtra("Quiz");
                    Log.i("mytag", "the quiz now has: " + this.quiz.getQuestions().size() + " questions");

                    // metadata on quiz can't be changed from now on
                    EditText quizNameTxt = (EditText) findViewById(R.id.quiz_name_txt);
                    quizNameTxt.setFocusable(false);
                    quizNameTxt.setFocusableInTouchMode(false);
                    quizNameTxt.setClickable(false);

                    // update UI with list of questions
                    list.clear();
                    ArrayList<Question> questions = quiz.getQuestions();
                    for (Question q : questions) {
                        String name = q.getQuestionText();
                        list.add(name);
                    }
                    adapter.notifyDataSetChanged();

                    Toast.makeText(getBaseContext(), "New question saved" , Toast.LENGTH_SHORT).show();

                }
                else if (resultCode == RESULT_FIRST_USER) { // save & add new question button was pressed
                    // store the result
                    this.quiz = (Quiz) data.getSerializableExtra("Quiz");

                    // update UI with list of questions
                    list.clear();
                    ArrayList<Question> questions = quiz.getQuestions();
                    for (Question q : questions) {
                        String name = q.getQuestionText();
                        list.add(name);
                    }
                    adapter.notifyDataSetChanged();

                    Toast.makeText(getBaseContext(), "New question saved" , Toast.LENGTH_SHORT).show();

                    // start new AddQuestion activity
                    Button addQuestion_btn = (Button) findViewById(R.id.add_question_btn);
                    addQuestion_btn.performClick(); // simulates click of button
                }
            } else if (requestCode >= 0 && requestCode < quiz.getQuestions().size()) { // an edited question was sent back
                Question question_edited = (Question) data.getSerializableExtra("Question");
                if (!question_edited.getQuestionText().equals("N/A")) {
                    ArrayList<Question> questions = quiz.getQuestions();
                    questions.set(requestCode, question_edited);
                    quiz.attachQuestions(questions);


                    // update UI with list of questions
                    list.clear();
                    ArrayList<Question> questions2 = quiz.getQuestions();
                    for (Question q : questions2) {
                        String name = q.getQuestionText();
                        list.add(name);
                    }
                    adapter.notifyDataSetChanged();

                    Toast.makeText(getBaseContext(), "Question was edited successfully" , Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("mytag", "resume CreateQuiz: quiz has " + quiz.getQuestions().size() + " questions. quiz name is: "+quiz.getName());





    }
}
