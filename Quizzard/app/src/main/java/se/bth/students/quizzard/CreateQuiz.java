package se.bth.students.quizzard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    boolean NO_SAVE_TO_DISK = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_quiz);

        // restore saved state
        loadQuizFromDisk();

        // restore state of text fields
        if (savedInstanceState != null) {
            EditText quiz_name_txt = (EditText) findViewById(R.id.quiz_name_txt);
            EditText course_txt = (EditText) findViewById(R.id.course_txt);
            EditText author_txt = (EditText) findViewById(R.id.author_txt);

            String quiz_name = savedInstanceState.getString("quiz_name");
            String course = savedInstanceState.getString("course");
            String author = savedInstanceState.getString("author");

            quiz_name_txt.setText(quiz_name);
            course_txt.setText(course);
            author_txt.setText(author);
        }

        q_list = (ListView) findViewById(R.id.questions_list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        q_list.setAdapter(adapter);
        updateUIList();

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1,
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
            }
        };
        q_list.setOnItemClickListener(itemClickListener);

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
                    if (quiz == null || quiz.getName().equals("N/A")) // make new quiz obj
                        quiz = new Quiz(nameQuiz, courseName, authorName);


                    Intent i = new Intent(getApplicationContext(), AddQuestion.class);
                    //i.putExtra("Quiz", quiz);
                    // find out index of next question
                    int index = quiz.getQuestions().size();  //next free pos in questions array
                    startActivityForResult(i, index);
                }
                else Toast.makeText(getBaseContext(), "Your quiz must have a name" , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveQuiz(View v) {
        EditText quiz_name_txt = (EditText) findViewById(R.id.quiz_name_txt);
        String quizName = quiz_name_txt.getText().toString();

        if (!quizName.equals("") && !quizName.equals("N/A")) {
            if (quiz == null) {
                EditText course_txt = (EditText) findViewById(R.id.course_txt);
                String course = course_txt.getText().toString();
                EditText author_txt = (EditText) findViewById(R.id.author_txt);
                String author = author_txt.getText().toString();
                quiz = new Quiz(quizName, course, author);
            }
            Intent resultIntent = new Intent();
            resultIntent.putExtra("Quiz", this.quiz);
            setResult(Activity.RESULT_OK, resultIntent);
            NO_SAVE_TO_DISK = true;
            finish();
        }
        else Toast.makeText(getBaseContext(), "Quiz needs at least a title." , Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int index = quiz.getQuestions().size();
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == index) { // a new question
                if (resultCode == RESULT_OK) {
                    Log.i("mytag", "IM BACK!");
                    Question newQuestion = (Question) data.getSerializableExtra("Question");
                    this.quiz.addQuestion(newQuestion);
                    //Log.i("mytag", "the quiz now has: " + this.quiz.getQuestions().size() + " questions");

                    // metadata on quiz can't be changed from now on
                    EditText quizNameTxt = (EditText) findViewById(R.id.quiz_name_txt);
                    quizNameTxt.setFocusable(false);
                    quizNameTxt.setFocusableInTouchMode(false);
                    quizNameTxt.setClickable(false);

                    updateUIList();
                    Toast.makeText(getBaseContext(), "New question saved" , Toast.LENGTH_SHORT).show();

                }
                else if (resultCode == RESULT_FIRST_USER) { // save & add new question button was pressed
                    // store the result
                    Question newQuestion = (Question) data.getSerializableExtra("Question");
                    quiz.addQuestion(newQuestion);

                    updateUIList();
                    Toast.makeText(getBaseContext(), "New question saved" , Toast.LENGTH_SHORT).show();

                    // start new AddQuestion activity
                    Button addQuestion_btn = (Button) findViewById(R.id.add_question_btn);
                    addQuestion_btn.performClick(); // simulates click of button
                }
            } else if (requestCode >= 0 && requestCode < quiz.getQuestions().size()) { // an edited question was sent back
                Question question_edited = (Question) data.getSerializableExtra("Question");
                if (resultCode != Activity.RESULT_FIRST_USER+1) { // question to be edited
                    ArrayList<Question> questions = quiz.getQuestions();
                    questions.set(requestCode, question_edited);
                    quiz.attachQuestions(questions);
                    updateUIList();
                    Toast.makeText(getBaseContext(), "Question was edited successfully" , Toast.LENGTH_SHORT).show();

                }
                else { // question to be deleted

                    // delete question
                    ArrayList<Question> questions = quiz.getQuestions();
                    questions.remove(requestCode);
                    quiz.attachQuestions(questions);

                    // update UI with list of questions
                    updateUIList();
                    Toast.makeText(getBaseContext(), "Question was deleted successfully" , Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onResume() {
        TextView questions_tv = (TextView) findViewById(R.id.questions_list_tv);
        if (list.size() == 0) {
            questions_tv.setVisibility(View.INVISIBLE);
        }
        else questions_tv.setVisibility(View.VISIBLE);
        super.onResume();
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateQuiz.this);
        final TextView msg = new TextView(this);
        builder.setView(msg);

        builder.setMessage("Leave page? (Quiz will not be saved)")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NO_SAVE_TO_DISK = true;
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        AlertDialog alertDialog = builder.create();

        EditText quiz_name_txt = (EditText) findViewById(R.id.quiz_name_txt);
        String quizName = quiz_name_txt.getText().toString();
        if (quizName != null && !quizName.equals("") && !quizName.equals("N/A"))
            alertDialog.show();
        else {
            NO_SAVE_TO_DISK = true;
            finish();
        }
    }

    // Back button in Action bar
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }

    @Override protected void onDestroy() {
        if (!NO_SAVE_TO_DISK && quiz != null && !quiz.getName().equals("N/A")) {
            saveQuizToDisk();
        }
        else {
            boolean delete = deleteBackupFile();
            NO_SAVE_TO_DISK = false;
        }
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        String quiz_name = ((EditText) findViewById(R.id.quiz_name_txt)).getText().toString();
        String course = ((EditText) findViewById(R.id.course_txt)).getText().toString();
        String author = ((EditText) findViewById(R.id.author_txt)).getText().toString();
        savedInstanceState.putString("quiz_name", quiz_name);
        savedInstanceState.putString("course", course);
        savedInstanceState.putString("author", author);
        super.onSaveInstanceState(savedInstanceState);

    }

    private void updateUIList() {
        if(quiz != null) {
            // update UI with list of questions
            list.clear();
            ArrayList<Question> questions = quiz.getQuestions();
            for (Question q : questions) {
                String name = q.getQuestionText();
                list.add(name);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void saveQuizToDisk() {
        File dir = getFilesDir();
        File file = new File (dir, "create_quiz");
        try{

            if(!file.exists())
                file.createNewFile();

            FileOutputStream fout = getApplicationContext().openFileOutput(file.getName(), Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fout);

            //writes searialized arraylist, containing quiz objects, to file
            out.writeObject(quiz);

            out.close();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }

    private void loadQuizFromDisk() {
        File dir = getFilesDir();
        File file = new File (dir, "create_quiz");

        try {

            FileInputStream fin = getApplicationContext().openFileInput(file.getName());
            ObjectInputStream in = new ObjectInputStream(fin);

            //reads in an arraylist, containing quiz objects.
            quiz = (Quiz) in.readObject();

            in.close();
        }
        catch(Exception ex){
        }

    }

    private boolean deleteBackupFile() {
        File dir = getFilesDir();
        File file = new File(dir, "create_quiz");
        boolean deleted = file.delete();
        return deleted;

    }

}
