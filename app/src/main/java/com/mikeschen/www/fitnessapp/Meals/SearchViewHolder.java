package com.mikeschen.www.fitnessapp.Meals;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mikeschen.www.fitnessapp.R;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;



public class SearchViewHolder extends RecyclerView.ViewHolder implements MealsTouchHelperViewHolder {

    @Bind(R.id.searchTextView) TextView mSearchTextView;
    @Bind(R.id.calorieTextView) TextView mCalorieTextView;

    private Context mContext;
    private ArrayList<Food> mFoods = new ArrayList<>();

    public SearchViewHolder(View foodView, ArrayList<Food> foods) {
        super(foodView);
        ButterKnife.bind(this, foodView);
        mContext = foodView.getContext();
        mFoods = foods;
        foodView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int foodPosition = getLayoutPosition();
                Toast.makeText(mContext, "Click", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, MealsActivity.class);
                intent.putExtra("position", foodPosition);
                intent.putExtra("food", Parcels.wrap(mFoods));
                mContext.startActivity(intent);
            }
        });
    }

    public void bindFood(Food food) {
        mSearchTextView.setText(food.getItemName());
        mCalorieTextView.setText(food.getCalories() + "");
    }

    @Override
    public void onFoodSelected() {

    }

    @Override
    public void onFoodClear() {

    }
}

