package com.example.jiumoon.bucketlist;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.ColorStateList;
import com.example.jiumoon.bucketlist.db.TaskDBHelper;
import com.example.jiumoon.bucketlist.db.TaskContract;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleCursorAdapter;

import java.util.List;

public class MainActivity extends ListActivity {
    private TaskDBHelper helper;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateUI();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem((MenuItem) findViewById(R.id.action_add_task));
                //onOptionsItemSelected((MenuItem)findViewById(R.id.action_add_task));
            }
        });
    }
    private void updateUI() {
        helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK},
                null,null,null,null,null);

        listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.task_view,
                cursor,
                new String[] { TaskContract.Columns.TASK},
                new int[] { R.id.taskTextView},
                0
        );
        this.setListAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void addItem (MenuItem item){
        Context context = getApplicationContext();
        CharSequence text = "hi";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        //toast.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Bucket List Item");
        builder.setMessage("Anything else on your list?");
        final EditText inputField = new EditText(this);
        builder.setView(inputField);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Put the item into the DB
                String task = inputField.getText().toString();
                Log.d("MainActivity", task);

                TaskDBHelper helper = new TaskDBHelper(MainActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.clear();
                values.put(TaskContract.Columns.TASK, task);

                db.insertWithOnConflict(TaskContract.TABLE, null, values,
                        SQLiteDatabase.CONFLICT_IGNORE);
                updateUI();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }


/*    public void onDoneButtonClick(View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
        String task = taskTextView.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                TaskContract.TABLE,
                TaskContract.Columns.TASK,
                task);


        helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateUI();
    }
*/
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt("unimplemented", 0);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
}