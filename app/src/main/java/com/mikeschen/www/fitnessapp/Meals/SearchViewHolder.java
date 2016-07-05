package com.mikeschen.www.fitnessapp.Meals;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.models.Food;
import com.mikeschen.www.fitnessapp.utils.MealsTouchHelperViewHolder;
import com.mikeschen.www.fitnessapp.utils.OnFoodClickedListener;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;



public class SearchViewHolder extends RecyclerView.ViewHolder implements MealsTouchHelperViewHolder {

    @Bind(R.id.searchTextView) TextView mSearchTextView;
    @Bind(R.id.calorieTextView) TextView mCalorieTextView;
    @Bind(R.id.plusicon) ImageView mPlusIcon;

    private Context mContext;
    private ArrayList<Food> mFoods = new ArrayList<>();

    private OnFoodClickedListener mOnFoodClickListener;

    public SearchViewHolder(View foodView, ArrayList<Food> foods) {
        super(foodView);
        ButterKnife.bind(this, foodView);
        mContext = foodView.getContext();
        mFoods = foods;

        if (mContext instanceof OnFoodClickedListener) {
            mOnFoodClickListener = (OnFoodClickedListener) foodView.getContext();
        }

        if (mContext.getClass() == MealsSearchResultActivity.class) {
            mPlusIcon.setImageResource(R.drawable.ic_add_circle_black_24dp);
        }

        foodView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int foodPosition = getLayoutPosition();

                if (mContext.getClass() == MealsSearchResultActivity.class) {
                    Toast.makeText(mContext.getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, MealsActivity.class);
                    intent.putExtra("position", foodPosition);
                    intent.putExtra("food", Parcels.wrap(mFoods));
                    mContext.startActivity(intent);
                } else {
                    if (mOnFoodClickListener != null) {
                        mOnFoodClickListener.onFoodClicked(foodPosition, mFoods);
                    }
                }
            }
        });
    }

    public void bindFood(Food food) {
        mSearchTextView.setText(food.getItemName());
        mCalorieTextView.setText(food.getCalories() + "" + " cals");
    }

    @Override
    public void onFoodSelected() {

    }

    @Override
    public void onFoodClear() {

    }

}

