//package com.mikeschen.www.fitnessapp.Meals;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.mikeschen.www.fitnessapp.R;
//
//import java.util.ArrayList;
//
///**
// * Created by Ramon on 6/21/16.
// */
//public class MealsListAdapter extends RecyclerView.Adapter<MealsViewHolder> {
//    private ArrayList<Food> mFood = new ArrayList<>();
//    private Context mContext;
//
//    public MealsListAdapter(Context context, ArrayList<Food> foods) {
//        mContext = context;
//        mFood = foods;
//    }
//
//    @Override
//    public MealsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meals_list_item, parent, false);
//        MealsViewHolder viewHolder = new MealsViewHolder(view, mFood);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(MealsViewHolder holder, int position) {
//        holder.bindFood(mFood.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return mFood.size();
//    }
//}