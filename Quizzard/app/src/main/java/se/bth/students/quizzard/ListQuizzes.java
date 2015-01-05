package se.bth.students.quizzard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by mihai on 2014-12-02.
 */
public class ListQuizzes extends Activity {
    private ArrayList<Quiz> quizzesL; // local quizzes
    private ArrayList<String> quizzesS = new ArrayList<String>(); // names of quizzes on server
    private ListView listViewL, listViewS, currListView;
    private ArrayList<Quiz> listL = new ArrayList<Quiz>();
    private ArrayList<String> listS = new ArrayList<String>();
    private RadioButton buttonServer;
    private RadioButton buttonLocal;
    //private Button buttonFireList;
    private ArrayAdapter<Quiz> adapterL;
    private ArrayAdapter<String> adapterS;
    private static final int LOCAL_LIST_ID = 1;
    private static final int SERVER_LIST_ID = 2;
    private static final int CURR_LIST_ID = 3;

    private static final int DO_CODE = 4;
    private static final int EDIT_CODE = 5;
    private boolean BACK_PRESSED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_quizzes);

        quizzesL = loadLocalQuizzesFromDisk();
        boolean serverView = false;

        if (savedInstanceState != null) {
            serverView = savedInstanceState.getBoolean("serverView");
        }

        buttonServer = (RadioButton) findViewById(R.id.radioButtonServer);
        buttonServer.setOnClickListener(radioButtonServerListener);
        buttonLocal = (RadioButton) findViewById(R.id.radioButtonLocal);
        buttonLocal.setOnClickListener(radioButtonLocalListener);

        listViewL = new ListView (this);
        listViewS = new ListView(this);
        listViewL.setDivider(new ColorDrawable(0x0000000));
        listViewL.setDividerHeight(2);
        if (serverView == false) {
            // default view is local
            buttonLocal.setChecked(true);
            buttonServer.setChecked(false);
            currListView = listViewL;
            Toast.makeText(getApplicationContext(), "Long tap for quiz options" , Toast.LENGTH_SHORT).show();

        }
        else {
            buttonLocal.setChecked(false);
            buttonServer.setChecked(true);
            currListView = listViewS;
        }

        listViewL.setId(LOCAL_LIST_ID);
        listViewS.setId(SERVER_LIST_ID);
        currListView.setId(CURR_LIST_ID);

        // add to layout
        LinearLayout container = (LinearLayout) findViewById(R.id.listview_container);
        container.removeAllViews();
        container.addView(currListView);

        //buttonFireList = (Button)findViewById(R.id.buttonFireList);
        //buttonFireList.setOnClickListener(buttonFireListListener);
        //quizzesL = (ArrayList<Quiz>) getIntent().getSerializableExtra("quizzes");

        adapterL = new ArrayAdapter<Quiz>(this, android.R.layout.simple_list_item_1, listL);
        adapterS = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listS);
        // adapter = new ItemView(this, quizzes);


        listViewL.setAdapter(adapterL);
        registerForContextMenu(listViewL);
        listViewS.setAdapter(adapterS);
        registerForContextMenu(listViewS);
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView av, View v,
                                    int position, long id) {

                if (av.getId() == listViewL.getId()) { // action for short click on item in Local List
                    // get quiz obj
                    Quiz quizToSend = null;

                    if (quizzesL != null)
                        quizToSend = quizzesL.get(position);

                    // start the DoQuiz activity
                    if (quizToSend != null) {
                        Intent i = new Intent(getApplicationContext(), DoQuiz.class);
                        i.putExtra("Quiz", quizToSend);
                        startActivityForResult(i, DO_CODE);
                    }
                }
                else if (av.getId() == listViewS.getId()) { // action for short click on item in Server List
                    Toast.makeText(getApplicationContext(), "You will download " + quizzesS.get(position), Toast.LENGTH_SHORT).show();
                }
            }
        };
        listViewL.setOnItemClickListener(itemClickListener);
        listViewS.setOnItemClickListener(itemClickListener);

        // populate Server quizzes with mock-up objects
        mockUpServer();

        if (serverView == false)
            updateUIListLocal();
        else updateUIListServer();
        sort();
        paintFavorite(listViewL);//funkar inte
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

    private void updateUIListLocal() {

        if (quizzesL != null && quizzesL.size() > 0) {
            // update UI with list of questions
            listL.clear();
            for (Quiz q : quizzesL) {
                //String name = q.getName();
                listL.add(q);
            }
            adapterL.notifyDataSetChanged();
        }
        else { // just clean up the list view
            LinearLayout l = (LinearLayout) findViewById(R.id.listview_container);
            l.removeAllViews();
        }
    }

    private void updateUIListServer() {
        Toast.makeText(getApplicationContext(), "Coming soon :)", Toast.LENGTH_SHORT).show();
        if (quizzesS != null && quizzesS.size() > 0) {
            // update UI with list of questions
            listS.clear();
            for (String s : quizzesS) {
                listS.add(s);
            }
            adapterS.notifyDataSetChanged();
        }
        else { // just clean up the list view
            LinearLayout l = (LinearLayout) findViewById(R.id.listview_container);
            l.removeAllViews();
        }
    }

    private void mockUpServer() {
        String sQuiz1 = "serv_mockup_quiz1";
        String sQuiz2 = "serv_mockup_quiz2";

        this.quizzesS.add(sQuiz1);
        this.quizzesS.add(sQuiz2);
    }

    @Override
    protected void onPause() {
        if (quizzesL != null && quizzesL.size() > 0) {
            saveLocalQuizzesToDisk();
        }
        super.onPause();

    }

    // Back button in Action bar
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        boolean serverView = false;
        RadioButton serverBtn = (RadioButton) findViewById(R.id.radioButtonServer);
        if (serverBtn.isChecked()) {
            serverView = true;
        }
        savedInstanceState.putBoolean("serverView", serverView);
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    public void onBackPressed() {
        this.BACK_PRESSED = true;
        Intent resultIntent = new Intent();
        resultIntent.putExtra("Quizzes", this.quizzesL);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        // send quizzes back to MainActivity for persistence (otherwise MainActivity will overwrite the file on disk with its own data)
        Intent resultIntent = new Intent();
        resultIntent.putExtra("Quizzes", this.quizzesL);
        setResult(Activity.RESULT_OK, resultIntent);
        if (this.BACK_PRESSED == true) { // only finish if Back button was pressed (don't finish on phone rotation)
            this.BACK_PRESSED = false;
            finish();
        }
        super.onDestroy();
    }

    private void saveLocalQuizzesToDisk() {
        File dir = getFilesDir();
        File file = new File (dir, MainActivity.FILE_QUIZZES);
        try{
            if(!file.exists())
                file.createNewFile();

            FileOutputStream fout = getApplicationContext().openFileOutput(file.getName(), Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            //writes serialized arraylist, containing quiz objects, to file
            out.writeObject(quizzesL);
            out.close();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private ArrayList<Quiz> loadLocalQuizzesFromDisk() {
        ArrayList<Quiz> ret = null;
        File dir = getFilesDir();
        File file = new File (dir, MainActivity.FILE_QUIZZES);

        try {

            FileInputStream fin = getApplicationContext().openFileInput(file.getName());
            ObjectInputStream in = new ObjectInputStream(fin);

            //reads in an arraylist, containing quiz objects.
            ret = (ArrayList<Quiz>) in.readObject();

            in.close();
        }
        catch(Exception ex){
        }

        return ret;
    }



    // This method creates a context Menu for the quiz list
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;

        if (v.getId() == this.listViewL.getId()) {
            String quizName = ((Quiz) adapterL.getItem(aInfo.position)).toString();
            menu.setHeaderTitle("Options for " + quizName);
            menu.add(1, 0, 1, "Edit quiz"); // groupId, itemId, orderIndex, name
            menu.add(1, 1, 2, "Delete quiz");
            menu.add(1, 2, 3, "Upload quiz");
            if(!quizzesL.get(aInfo.position).isFavorite()) {
                menu.add(1, 3, 4, "Favoring quiz");
            }
            else {
                menu.add(1, 3, 4, "Remove from favorites");
            }
        }

        if (v.getId() == this.listViewS.getId()) {
            String quizName = ((String) adapterS.getItem(aInfo.position));
            menu.setHeaderTitle("Options for " + quizName);
            menu.add(1, 0, 1, "Download quiz"); // groupId, itemId, orderIndex, name
        }
    }

    // This method called when user selects an Item in the Context menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemId = item.getItemId();
        ListView listV = (ListView) aInfo.targetView.getParent();
        boolean quizName = ((Quiz) adapterL.getItem(aInfo.position)).isFavorite();
        if (listV.getId() == this.listViewL.getId()) {  // Local ListView

            if (itemId == 1) { // delete quiz
                //Toast.makeText(this, "You will delete " + quizzesL.get(aInfo.position).toString(), Toast.LENGTH_SHORT).show();
                removeQuizFromLocalList(aInfo.position);
            } else if (itemId == 0) { // edit quiz
                //Toast.makeText(this, "You will edit " + quizzesL.get(aInfo.position).toString(), Toast.LENGTH_SHORT).show();
                editQuiz(aInfo.position);
            } else if (itemId == 2) { // upload quiz
                Toast.makeText(this, "You will upload " + quizzesL.get(aInfo.position).toString(), Toast.LENGTH_SHORT).show();
            } else if (itemId == 3){
                if(!quizzesL.get(aInfo.position).isFavorite()) {
                    makeAsFavorite(aInfo.position);
                    paintFavorite(listV);
                }
                else {
                    removeAsFavorite(aInfo.position, listV);
                }
            }
        }

        if (listV.getId() == this.listViewS.getId()) { // ServerListView
            if (itemId == 0) { // download quiz
                Toast.makeText(this, "You will download " + quizzesS.get(aInfo.position), Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }
    public View getViewByPosition(int pos, ListView listV) {
        final int firstListItemPos = listV.getFirstVisiblePosition();
        final int lastListItemPos = firstListItemPos + listV.getChildCount() - 1;

        if (pos < firstListItemPos || pos > lastListItemPos ) {
            return listV.getAdapter().getView(pos, null, listV);
        } else {
            final int childIndex = pos - firstListItemPos;
            return listV.getChildAt(childIndex);
        }
    }
    public void paintFavorite(ListView listV) {
        View rowView = null;
        for(int i=0; i<quizzesL.size(); i++) {
            if ((adapterL.getItem(i)).isFavorite()) {
                rowView = getViewByPosition(i, listV);
                rowView.setBackgroundResource(R.color.green);
            } else {
                rowView = getViewByPosition(i, listV);
                rowView.setBackgroundResource(R.color.white);
            }
        }
        //listViewL.setDivider(new ColorDrawable(0xff00000));
        //listViewL.setDividerHeight(1);
        updateUIListLocal();
        saveLocalQuizzesToDisk();
    }
    public void removeAsFavorite(int pos, ListView listV)
    {
        quizzesL.get(pos).setAsFavorite(false);
        sort();
        //paintFavorite(listV);
    }
    public void makeAsFavorite(int pos) {
        quizzesL.get(pos).setAsFavorite(true);
            //listViewL.setDivider(new ColorDrawable(0x99F10529));
            //listViewL.setDividerHeight(1);
            sort();
       // }
    }
    public void sort(){
        ArrayList<Quiz> tempQuizzes = quizzesL;
        for (int i = 0; i < tempQuizzes.size() - 1; ++i)
        {
            int minIndex = i;
            for (int j = i + 1; j < tempQuizzes.size(); ++j)
            {
                if (tempQuizzes.get(j).getName().compareTo(tempQuizzes.get(minIndex).getName()) < 0)
                {
                    minIndex = j;
                }
            }
            Quiz tempQuiz = tempQuizzes.get(i);
            tempQuizzes.set(i, tempQuizzes.get(minIndex));
            tempQuizzes.set(minIndex, tempQuiz);
        }
        sortWithFavorites(tempQuizzes);
    }
    public void sortWithFavorites(ArrayList<Quiz> sortedArray)
    {
        ArrayList<Quiz> sortedFavoriteQuizzes = new ArrayList<Quiz>();
        ArrayList<Quiz> sortedLocalQuizzes = new ArrayList<Quiz>();
        ArrayList<Quiz> finalSortedLocalQuizzes = new ArrayList<Quiz>();
        for(int i = 0; i<sortedArray.size(); i++)
        {
            if (quizzesL.get(i).isFavorite() == true) {
                sortedFavoriteQuizzes.add(sortedArray.get(i));
            } else {
                sortedLocalQuizzes.add(sortedArray.get(i));
            }
        }
        for(int i = 0; i<sortedFavoriteQuizzes.size(); i++)
        {
            finalSortedLocalQuizzes.add(sortedFavoriteQuizzes.get(i));
        }
        for(int i = 0; i<sortedLocalQuizzes.size(); i++)
        {
            finalSortedLocalQuizzes.add(sortedLocalQuizzes.get(i));
        }
        quizzesL = finalSortedLocalQuizzes;
        updateUIListLocal();
        saveLocalQuizzesToDisk();
    }
    public void removeQuizFromLocalList(int i) {
        final int idToBeDeleted = i;
        AlertDialog.Builder builder = new AlertDialog.Builder(ListQuizzes.this);
        final TextView msg = new TextView(this);
        builder.setView(msg);

        builder.setMessage("Really delete quiz?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        quizzesL.remove(idToBeDeleted);
                        //refresh list
                        sort();
                        paintFavorite(listViewL);
                        updateUIListLocal();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void editQuiz(int i) {
        Quiz quizToSend = quizzesL.get(i);
        // prepare new Intent
        Intent intent = new Intent(getApplicationContext(), EditQuiz.class);
        intent.putExtra("Quiz", quizToSend);
        startActivityForResult(intent, EDIT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == DO_CODE) {
                Quiz updatedQuiz = quizzesL.get(requestCode);
                Bundle b = getIntent().getExtras();
                double result = b.getDouble("result");
                // check result for null
                // do something with result
            }

            if (requestCode == EDIT_CODE) {
                // handle edited quiz
                // retrieve original quiz by name; replace with edited one
                Quiz editedQuiz = (Quiz) data.getSerializableExtra("Quiz");
                if (editedQuiz != null) {
                    int found = -1;
                    for (int i=0; found == -1 && i<quizzesL.size(); i++) {
                        if (quizzesL.get(i).getName().equals(editedQuiz.getName())) {
                            found = i;
                        }
                    }
                    if (found != -1) {
                        quizzesL.set(found, editedQuiz);
                    }
                }
            }
        }
    }


}
