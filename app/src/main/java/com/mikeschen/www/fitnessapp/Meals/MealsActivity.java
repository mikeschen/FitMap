package com.mikeschen.www.fitnessapp.Meals;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.mikeschen.www.fitnessapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MealsActivity extends AppCompatActivity {
    @Bind(R.id.foodInputEditText) EditText mFoodInputEditText;
    @Bind(R.id.totalCaloriesTextView) TextView mTotalCaloriesTextView;
    @Bind(R.id.dailyCaloriesBurnedTextView) TextView mDailyCaloriesBurnedTextView;
    @Bind(R.id.todaysDate) TextView mTodaysDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);

        ButterKnife.bind(this);
//        final Calendar c = Calendar.getInstance();
//        char[] day = c.get(Calendar.DAY_OF_MONTH);
//        int month = c.get(Calendar.MONTH);
//        int year = c.get(Calendar.YEAR);
//        mTodaysDate.setText(day, month, year);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ");
        String strDate = "Today's Date : " + mdformat.format(calendar.getTime());
        mTodaysDate.setText(strDate);
    }
}
