package ru.spbau.mit.auTimetable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by equi on 01.12.15.
 *
 * @author Kravchenko Dima
 */
public class SettingsActivity extends ActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        Button doneButton = (Button) findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAndFinish(v);
            }
        });
    }

    private void sendAndFinish(View v) {
        Intent returnIntent = new Intent(getBaseContext(), MainActivity.class);

        EditText groupNumberEditText = (EditText) findViewById(R.id.group_number);
        EditText subgroupNumberEditText = (EditText) findViewById(R.id.subgroup_number);

        int groupNumber;
        int subgroupNumber;

        try {
            groupNumber = Integer.parseInt(groupNumberEditText.getText().toString());
            subgroupNumber = Integer.parseInt(subgroupNumberEditText.getText().toString());
        } catch (Exception e) {
            groupNumber = 0;
            subgroupNumber = 0;
        }

        returnIntent.putExtra("group_number", groupNumber);
        returnIntent.putExtra("subgroup_number", subgroupNumber);

        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
