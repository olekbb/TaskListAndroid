package example.projekt;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import example.projekt.db.TaskContract;
import example.projekt.db.TaskDbHelper;

public class EditActivity extends AppCompatActivity {
    private TaskDbHelper mHelper;
    private static final String TAG = "EditActivity";
    public static final String TASK_EXTRA_KEY = "task";
    private String taskName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mHelper = new TaskDbHelper(this);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                taskName= null;
            } else {
                taskName= extras.getString(TASK_EXTRA_KEY);
            }
        } else {
            taskName= (String) savedInstanceState.getSerializable(TASK_EXTRA_KEY);
        }
        final EditText taskEditText = (EditText) findViewById(R.id.edit_text);
        taskEditText.setText(taskName);

        Button okButton = (Button) findViewById(R.id.ok_button);
        if (okButton != null) {
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String task = String.valueOf(taskEditText.getText());

                    if (task != null && !task.equals("")) {
                        deleteTask();
                        SQLiteDatabase db = mHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                                null,
                                values,
                                SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();
                    }
                    finish();
                }
            });
        }

        Button cancelButton = (Button) findViewById(R.id.cancel_button);

        if (cancelButton != null) {
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    public void deleteTask() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{taskName});
        db.close();
    }
}
