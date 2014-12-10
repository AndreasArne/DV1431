package se.bth.students.quizzard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by mihai on 2014-12-02.
 */
public class ListQuizzes extends Activity {
    public ArrayList<Quiz> quizzesL; // local quizzes
    public ArrayList<Quiz> quizzesS = new ArrayList<Quiz>(); // quizzes on server
    public ListView listView;
    ArrayList<String> list = new ArrayList<String>();
    public RadioButton buttonServer;
    public RadioButton buttonLocal;
    public Button buttonFireList;
    public ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_quizzes);

        buttonServer = (RadioButton)findViewById(R.id.radioButtonServer);
        buttonServer.setOnClickListener(radioButtonServerListener);
        buttonLocal = (RadioButton)findViewById(R.id.radioButtonLocal);
        buttonLocal.setOnClickListener(radioButtonLocalListener);
        buttonFireList = (Button)findViewById(R.id.buttonFireList);
        buttonFireList.setOnClickListener(buttonFireListListener);
        quizzesL =(ArrayList<Quiz>) getIntent().getSerializableExtra("quizzes");
        listView = (ListView) findViewById(R.id.listViewListQuizzes);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        // adapter = new ItemView(this, quizzes);
        listView.setAdapter(adapter);


        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1,
                                    int position, long id) {
                // get quiz obj
                Quiz quizToSend = null;

                if (quizzesL != null)
                quizToSend = quizzesL.get(position);

                // start the DoQuiz activity
                if (quizToSend != null) {
                    Intent i = new Intent(getApplicationContext(), DoQuiz.class);
                    i.putExtra("Quiz", quizToSend);
                    startActivityForResult(i, position);
                }
            }
        };
        listView.setOnItemClickListener(itemClickListener);

        updateUIListLocal();

        // populate Server quizzes with mock-up objects
        mockUpServer();




        //Toast.makeText(getApplicationContext(), quizzes.get(0).getAuthor() + quizzes.get(1).getCourse(), Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(),quizzes.get(1).getName(),Toast.LENGTH_SHORT).show();
    }
    public void listLocalQuiz()
    {
        updateUIListLocal();
    }
    public void listServerQuiz()
    {
        updateUIListServer();
        //Toast.makeText(getApplicationContext(),"Coming soon :)",Toast.LENGTH_SHORT).show();
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

    private void updateUIListLocal() {
        if(quizzesL != null) {
            // update UI with list of questions
            list.clear();
            for (Quiz q : quizzesL) {
                String name = q.getName();
                list.add(name);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void updateUIListServer() {
        if(quizzesS != null) {
            // update UI with list of questions
            list.clear();
            for (Quiz q : quizzesS) {
                String name = q.getName();
                list.add(name);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void mockUpServer() {
        Quiz sQuiz1 = new Quiz ("serv_mockup_quiz1", "", "");
        Question q1 = new Question("question1");
        q1.addAnswer("answer1", true);
        q1.addAnswer("answer2", false);
        sQuiz1.addQuestion(q1);

        Quiz sQuiz2 = new Quiz ("serv_mockup_quiz2", "", "");
        Question q2 = new Question("question1 of quiz2");
        q2.addAnswer("answer1 quiz2", true);
        q2.addAnswer("answer2 quiz2", false);
        sQuiz2.addQuestion(q2);

        this.quizzesS.add(sQuiz1);
        this.quizzesS.add(sQuiz2);
    }


}
