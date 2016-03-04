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
@SuppressWarnings("deprecation")
public class SettingsActivity extends ActionBarActivity {
    private int groupNumber;
    private int subgroupNumber;

    private EditText groupNumberEditText;
    private EditText subgroupNumberEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        setTextNumbers();

        setUpDoneButton();
    }

    private void setTextNumbers() {
        groupNumber = getIntent().getIntExtra("group_number", 0);
        subgroupNumber = getIntent().getIntExtra("subgroup_number", 0);

        groupNumberEditText = (EditText) findViewById(R.id.group_number);
        subgroupNumberEditText = (EditText) findViewById(R.id.subgroup_number);

        groupNumberEditText.setText(Integer.toString(groupNumber));
        subgroupNumberEditText.setText(Integer.toString(subgroupNumber));
    }

    private void setUpDoneButton() {
        Button doneButton = (Button) findViewById(R.id.done_button);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAndFinish();
            }
        });
    }

    private void sendAndFinish() {
        Intent returnIntent = new Intent(getBaseContext(), MainActivity.class);

        getGroupAndSubgroupNumbers();

        returnIntent.putExtra("group_number", groupNumber);
        returnIntent.putExtra("subgroup_number", subgroupNumber);

        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private void getGroupAndSubgroupNumbers() {
        try {
            groupNumber = Integer.parseInt(groupNumberEditText.getText().toString());
            subgroupNumber = Integer.parseInt(subgroupNumberEditText.getText().toString());
        } catch (Exception e) {
            groupNumber = 0;
            subgroupNumber = 0;
        }
    }
}
