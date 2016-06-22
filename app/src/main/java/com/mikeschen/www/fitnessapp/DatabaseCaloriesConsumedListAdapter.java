package com.mikeschen.www.fitnessapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ramon on 6/21/16.
 */
public class DatabaseCaloriesConsumedListAdapter extends RecyclerView.Adapter<DatabaseCaloriesConsumedListAdapter.DatabaseViewHolder> {
    private ArrayList<Calories> mCalories = new ArrayList<>();
    private Context mContext;

    public DatabaseCaloriesConsumedListAdapter(Context context, ArrayList<Calories> calories) {
        mContext = context;
        mCalories = calories;
    }

    @Override
    public DatabaseCaloriesConsumedListAdapter.DatabaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.database_list_item, parent, false);
        DatabaseViewHolder viewHolder = new DatabaseViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DatabaseCaloriesConsumedListAdapter.DatabaseViewHolder holder, int position) {
        holder.bindCalories(mCalories.get(position));
    }

    @Override
    public int getItemCount() {
        return mCalories.size();
    }

    public class DatabaseViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textViewId) TextView mTextViewId;
        @Bind(R.id.textViewCount) TextView mTextViewCount;
        @Bind(R.id.textViewDate) TextView mTextViewDate;
        private Context mContext;

        public DatabaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }

        public void bindCalories(Calories calories) {
            mTextViewId.setText(String.valueOf(calories.getId()));
            mTextViewCount.setText(String.valueOf(calories.getCalories()));
            mTextViewDate.setText(String.valueOf(calories.getDate()));

        }
    }
}