package com.mikeschen.www.fitnessapp.Meals;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mikeschen.www.fitnessapp.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MealsDetailActivity extends AppCompatActivity {

    @Bind(R.id.viewPager)
    ViewPager mViewPager;
    private MealsPagerAdapter adapterViewPager;
    ArrayList<Food> mFood = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals_detail);
        ButterKnife.bind(this);

        mFood = Parcels.unwrap(getIntent().getParcelableExtra("items"));
        int startingPosition = Integer.parseInt(getIntent().getStringExtra("position"));
        adapterViewPager = new MealsPagerAdapter(this, getSupportFragmentManager(), mFood);
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(startingPosition);
    }
}
