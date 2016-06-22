package com.mikeschen.www.fitnessapp.Meals;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mikeschen.www.fitnessapp.BaseActivity;
import com.mikeschen.www.fitnessapp.Calories;
import com.mikeschen.www.fitnessapp.DatabaseHelper;
import com.mikeschen.www.fitnessapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MealsActivity extends BaseActivity implements
        MealsInterface.View,
        View.OnClickListener{
    @Bind(R.id.foodInputEditText) EditText mFoodInputEditText;
    @Bind(R.id.dailyCaloriesBurnedTextView) TextView mDailyCaloriesBurnedTextView;
    @Bind(R.id.todaysDate) TextView mTodaysDate;
    @Bind(R.id.calorieInputEditText) EditText mCalorieInputEditText;
    @Bind(R.id.saveButton) Button mSaveButton;
    @Bind(R.id.totalCaloriesTextView) TextView mTotalCaloriesTextView;

    private String mSearchString;
    private String mSearchType;
    private ProgressDialog mAuthProgressDialog;

    DatabaseHelper db;
    private MealsPresenter mMealsPresenter;

    private Context mContext;
    public ArrayList<Food> mFoods = new ArrayList<>();

    private void setHideSoftKeyboard(EditText editText){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);

        ButterKnife.bind(this);


        mContext = this;
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Searching for food items...");
        mAuthProgressDialog.setCancelable(false);
        Intent intent = getIntent();
        mSearchString = intent.getStringExtra("inputText");
//        mSearchType = mSharedPreferences.getString(Constants.PREFERENCES_SEARCH_TYPE_KEY, null);
        if(mSearchType != null && mSearchType.equals("string")){
//            searchDatabaseByTerm();
        } else if(mSearchType != null && mSearchType.equals("upc") && mSearchString != null){
//            mMealsPresenter.searchUPC(String upc);
        }

//        if(mSearchType != null && mSearchType.equals("string")){
//            searchDatabaseByTerm();
//        } else if(mSearchType != null && mSearchType.equals("upc") && mSearchString != null){
//            mMealsPresenter.searchUPC(upc);
//        }
//        mAuthProgressDialog.show();


        mSaveButton.setOnClickListener(this);
        db = new DatabaseHelper(getApplicationContext());
        mMealsPresenter = new MealsPresenter(this, getApplicationContext());



        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("MM / dd / yyyy");
        String strDate = "Today's Date : " + mdformat.format(calendar.getTime());
        mTodaysDate.setText(strDate);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.saveButton):
                String strCalories = mCalorieInputEditText.getText().toString();
                Integer calories = Integer.parseInt(strCalories);
                setHideSoftKeyboard(mCalorieInputEditText);
                mMealsPresenter.saveCalories(calories);
                break;
        }
    }

    @Override
    public void showFoodItem(String foodItem) {

    }

    @Override
    public void saveFoodItem(String foodItem) {

    }

    @Override
    public void showCalories(Calories calories) {
        Calories caloriesConsumed;
        caloriesConsumed = new Calories(7, calories.getCalories(), calories.getDate());

        caloriesConsumed = db.getCaloriesConsumed(calories.getId());
        mTotalCaloriesTextView.setText("TOTAL CALORIES CONSUMED: " + caloriesConsumed);
        Log.d("Calories consumed", caloriesConsumed + "");
    }

    @Override
    public void refresh() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        ButterKnife.bind(this);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide action item
                getSupportActionBar().setTitle("");
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getSupportActionBar().setTitle("FitnessApp");
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String mFoodInputEditText) {
                Intent intent = new Intent(MealsActivity.this, MealsSearchResultActivity.class);
                intent.putExtra("mFoodInputEditText", mFoodInputEditText);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayFood(ArrayList<Food> foods) {
        MealsActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mFoods == null) {
                    mAuthProgressDialog.dismiss();
                    Toast.makeText(mContext, "Food Item Not Found", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(mContext, MealsSearchResultActivity.class);
                    mContext.startActivity(intent);
                } else {
                    mAuthProgressDialog.dismiss();
                }
            }
        });
    }
}
