package ru.spbau.mit.auTimetable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;


public class TimetableFragment extends Fragment {
    private ListView mDayTimetable;
    private LinearLayout mWholeScreen;
    private TextView currentDayName;
    private TextView currentDate;

    private Activity activity;

    private GlobalGroupId globalGroupId = new GlobalGroupId(0, 0);

    ProgressDialog progressDialog;

    private Date date;

    private boolean wasSetUp = false;

    private XMLTimetableParser parser = null;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public TimetableFragment() {
        globalGroupId.group = 0;
        globalGroupId.subgroup = 0;
        date = new Date();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUp(inflater, container);

        Bundle args = getArguments();
        globalGroupId.group = args.getInt("group_number", 0);
        globalGroupId.subgroup = args.getInt("subgroup_number", 0);

        activity = getActivity();
        progressDialog = ProgressDialog.show(getActivity(), "Loading",
                "Loading timetable, please wait...", false, false);
        new AsyncCreateTimetable().execute();

        return mWholeScreen;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private void setUp(LayoutInflater inflater, ViewGroup container) {
        if (wasSetUp) {
            return;
        }

        mWholeScreen = (LinearLayout) inflater.inflate(
                R.layout.fragment_timetable, container, false);
        mDayTimetable = (ListView) mWholeScreen.findViewById(R.id.day_timetable);

        setUpDayInfo();
        setUpButtons();

        wasSetUp = true;
    }

    private void setUpDayInfo() {
        View DayInformation = mWholeScreen.findViewById(R.id.day_information);
        currentDayName = (TextView) DayInformation.findViewById(R.id.day);
        currentDate = (TextView) DayInformation.findViewById(R.id.date);
    }

    private void setUpButtons() {
        Button leftButton = (Button) mWholeScreen.findViewById(R.id.left_button);
        Button rightButton = (Button) mWholeScreen.findViewById(R.id.right_button);

        leftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    date.previousDay();
                    currentDayName.setText(date.dayName);
                    currentDate.setText(date.day + "." + date.month + "." + date.year);
                    updateDayTimetable();
                }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    date.nextDay();
                    currentDayName.setText(date.dayName);
                    currentDate.setText(date.day + "." + date.month + "." + date.year);
                    updateDayTimetable();
                }
        });
    }

    private void setCurDateAndUpdateTimetable() {
        date.update();
        currentDayName.setText(date.dayName);
        currentDate.setText(date.day + "." + date.month + "." + date.year);

        updateDayTimetable();
    }

    private void updateDayTimetable() {
        WeekInfo weekInfo = parser.getWeek(date);
        DayInfo dayInfo = weekInfo.getDay(date.dayNameEn);

        ClassInfo classes[] = new ClassInfo[dayInfo.classList.size()];
        classes = dayInfo.classList.toArray(classes);

        mDayTimetable.setAdapter(new TimetableAdapter(
                getActionBar().getThemedContext(),
                classes,
                date
        ));
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) activity).getSupportActionBar();
    }

    private class AsyncCreateTimetable extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            TimetableFragment.this.progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            StringProvider stringProvider = new TimetableStringProvider();
            FileProvider fileProvider = new FileProvider(activity, stringProvider, globalGroupId);

            parser = new XMLTimetableParser(fileProvider.provideFile(), globalGroupId);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            activity.runOnUiThread(new Runnable() {
                public void run() {
                    setCurDateAndUpdateTimetable();
                }
            });
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values[0]);
        }
    }
}
