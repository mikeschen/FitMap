package com.mikeschen.www.fitnessapp.Meals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.models.Calories;
import com.mikeschen.www.fitnessapp.utils.ItemTouchHelperAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MealsSearchResultActivity extends AppCompatActivity implements MealsInterface.View {

    private SearchListAdapter mSearchListAdapter;
    private ItemTouchHelperAdapter mItemTouchHelperAdapter;
    private Context mContext;
    private MealsPresenter mMealsPresenter;

    public ArrayList<Food> mFoods = new ArrayList<>();


    private ItemTouchHelper mItemTouchHelper;
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
    public void showFoodItem(String foodItem) {

    }

    @Override
    public void saveFoodItem(String foodItem) {

    }

    @Override
    public void showCalories(Calories calorieRecord) {

    }

    @Override
    public void refresh() {

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

//bar code scanned from meals activity the item shows on the mealsSearchResultActivity. From there, the actual item is clicked and it goes
//load recycler view in the beginning of the meal tracker; the first page which is the mealsactivity page should be all the items added
//which are taken from the recyclerView
//searchBar at the top, scanUPC, and add your item
//search dialog will the at the top-change openDialog();
//once the user scans the upc; the option to delete each item
//the app should have a delete function
//design a my page with recyclerView with the name of the food and its activity
//bottun will be replaced
//having the time on its own class, maybe have the timer like a service
//food should be stored in the database which means changing the way to update calories
//