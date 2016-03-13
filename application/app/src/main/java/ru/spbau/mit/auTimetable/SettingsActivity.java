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
    private GlobalGroupId globalGroupId = new GlobalGroupId(0, 0);

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
        globalGroupId.group = getIntent().getIntExtra("group_number", 0);
        globalGroupId.subgroup = getIntent().getIntExtra("subgroup_number", 0);

        groupNumberEditText = (EditText) findViewById(R.id.group_number);
        subgroupNumberEditText = (EditText) findViewById(R.id.subgroup_number);

        groupNumberEditText.setText(Integer.toString(globalGroupId.group));
        subgroupNumberEditText.setText(Integer.toString(globalGroupId.subgroup));
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

        returnIntent.putExtra("group_number", globalGroupId.group);
        returnIntent.putExtra("subgroup_number", globalGroupId.subgroup);

        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private void getGroupAndSubgroupNumbers() {
        try {
            globalGroupId.group = Integer.parseInt(groupNumberEditText.getText().toString());
            globalGroupId.subgroup = Integer.parseInt(subgroupNumberEditText.getText().toString());
        } catch (Exception e) {
            globalGroupId.group = 0;
            globalGroupId.subgroup = 0;
        }
    }
}
