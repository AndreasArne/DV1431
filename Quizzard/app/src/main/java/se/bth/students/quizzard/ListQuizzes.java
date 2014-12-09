package se.bth.students.quizzard;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by mihai on 2014-12-02.
 */
public class ListQuizzes extends Activity {
    public ArrayList<Quiz> quizzes;
    public ListView listView;
    public RadioButton buttonServer;
    public RadioButton buttonLocal;
    public Button buttonFireList;
    public ItemView adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_quizzes);
        listView = (ListView) findViewById(R.id.listViewListQuizzes);
        buttonServer = (RadioButton)findViewById(R.id.radioButtonServer);
        buttonServer.setOnClickListener(radioButtonServerListener);
        buttonLocal = (RadioButton)findViewById(R.id.radioButtonLocal);
        buttonLocal.setOnClickListener(radioButtonLocalListener);
        buttonFireList = (Button)findViewById(R.id.buttonFireList);
        buttonFireList.setOnClickListener(buttonFireListListener);

        listLocalQuiz();
        quizzes =(ArrayList<Quiz>) getIntent().getSerializableExtra("quizzes");
        Toast.makeText(getApplicationContext(), quizzes.get(0).getAuthor() + quizzes.get(1).getCourse(), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),quizzes.get(1).getName(),Toast.LENGTH_SHORT).show();
    }
    public void listLocalQuiz()
    {
        adapter = new ItemView(this, quizzes);
        listView.setAdapter(adapter);
    }
    public void listServerQuiz()
    {

    }
    public void removeQuizFromLocalList()
    {

    }
    public OnClickListener radioButtonServerListener= new OnClickListener() {
        @Override
        public void onClick(View v) {
                buttonLocal.setChecked(false);
                buttonServer.setChecked(true);
        }
    };
    public OnClickListener radioButtonLocalListener= new OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonLocal.setChecked(true);
            buttonServer.setChecked(false);
        }
    };
    public OnClickListener buttonFireListListener= new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(buttonLocal.isChecked())
            {
                listLocalQuiz();
            }
            else if(buttonServer.isChecked())
            {
                listServerQuiz();
            }

        }
    };
}
