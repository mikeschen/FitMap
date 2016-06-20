package com.mikeschen.www.fitnessapp.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.mikeschen.www.fitnessapp.DatabaseHelper;
import com.mikeschen.www.fitnessapp.DatabaseListAdapter;
import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.Steps;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StatsActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.recyclerView) RecyclerView mStepsRecyclerView;
    @Bind(R.id.button) Button mButton;
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
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case(R.id.button) :
                db.deleteAllStepsRecords();
                refresh();
        }
    }

    public void refresh() {
        Intent intent = new Intent(StatsActivity.this, StatsActivity.class);
        startActivity(intent);
        finish();
    }
}




