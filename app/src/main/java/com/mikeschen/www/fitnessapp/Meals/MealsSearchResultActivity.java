package com.mikeschen.www.fitnessapp.Meals;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.adapters.SearchListAdapter;
import com.mikeschen.www.fitnessapp.models.Food;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MealsSearchResultActivity extends AppCompatActivity implements MealsInterface.View {

    private SearchListAdapter mSearchListAdapter;
    private MealsPresenter mMealsPresenter;
    private ProgressDialog mAuthProgressDialog;

    @Bind(R.id.searchResultsRecyclerView) RecyclerView mSearchResultsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals_search_result);
        ButterKnife.bind(this);
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Searching for food items...");
        mAuthProgressDialog.setCancelable(false);
        mAuthProgressDialog.show();

        mMealsPresenter = new MealsPresenter(this);

        Intent intent = getIntent();
        String foodItem = intent.getStringExtra("food item");
        String scanResult = intent.getStringExtra("inputText");
        if (scanResult == null) {
            mMealsPresenter.searchFoods(foodItem);
        } else {
            mMealsPresenter.searchUPC(scanResult);
        }
    }

    @Override
    public void displayFoodByUPC(final ArrayList<Food> foods) {
        MealsSearchResultActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mMealsPresenter.mFoods == null) {
                    mAuthProgressDialog.dismiss();
                    Toast.makeText(MealsSearchResultActivity.this, "Food Item Not Found", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MealsSearchResultActivity.this, MealsActivity.class);
                    startActivity(intent);
                } else {
                    mSearchListAdapter = new SearchListAdapter(getApplicationContext(), foods);
                    mSearchResultsRecyclerView.setAdapter(mSearchListAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(MealsSearchResultActivity.this);
                    mSearchResultsRecyclerView.setLayoutManager(layoutManager);
                    mAuthProgressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void displayFoodByItem(final ArrayList<Food> foods) {
        MealsSearchResultActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mMealsPresenter.mFoods.isEmpty()) {
                    mAuthProgressDialog.dismiss();
                    Toast.makeText(MealsSearchResultActivity.this, "Food Item Not Found", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MealsSearchResultActivity.this, MealsActivity.class);
                    startActivity(intent);
                } else {
                    mSearchListAdapter = new SearchListAdapter(getApplicationContext(), foods);
                    mSearchResultsRecyclerView.setAdapter(mSearchListAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(MealsSearchResultActivity.this);
                    mSearchResultsRecyclerView.setLayoutManager(layoutManager);
                    mAuthProgressDialog.dismiss();
                }
            }
        });
    }
}
