package se.bth.students.quizzard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by mihai on 2014-12-02.
 */
public class ListQuizzes extends Activity {
    private ArrayList<Quiz> quizzesL; // local quizzes
    private ArrayList<Quiz> quizzesS = new ArrayList<Quiz>(); // quizzes on server
    private ListView listViewL, listViewS, currListView;
    private ArrayList<Quiz> list = new ArrayList<Quiz>();
    private RadioButton buttonServer;
    private RadioButton buttonLocal;
    private Button buttonFireList;
    private ArrayAdapter<Quiz> adapter;
    private static final int LOCAL_LIST_ID = 1;
    private static final int SERVER_LIST_ID = 2;
    private static final int CURR_LIST_ID = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_quizzes);

        buttonServer = (RadioButton) findViewById(R.id.radioButtonServer);
        buttonServer.setOnClickListener(radioButtonServerListener);
        buttonLocal = (RadioButton) findViewById(R.id.radioButtonLocal);
        buttonLocal.setOnClickListener(radioButtonLocalListener);

        // default view is local
        buttonLocal.setChecked(true);
        buttonServer.setChecked(false);
        listViewL = new ListView (this);
        listViewS = new ListView(this);
        currListView = listViewL;
        listViewL.setId(LOCAL_LIST_ID);
        listViewS.setId(SERVER_LIST_ID);
        currListView.setId(CURR_LIST_ID);

        // add to layout
        LinearLayout container = (LinearLayout) findViewById(R.id.listview_container);
        container.removeAllViews();
        container.addView(currListView);

        //buttonFireList = (Button)findViewById(R.id.buttonFireList);
        //buttonFireList.setOnClickListener(buttonFireListListener);
        quizzesL = (ArrayList<Quiz>) getIntent().getSerializableExtra("quizzes");

        adapter = new ArrayAdapter<Quiz>(this, android.R.layout.simple_list_item_1, list);
        // adapter = new ItemView(this, quizzes);
        listViewL.setAdapter(adapter);
        registerForContextMenu(listViewL);
        listViewS.setAdapter(adapter);
        registerForContextMenu(listViewS);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView av, View v,
                                    int position, long id) {
                // get quiz obj
                Quiz quizToSend = null;
                // select right list of quizzes (local or server)
                ArrayList<Quiz> quizzes;
                RadioButton rb1 = (RadioButton) findViewById(R.id.radioButtonLocal);
                if (rb1.isChecked())
                    quizzes = quizzesL;
                else quizzes = quizzesS;

                if (quizzes != null)
                    quizToSend = quizzes.get(position);

                // start the DoQuiz activity
                if (quizToSend != null) {
                    Intent i = new Intent(getApplicationContext(), DoQuiz.class);
                    i.putExtra("Quiz", quizToSend);
                    startActivityForResult(i, position);
                }
            }
        };
        listViewL.setOnItemClickListener(itemClickListener);
        listViewS.setOnItemClickListener(itemClickListener);

        // populate Server quizzes with mock-up objects
        mockUpServer();


        updateUIListLocal();




        //Toast.makeText(getApplicationContext(), quizzes.get(0).getAuthor() + quizzes.get(1).getCourse(), Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(),quizzes.get(1).getName(),Toast.LENGTH_SHORT).show();
    }

    public void removeQuizFromLocalList() {

    }

    public OnClickListener radioButtonServerListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonLocal.setChecked(false);
            buttonServer.setChecked(true);
            TextView intro = (TextView) findViewById(R.id.ListQuizzesTextView);
            intro.setText("Quizzes online:");
            currListView = listViewS;

            LinearLayout container = (LinearLayout) findViewById(R.id.listview_container);
            container.removeAllViews();
            container.addView(currListView);

            updateUIListServer();
        }
    };
    public OnClickListener radioButtonLocalListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonLocal.setChecked(true);
            buttonServer.setChecked(false);
            TextView intro = (TextView) findViewById(R.id.ListQuizzesTextView);
            intro.setText("Quizzes on your phone:");
            currListView = listViewL;

            LinearLayout container = (LinearLayout) findViewById(R.id.listview_container);
            container.removeAllViews();
            container.addView(currListView);

            updateUIListLocal();
        }
    };

