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

public class AddTaskActivity extends AppCompatActivity {
    private TaskDbHelper mHelper;
    private static final String TAG = "AddTaskActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mHelper = new TaskDbHelper(this);

        final EditText taskEditText = (EditText) findViewById(R.id.edit_text);

        Button okButton = (Button) findViewById(R.id.ok_button);
        if (okButton != null) {
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String task = String.valueOf(taskEditText.getText());

                    if (task != null && !task.equals("")) {
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
}
