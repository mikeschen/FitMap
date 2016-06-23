package com.mikeschen.www.fitnessapp.Meals;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.mikeschen.www.fitnessapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MealsSearchResultActivity extends AppCompatActivity {

    private ItemTouchHelper mItemTouchHelper;
    @Bind(R.id.searchResultsRecyclerView) RecyclerView mSearchResultsRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals_search_result);
        ButterKnife.bind(this);

        setUpRecyclerView();
    }

    private void setUpRecyclerView();
    mSearchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
}