/*    public OnClickListener buttonFireListListener= new OnClickListener() {
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
    };*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Quiz updatedQuiz = quizzesL.get(requestCode);
            Bundle b = getIntent().getExtras();
            double result = b.getDouble("result");

            // check result for null
            // do something with result
        }

    }

    private void updateUIListLocal() {

        if (quizzesL != null) {
            // update UI with list of questions
            list.clear();
            for (Quiz q : quizzesL) {
                //String name = q.getName();
                list.add(q);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void updateUIListServer() {
        Toast.makeText(getApplicationContext(), "Coming soon :)", Toast.LENGTH_SHORT).show();


        if (quizzesS != null) {
            // update UI with list of questions
            list.clear();
            for (Quiz q : quizzesS) {
                //String name = q.getName();
                list.add(q);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void mockUpServer() {
        Quiz sQuiz1 = new Quiz("serv_mockup_quiz1", "", "");
        Question q1 = new Question("question1");
        q1.addAnswer("answer1", true);
        q1.addAnswer("answer2", false);
        sQuiz1.addQuestion(q1);

        Quiz sQuiz2 = new Quiz("serv_mockup_quiz2", "", "");
        Question q2 = new Question("question1 of quiz2");
        q2.addAnswer("answer1 quiz2", true);
        q2.addAnswer("answer2 quiz2", false);
        sQuiz2.addQuestion(q2);

        this.quizzesS.add(sQuiz1);
        this.quizzesS.add(sQuiz2);
    }

    // This method creates a context Menu for the quiz list
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;

        int lId = listViewL.getId();
        int sId = listViewS.getId();
        int currId = v.getId();

        if (v.getId() == this.listViewL.getId()) {
            String quizName = ((Quiz) adapter.getItem(aInfo.position)).toString();
            menu.setHeaderTitle("Options for " + quizName);
            menu.add(1, 0, 1, "Edit quiz"); // groupId, itemId, orderIndex, name
            menu.add(1, 1, 2, "Delete quiz");
        }

        if (v.getId() == this.listViewS.getId()) {
            String quizName = ((Quiz) adapter.getItem(aInfo.position)).toString();
            menu.setHeaderTitle("Options for " + quizName);
            menu.add(1, 0, 1, "Download quiz"); // groupId, itemId, orderIndex, name
            //menu.add(1, 1, 2, "Delete quiz")
        }

    }

    // This method called when user selects an Item in the Context menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // select right list of quizzes (local or server)
        ArrayList<Quiz> quizzes;
        RadioButton rb1 = (RadioButton) findViewById(R.id.radioButtonLocal);
        if (rb1.isChecked())
            quizzes = quizzesL;
        else quizzes = quizzesS;

        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemId = item.getItemId();

        ListView l = (ListView) aInfo.targetView.getParent();


        int currId = l.getId();
        int lId = listViewL.getId();
        int sId = listViewS.getId();

        if (l.getId() == this.listViewL.getId()) {  // Local ListView

            if (itemId == 1) { // delete quiz
                Toast.makeText(this, "You will delete " + quizzes.get(aInfo.position).toString(), Toast.LENGTH_SHORT).show();
            } else if (itemId == 0) { // edit quiz
                Toast.makeText(this, "You will edit " + quizzes.get(aInfo.position).toString(), Toast.LENGTH_SHORT).show();
            }
        }

        if (l.getId() == this.listViewS.getId()) { // ServerListView
            if (itemId == 0) { // download quiz
                Toast.makeText(this, "You will download " + quizzes.get(aInfo.position).toString(), Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    @Override protected void onDestroy() {
        if (quizzesL != null ) {
            saveQuizzesToDisk();
        }
        super.onDestroy();
    }

    private void saveQuizzesToDisk() { // just local quizzes are saved to disk


    }

}
