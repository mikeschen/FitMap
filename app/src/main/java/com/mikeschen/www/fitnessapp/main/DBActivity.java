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

public class DBActivity extends BaseActivity implements View.OnClickListener{


    @Bind(R.id.stepsRecyclerView) RecyclerView mStepsRecyclerView;
    @Bind(R.id.button) Button mButton;
    private DatabaseDaysListAdapter mDatabaseDaysListAdapter;
    public ArrayList<Days> mDays = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        ButterKnife.bind(this);

        mDays = (ArrayList<Days>) db.getAllDaysRecords();

        mDatabaseDaysListAdapter = new DatabaseDaysListAdapter(getApplicationContext(), mDays);
        mStepsRecyclerView.setAdapter(mDatabaseDaysListAdapter);
        RecyclerView.LayoutManager stepsLayoutManager =
                new LinearLayoutManager(DBActivity.this);
        mStepsRecyclerView.setLayoutManager(stepsLayoutManager);
        mStepsRecyclerView.setHasFixedSize(true);

        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case(R.id.button) :
                db.deleteAllDayRecords();
                db.deleteAllFoodRecords();
                db.closeDB();
                refresh();
        }
    }

    public void refresh() {
        Intent intent = new Intent(DBActivity.this, DBActivity.class);
        startActivity(intent);
        finish();
    }
}
