package com.mikeschen.www.fitnessapp.Meals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.adapters.SearchListAdapter;
import com.mikeschen.www.fitnessapp.models.Food;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MealsSearchResultActivity extends AppCompatActivity implements MealsInterface.View {

    private SearchListAdapter mSearchListAdapter;
    private MealsPresenter mMealsPresenter;

    @Bind(R.id.searchResultsRecyclerView) RecyclerView mSearchResultsRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals_search_result);
        ButterKnife.bind(this);

        mMealsPresenter = new MealsPresenter(this);

        Intent intent = getIntent();
        String foodItem = intent.getStringExtra("food item");
        String scanResult = intent.getStringExtra("inputText");
        if ( scanResult == null) {

            Log.d("Food Item?", intent.getStringExtra("food item"));
            mMealsPresenter.searchFoods(foodItem);
        } else {
            Log.d("scanResult", scanResult);
            mMealsPresenter.searchUPC(scanResult);
        }

    }

    @Override
    public void displayFoodByUPC(final ArrayList<Food> foods) {
        MealsSearchResultActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("displayFoodByUPC", foods + "");
                mSearchListAdapter = new SearchListAdapter(getApplicationContext(), foods);
                mSearchResultsRecyclerView.setAdapter(mSearchListAdapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(MealsSearchResultActivity.this);
                mSearchResultsRecyclerView.setLayoutManager(layoutManager);
            }
        });
    }

    @Override
    public void displayFoodByItem(final ArrayList<Food> foods) {
        MealsSearchResultActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("displayFoodByItem", foods + "");
                mSearchListAdapter = new SearchListAdapter(getApplicationContext(), foods);
                mSearchResultsRecyclerView.setAdapter(mSearchListAdapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(MealsSearchResultActivity.this);
                mSearchResultsRecyclerView.setLayoutManager(layoutManager);
            }
        });
    }
}
