//package com.mikeschen.www.fitnessapp.Meals;
//
//import android.content.Context;
//import android.content.Intent;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.TextView;
//
//import com.mikeschen.www.fitnessapp.R;
//
//import java.util.ArrayList;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
///**
// * Created by Ramon on 6/21/16.
// */
//public class MealsViewHolder extends RecyclerView.ViewHolder implements MealsTouchHelperViewHolder {
//
//    @Bind(R.id.nameTextView) public TextView mNameTextView;
//    @Bind(R.id.quantityTextView) TextView mQuantityTextView;
//    @Bind(R.id.notesTextView) TextView mNotesTextView;
//
//    private Context mContext;
//    private ArrayList<Food> mFood = new ArrayList<>();
//
//    public GroceryViewHolder(View foodView, ArrayList<Food> foods) {
//        super(foodView);
//        ButterKnife.bind(this, foodView);
//        mContext = foodView.getContext();
//        mFood = foods;
//        foodView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int foodPosition = getLayoutPosition();
//                Intent intent = new Intent(mContext, MealsDetailActivity.class);
//                intent.putExtra("position", foodPosition + "");
//                intent.putExtra("food", Parcels.wrap(mFood));
//                mContext.startActivity(intent);
//            }
//        });
//    }
//
//    public void bindFood(Food food) {
//        mNameTextView.setText(food.getFoodName());
//        mQuantityTextView.setText(food.getFoodQuantity());
//        mNotesTextView.setText(food.getFoodNotes());
//    }
//
//    @Override
//    public void onFoodSelected() {
//
//    }
//
//    @Override
//    public void onFoodClear() {
//
//    }
//}
