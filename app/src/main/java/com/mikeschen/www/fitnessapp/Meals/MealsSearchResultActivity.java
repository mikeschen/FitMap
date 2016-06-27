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
import com.mikeschen.www.fitnessapp.utils.ItemTouchHelperViewHolder;
import com.mikeschen.www.fitnessapp.utils.SimpleItemTouchHelperCallback;

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

        mMealsPresenter = new MealsPresenter(this, getApplicationContext());

        Intent intent = getIntent();
        String foodItem = intent.getStringExtra("food item");
        Log.d("Food Item?", intent.getStringExtra("food item"));
        mMealsPresenter.searchFoods(foodItem);

    }


    @Override
    public void showFoodItem(String foodItem) {

    }

    @Override
    public void saveFoodItem(String foodItem) {

    }

    @Override
    public void showCalories(Calories calories) {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void displayFoodByUPC(ArrayList<Food> foods) {

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
//                Intent intent = new Intent(mContext, MealsSearchResultActivity.class);
//                mContext.startActivity(intent);
            }
        });
    }
}
