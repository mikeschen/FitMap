package com.mikeschen.www.fitnessapp.Meals;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mikeschen.www.fitnessapp.BaseActivity;
import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.adapters.SearchListAdapter;
import com.mikeschen.www.fitnessapp.models.Days;
import com.mikeschen.www.fitnessapp.models.Food;
import com.mikeschen.www.fitnessapp.utils.OnFoodClickedListener;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MealsActivity extends BaseActivity implements
        MealsInterface.View,
        OnFoodClickedListener,
        View.OnClickListener {
    @Bind(R.id.todaysDate) TextView mTodaysDate;
    @Bind(R.id.totalCaloriesTextView) TextView mTotalCaloriesTextView;
    @Bind(R.id.foodRecyclerView) RecyclerView mFoodRecyclerView;

    private String mSearchString;
    private String mSearchType;
    private ProgressDialog mAuthProgressDialog;

    private MealsPresenter mMealsPresenter;
    private SearchListAdapter mAdapter;

    public ArrayList<Food> mFoods = new ArrayList<>();

    private Days today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);
        ButterKnife.bind(this);

        List<Days> days = db.getAllDaysRecords();
        if(days.size() > 0) {
            today = days.get(days.size()-1);
        }

        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Searching for food items...");
        mAuthProgressDialog.setCancelable(false);
        Intent intent = getIntent();

        //setup recycler view
        int position = intent.getIntExtra("position", -1);
        ArrayList<Food> foods = Parcels.unwrap(intent.getParcelableExtra("food"));
        //Adds food from API call results
        if (position >= 0 && foods != null) {
            db.logFood(foods.get(position));
            mTotalCaloriesTextView.setText("Total Calories: " + getFoodFromDB());
        }

        mSearchString = intent.getStringExtra("inputText");

        mMealsPresenter = new MealsPresenter(this);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("MM / dd / yyyy");
        String strDate = mdformat.format(calendar.getTime());
        mTodaysDate.setText("Today's Date: " + strDate);

        mTotalCaloriesTextView.setText("Total Calories: " + getFoodFromDB());

        mAdapter.setOnFoodClickedListener(this);
    }


    private void scanUpc() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a food barcode");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null && resultCode == RESULT_OK) {
            String scanContent = scanningResult.getContents();
            Intent searchIntent = new Intent(this, MealsSearchResultActivity.class);
            searchIntent.putExtra("inputText", scanContent);
            startActivity(searchIntent);

        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_photo:
                scanUpc();
                break;
            case R.id.action_search:
                openDialog();
                break;
            case R.id.add_item:
                openAddItemDialog();
                break;
            default:
        }
        return false;
    }

    @Override
    public void displayFoodByUPC(ArrayList<Food> foods) {
        //Used in MealsSearchResultActivity
    }

    @Override
    public void displayFoodByItem(ArrayList<Food> foods) {
        //Used in MealsSearchResultActivity
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

                if (foodItem.trim().length() == 0) {
                    Toast.makeText(MealsActivity.this, "Please enter a food to search for", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mContext, MealsSearchResultActivity.class);
                    intent.putExtra("food item", foodItem);
                    mContext.startActivity(intent);
                    finish();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void openAddItemDialog() {
        LayoutInflater inflater = LayoutInflater.from(MealsActivity.this);
        View subView = inflater.inflate(R.layout.fragment_add_item, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a food item");
        builder.setView(subView);

        final EditText subEditText = (EditText) subView.findViewById(R.id.foodInputEditText);
        final EditText secondEditText = (EditText) subView.findViewById(R.id.calorieInputEditText);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String foodItem = subEditText.getText().toString();
                    String foodCalories = secondEditText.getText().toString();

                    if (foodItem.trim().length() == 0 || foodCalories.trim().length() == 0) {
                        Toast.makeText(MealsActivity.this, "Please fill out both fields", Toast.LENGTH_SHORT).show();
                    } else {

                        Integer intCalories = Integer.parseInt(foodCalories);
                        Food food = new Food(1, foodItem, intCalories);
                        db.logFood(food);
                        mTotalCaloriesTextView.setText("TOTAL CALORIES CONSUMED: " + getFoodFromDB());
                        }
                    }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.show();
        }

    public String getFoodFromDB() {

        //Populates RecyclerView with saved food items
        mFoods = (ArrayList<Food>) db.getAllFoodRecords();
        mAdapter = new SearchListAdapter(this, mFoods);

        mFoodRecyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager stepsLayoutManager =
                new LinearLayoutManager(this);
        mFoodRecyclerView.setLayoutManager(stepsLayoutManager);
        mFoodRecyclerView.setHasFixedSize(true);

        //Adds number of consumed calories
        int totalCalories = 0;
        for (int i = 0; i < mFoods.size(); i++) {
            totalCalories += mFoods.get(i).getCalories();
        }
        today.setCaloriesConsumed(totalCalories);
        db.updateDays(today);
        return String.valueOf(totalCalories);
    }

    @Override
    public void onFoodClicked(int position, ArrayList<Food> mFoods) {

        final long foodId = mFoods.get(position).getItemId();

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Remove Item From List");
        builder.setMessage("Are you sure you want to delete this item?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteFoodRecord(foodId);
                Toast.makeText(mContext.getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                mTotalCaloriesTextView.setText("Total Calories: " + getFoodFromDB());
                db.closeDB();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    @Override
    public void onClick(View view) {

    }
}





