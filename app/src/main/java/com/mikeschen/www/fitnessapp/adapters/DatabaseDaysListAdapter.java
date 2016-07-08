package com.mikeschen.www.fitnessapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikeschen.www.fitnessapp.Meals.SearchViewHolder;
import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.models.Days;
import com.mikeschen.www.fitnessapp.models.Food;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ramon on 6/28/16.
 */
public class DatabaseDaysListAdapter extends RecyclerView.Adapter<DatabaseDaysListAdapter.DatabaseViewHolder> {
    private ArrayList<Days> mDays = new ArrayList<>();
    private Context mContext;

    public DatabaseDaysListAdapter(Context context, ArrayList<Days> days) {
        mContext = context;
        mDays = days;
    }

    @Override
    public DatabaseDaysListAdapter.DatabaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.database_list_item, parent, false);
        DatabaseViewHolder viewHolder = new DatabaseViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DatabaseDaysListAdapter.DatabaseViewHolder holder, int position) {
        holder.bindDays(mDays.get(position));
    }

    @Override
    public int getItemCount() {
        return mDays.size();
    }

    public class DatabaseViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textViewCount) TextView mTextViewCount;
        @Bind(R.id.textViewCalBurned) TextView mTextViewCalBurned;
        @Bind(R.id.textViewCalConsumed) TextView mTextViewCalConsumed;
        @Bind(R.id.textViewDate) TextView mTextViewDate;
        private Context mContext;

        public DatabaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }
        public void bindDays(Days days) {

            mTextViewCount.setText(String.valueOf(days.getStepsTaken()));
            mTextViewCalBurned.setText(String.valueOf(days.getCaloriesBurned()));
            mTextViewCalConsumed.setText(String.valueOf(days.getCaloriesConsumed()));
            mTextViewDate.setText(days.getDate());
        }
    }
}
