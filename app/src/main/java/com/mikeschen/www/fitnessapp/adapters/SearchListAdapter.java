package com.mikeschen.www.fitnessapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikeschen.www.fitnessapp.utils.ItemTouchHelperAdapter;
import com.mikeschen.www.fitnessapp.utils.OnFoodClickedListener;
import com.mikeschen.www.fitnessapp.Meals.SearchViewHolder;
import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.models.Food;

import java.util.ArrayList;

public class SearchListAdapter extends RecyclerView.Adapter<SearchViewHolder> {
    private ArrayList<Food> mFood = new ArrayList<>();
    private Context mContext;
    private OnFoodClickedListener mOnFoodClickedListener;

    public SearchListAdapter(Context context, ArrayList<Food> foods) {
        mContext = context;
        mFood = foods;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);
        SearchViewHolder viewHolder = new SearchViewHolder(view, mFood);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        holder.bindFood(mFood.get(position));
    }

    @Override
    public int getItemCount() {
        return mFood.size();
    }

    public void setOnFoodClickedListener(OnFoodClickedListener listener) {
        mOnFoodClickedListener = listener;

    }
}
