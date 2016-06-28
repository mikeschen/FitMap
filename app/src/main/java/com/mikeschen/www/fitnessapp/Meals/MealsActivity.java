package com.mikeschen.www.fitnessapp.Meals;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.mikeschen.www.fitnessapp.models.Calories;
import com.mikeschen.www.fitnessapp.utils.DatabaseHelper;
import com.mikeschen.www.fitnessapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    @Bind(R.id.dialogButton) Button mDialogButton;

    private String mSearchString;
    private String mSearchType;
    private ProgressDialog mAuthProgressDialog;

    DatabaseHelper db;
    private MealsPresenter mMealsPresenter;

    private Context mContext;
    public ArrayList<Food> mFoods = new ArrayList<>();
    private Calories calorieRecord;

    private void setHideSoftKeyboard(EditText editText){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);
        setTitle("Meals");
        ButterKnife.bind(this);

        mContext = this;
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Searching for food items...");
        mAuthProgressDialog.setCancelable(false);
        Intent intent = getIntent();
        mSearchString = intent.getStringExtra("inputText");

        if(mSearchType != null && mSearchType.equals("string")){

        } else if(mSearchType != null && mSearchType.equals("upc") && mSearchString != null){

        }

        mSaveButton.setOnClickListener(this);
        mDialogButton.setOnClickListener(this);
        db = new DatabaseHelper(getApplicationContext());
        List<Calories> calories = db.getAllCalorieConsumedRecords();
        mMealsPresenter = new MealsPresenter(this);

        if (calories.size() > 0) {
            calorieRecord = calories.get(calories.size() - 1);
            mMealsPresenter.loadCalories(calorieRecord);
        } else {
            calorieRecord = new Calories(1, 0, 345);
            mTotalCaloriesTextView.setText("TOTAL CALORIES CONSUMED: " + 0);
        }



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



                mMealsPresenter.computeCalories(calories, calorieRecord);
                break;
            case (R.id.dialogButton):
                openDialog();
                break;
        }
    }



    @Override
    public void showFoodItem(String foodItem) {

    }

    @Override
    public void saveFoodItem(String foodItem) {

    }

//    @Override
//    public void saveCalories(Integer calories) {
//        Calories caloriesConsumed;
//        caloriesConsumed = new Calories(1, calories, 345);
//        db.updateCaloriesConsumed(caloriesConsumed);
//        Log.d("saveCalories", caloriesConsumed.getCalories() + "");
//        db.close();//MOVE THIS TO PRESENTER, MOVE db.STUFF TO showCalories VEIW METHOD
//    }

    @Override
    public void showCalories(Calories calorieRecord) {
//        Calories caloriesConsumed = db.getCaloriesConsumed(calorieRecord.getId());


        db.updateCaloriesConsumed(calorieRecord);

        Log.d("showCalories", calorieRecord + "");
        mTotalCaloriesTextView.setText("TOTAL CALORIES CONSUMED: " + calorieRecord.getCalories());
        db.close();
    }

    @Override
    public void refresh() {

    }

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
                getSupportActionBar().setTitle("FitMap");
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
    public void displayFoodByUPC(ArrayList<Food> foods) {
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


    @Override
    public void displayFoodByItem(ArrayList<Food> foods) {
    }

    private void openDialog() {
        LayoutInflater inflater = LayoutInflater.from(MealsActivity.this);
        View subView = inflater.inflate(R.layout.search_fragment_item, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search for a food item");
        builder.setView(subView);

        final EditText subEditText = (EditText) subView.findViewById(R.id.searchFoodItemEditText);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                String foodItem = subEditText.getText().toString();
                Intent intent = new Intent(mContext, MealsSearchResultActivity.class);
                intent.putExtra("food item", foodItem);
                Log.d("Food Item Input", intent.putExtra("food item", foodItem) + "");

                mContext.startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MealsActivity.this, "Cancel", Toast.LENGTH_LONG).show();
            }
        });

        builder.show();
    }

    //TODO
    //Create a "food" object so we can add from API call and manual entry
    //MealsActivity becomes RecyclerView of all saved food objects
    //Calorie values of "food" object are totaled, rather than Calories consumed from DB table
    //Refactor to remove calories consumed DB table?

}

