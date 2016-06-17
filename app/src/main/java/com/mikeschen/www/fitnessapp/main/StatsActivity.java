package com.mikeschen.www.fitnessapp.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mikeschen.www.fitnessapp.DatabaseHelper;
import com.mikeschen.www.fitnessapp.DatabaseListAdapter;
import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.Steps;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StatsActivity extends AppCompatActivity {

    @Bind(R.id.recyclerView) RecyclerView mStepsRecyclerView;
    private DatabaseListAdapter mDatabaseListAdapter;
    public ArrayList<Steps> mSteps = new ArrayList<>();
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        ButterKnife.bind(this);

        db = new DatabaseHelper(getApplicationContext());
        mSteps = (ArrayList<Steps>) db.getAllStepRecords();

        mDatabaseListAdapter = new DatabaseListAdapter(getApplicationContext(), mSteps);
        mStepsRecyclerView.setAdapter(mDatabaseListAdapter);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(StatsActivity.this);
        mStepsRecyclerView.setLayoutManager(layoutManager);
        mStepsRecyclerView.setHasFixedSize(true);
    }
}

