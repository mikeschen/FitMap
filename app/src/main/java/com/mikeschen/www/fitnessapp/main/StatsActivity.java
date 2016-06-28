package com.mikeschen.www.fitnessapp.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.mikeschen.www.fitnessapp.BaseActivity;
import com.mikeschen.www.fitnessapp.adapters.DatabaseDaysListAdapter;
import com.mikeschen.www.fitnessapp.models.Calories;
import com.mikeschen.www.fitnessapp.adapters.DatabaseCalorieListAdapter;
import com.mikeschen.www.fitnessapp.adapters.DatabaseCaloriesConsumedListAdapter;
import com.mikeschen.www.fitnessapp.models.Days;
import com.mikeschen.www.fitnessapp.utils.DatabaseHelper;
import com.mikeschen.www.fitnessapp.adapters.DatabaseStepsListAdapter;
import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.models.Steps;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StatsActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.stepsRecyclerView) RecyclerView mStepsRecyclerView;
//    @Bind(R.id.caloriesBurnedRecyclerView) RecyclerView mCaloriesBurnedRecyclerView;
//    @Bind(R.id.caloriesConsumedRecyclerView) RecyclerView mCaloriesConsumedRecyclerView;
    @Bind(R.id.button) Button mButton;
    private DatabaseDaysListAdapter mDatabaseDaysListAdapter;
//    private DatabaseCalorieListAdapter mDatabaseCaloriesBurnedListAdapter;
//    private DatabaseCaloriesConsumedListAdapter mDatabaseCaloriesConsumedListAdapter;
    public ArrayList<Days> mDays = new ArrayList<>();
//    public ArrayList<Calories> mCaloriesBurned = new ArrayList<>();
//    public ArrayList<Calories> mCaloriesConsumed = new ArrayList<>();
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        setTitle("Stats");
        ButterKnife.bind(this);

        db = new DatabaseHelper(getApplicationContext());
        mDays = (ArrayList<Days>) db.getAllDaysRecords();

        mDatabaseDaysListAdapter = new DatabaseDaysListAdapter(getApplicationContext(), mDays);
        mStepsRecyclerView.setAdapter(mDatabaseDaysListAdapter);
        RecyclerView.LayoutManager stepsLayoutManager =
                new LinearLayoutManager(StatsActivity.this);
        mStepsRecyclerView.setLayoutManager(stepsLayoutManager);
        mStepsRecyclerView.setHasFixedSize(true);

//        mDatabaseCaloriesBurnedListAdapter = new DatabaseCalorieListAdapter(getApplicationContext(), mCaloriesBurned);
//        mCaloriesBurnedRecyclerView.setAdapter(mDatabaseCaloriesBurnedListAdapter);
//        RecyclerView.LayoutManager calorieLayoutManager =
//                new LinearLayoutManager(StatsActivity.this);
//        mCaloriesBurnedRecyclerView.setLayoutManager(calorieLayoutManager);
//        mCaloriesBurnedRecyclerView.setHasFixedSize(true);
//
//        mDatabaseCaloriesConsumedListAdapter = new DatabaseCaloriesConsumedListAdapter(getApplicationContext(), mCaloriesConsumed);
//        mCaloriesConsumedRecyclerView.setAdapter(mDatabaseCaloriesConsumedListAdapter);
//        RecyclerView.LayoutManager caloriesConsumedLayoutManager =
//                new LinearLayoutManager(StatsActivity.this);
//        mCaloriesConsumedRecyclerView.setLayoutManager(caloriesConsumedLayoutManager);
//        mCaloriesConsumedRecyclerView.setHasFixedSize(true);
//
//        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case(R.id.button) :
                db.deleteAllDayRecords();
//                db.deleteAllCaloriesBurnedRecords();
//                db.deleteAllCaloriesConsumedRecords();
                refresh();
        }
    }

    public void refresh() {
        Intent intent = new Intent(StatsActivity.this, StatsActivity.class);
        startActivity(intent);
        finish();
    }
}




