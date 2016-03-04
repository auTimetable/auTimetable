package ru.spbau.mit.auTimetable;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static int groupNumber = 0;
    private static int subgroupNumber = 0;

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;

    private int currentSection = 0;

    private final static int[] SECTION_TITLE_IDS = new int[]{
            R.string.title_section1,
            R.string.title_section2,
            R.string.title_section3,
            R.string.title_section4,
            R.string.title_section5,
            R.string.title_section6
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout)
        );
    }

    private boolean mReturningWithResult = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            groupNumber = data.getIntExtra("group_number", 0);
            subgroupNumber = data.getIntExtra("subgroup_number", 0);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance(currentSection))
                    .commit();
        }

        mReturningWithResult = true;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (mReturningWithResult) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance(currentSection))
                    .commit();
        }

        mReturningWithResult = false;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        currentSection = position + 1;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(currentSection))
                .commit();
    }

    public void onSectionAttached(int number) {
        mTitle = getString(SECTION_TITLE_IDS[number - 1]);

        if (number == 6) {
            restoreActionBar();
        }
    }

    @SuppressWarnings("deprecation")
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        try {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
        } catch (NullPointerException e) {
            //this can never happen
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("group_number", groupNumber);
        savedInstanceState.putInt("subgroup_number", subgroupNumber);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        groupNumber = savedInstanceState.getInt("group_number");
        subgroupNumber = savedInstanceState.getInt("subgroup_number");
    }

        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            Bundle extras = new Bundle();

            extras.putInt("group_number", groupNumber);
            extras.putInt("subgroup_number", subgroupNumber);

            intent.putExtras(extras);
            startActivityForResult(intent, 0);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        private static final int[] LAYOUT_IDS = new int[] {
                R.layout.fragment_main,
                R.layout.fragment_announcements,
                R.layout.fragment_contacts,
                R.layout.fragment_main,
                R.layout.fragment_about,
        };

        public static Fragment newInstance(int sectionNumber) {
            Fragment fragment = getFragmentByNumber(sectionNumber);

            Bundle args = new Bundle();
            prepareArgs(args, sectionNumber);

            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int sectionId = this.getArguments().getInt(ARG_SECTION_NUMBER);
            return inflater.inflate(LAYOUT_IDS[sectionId - 1], container, false);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        private static Fragment getFragmentByNumber(int sectionNumber) {
            switch (sectionNumber) {
                case 1:
                    return new TimetableFragment();
                case 4:
                    return new ScoresFragment();
                default:
                    return new PlaceholderFragment();
            }
        }

        private static void prepareArgs(Bundle args, int sectionNumber) {
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt("group_number", groupNumber);
            args.putInt("subgroup_number", subgroupNumber);
        }
    }

}
