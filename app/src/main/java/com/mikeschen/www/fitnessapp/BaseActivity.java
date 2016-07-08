package com.mikeschen.www.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.mikeschen.www.fitnessapp.Meals.MealsActivity;
import com.mikeschen.www.fitnessapp.main.MainActivity;
import com.mikeschen.www.fitnessapp.maps.MapsActivity;
import com.mikeschen.www.fitnessapp.simpleActivities.AboutActivity;
import com.mikeschen.www.fitnessapp.simpleActivities.HistoryActivity;
import com.mikeschen.www.fitnessapp.simpleActivities.PrefsActivity;
import com.mikeschen.www.fitnessapp.simpleActivities.RealStatsActivity;
import com.mikeschen.www.fitnessapp.utils.DatabaseHelper;
import com.mikeschen.www.fitnessapp.utils.HeightWeightDatabaseHelper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends AppCompatActivity {
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    public Context mContext;
    public SharedPreferences mSharedPreferences;
    public SharedPreferences.Editor mEditor;
    public DatabaseHelper db;
    public HeightWeightDatabaseHelper heightWeightDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mActivityTitle = getTitle().toString();
        mContext = this;

        db = new DatabaseHelper(mContext.getApplicationContext());
        heightWeightDB = new HeightWeightDatabaseHelper(mContext.getApplicationContext());


        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mSharedPreferences.edit();
    }

    @Override
    public void setContentView(int layoutResID) {
        mDrawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        mDrawerList = (ListView) mDrawerLayout.findViewById(R.id.navList);
        FrameLayout activityContainer = (FrameLayout) mDrawerLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        setupDrawer();
        addDrawerItems();
        super.setContentView(mDrawerLayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    //Calligraphy
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    private void addDrawerItems() {

        String[] navArray = {"Main", "Maps", "Meals", "Stats", "History", "Prefs", "About"};
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, navArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDrawerLayout.closeDrawers();
                switch (position) {
                    case 0:
                        Intent main = new Intent(BaseActivity.this, MainActivity.class);
                        startActivity(main);
                        finish();
                        break;
                    case 1:
                        Intent maps = new Intent(BaseActivity.this, MapsActivity.class);
                        startActivity(maps);
                        finish();
                        break;
                    case 2:
                        Intent meals = new Intent(BaseActivity.this, MealsActivity.class);
                        startActivity(meals);
                        finish();
                        break;
                    case 3:
                        Intent stats = new Intent(BaseActivity.this, RealStatsActivity.class);
                        startActivity(stats);
                        finish();
                        break;
                    case 4:
                        Intent history = new Intent(BaseActivity.this, HistoryActivity.class);
                        startActivity(history);
                        finish();
                        break;
                    case 5:
                        Intent prefs = new Intent(BaseActivity.this, PrefsActivity.class);
                        startActivity(prefs);
                        finish();
                        break;
                    case 6:
                        Intent about = new Intent(BaseActivity.this, AboutActivity.class);
                        startActivity(about);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
}
