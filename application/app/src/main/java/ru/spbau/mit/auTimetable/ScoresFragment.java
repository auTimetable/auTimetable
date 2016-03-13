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

import java.util.List;


public class ScoresFragment extends Fragment {
    private LinearLayout mWholeScreen;
    private ListView mLinksList;

    private Activity activity;

    private GlobalGroupId globalGroupId = new GlobalGroupId(0, 0);

    ProgressDialog progressDialog;

    private boolean wasSetUp = false;

    private ScoresParser parser = null;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public ScoresFragment() {
        globalGroupId.group = 0;
        globalGroupId.subgroup = 0;
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
                "Loading links, please wait...", false, false);
        new AsyncCreateLinks().execute();

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
                R.layout.fragment_scores, container, false);

        mLinksList = (ListView) mWholeScreen.findViewById(R.id.links_list);

        wasSetUp = true;
    }

    private void updateList() {
        List<ScoresLink> scoresLinksList = parser.getLinks();

        ScoresLink scoresLinks[] = new ScoresLink[scoresLinksList.size()];
        scoresLinks = scoresLinksList.toArray(scoresLinks);

        mLinksList.setAdapter(new ScoresAdapter(
                getActionBar().getThemedContext(),
                scoresLinks
        ));
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) activity).getSupportActionBar();
    }

    private class AsyncCreateLinks extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            ScoresFragment.this.progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            StringProvider stringProvider = new ScoresStringProvider();
            FileProvider fileProvider = new FileProvider(activity, stringProvider, globalGroupId);

            parser = new ScoresParser(fileProvider.provideContent());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            activity.runOnUiThread(new Runnable() {
                public void run() {
                    updateList();
                }
            });
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values[0]);
        }
    }
}
