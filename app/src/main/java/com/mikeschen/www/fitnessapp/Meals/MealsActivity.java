package com.mikeschen.www.fitnessapp.Meals;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mikeschen.www.fitnessapp.BaseActivity;
import com.mikeschen.www.fitnessapp.Constants;
import com.mikeschen.www.fitnessapp.R;
import com.mikeschen.www.fitnessapp.adapters.DatabaseDaysListAdapter;
import com.mikeschen.www.fitnessapp.adapters.SearchListAdapter;
import com.mikeschen.www.fitnessapp.models.Days;
import com.mikeschen.www.fitnessapp.models.Food;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MealsActivity extends BaseActivity implements
        MealsInterface.View,
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
    private Days daysRecord;

    private void setHideSoftKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);
        ButterKnife.bind(this);

        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Searching for food items...");
        mAuthProgressDialog.setCancelable(false);
        Intent intent = getIntent();
        //setup recycler view
//        int position = intent.getIntExtra("position", -1);
//        ArrayList<Food> mFoods = intent.getParcelableExtra("food");
        mSearchString = intent.getStringExtra("inputText");

        if (mSearchType != null && mSearchType.equals("string")) {


        } else if (mSearchType != null && mSearchType.equals("upc") && mSearchString != null) {

        }

        List<Days> days = db.getAllDaysRecords();

        mMealsPresenter = new MealsPresenter(this);

        if (days.size() > 0) {
            daysRecord = days.get(days.size() - 1);
//            mMealsPresenter.loadCalories(daysRecord);
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM / dd / yyyy", Locale.getDefault());
            daysRecord = new Days(1, 0, 0, 0, dateFormat.toString());
//            mTotalCaloriesTextView.setText("TOTAL CALORIES CONSUMED: " + 0);
        }


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("MM / dd / yyyy");
        String strDate = "Today's Date : " + mdformat.format(calendar.getTime());
        mTodaysDate.setText(strDate);

        mTotalCaloriesTextView.setText(getFoodFromDB());
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

    @Override
    public void showFoodItem(String foodItem) {

    }

    @Override
    public void saveFoodItem(String foodItem) {

    }


    @Override
    public void showDays(Days daysRecord) {

        db.updateDays(daysRecord);

        Log.d("showCalories", daysRecord + "");
        mTotalCaloriesTextView.setText("TOTAL CALORIES CONSUMED: " + daysRecord.getCaloriesConsumed());
        db.close();
    }

    @Override
    public void refresh() {
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_item, menu);
        inflater.inflate(R.menu.menu_photo, menu);
        ButterKnife.bind(this);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_photo:
                Log.d("menuItem", item.getItemId()+"");
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


    private void openAddItemDialog() {
        LayoutInflater inflater = LayoutInflater.from(MealsActivity.this);
        View subView = inflater.inflate(R.layout.fragment_add_item, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add food item");
        builder.setView(subView);

        final EditText subEditText = (EditText) subView.findViewById(R.id.foodInputEditText);
        final EditText secondEditText = (EditText) subView.findViewById(R.id.calorieInputEditText);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String foodItem = subEditText.getText().toString();
                String foodCalories = secondEditText.getText().toString();
                Integer intCalories = Integer.parseInt(foodCalories);

                Food food = new Food(1, foodItem, intCalories);
                db.logFood(food);
                mTotalCaloriesTextView.setText(getFoodFromDB());
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

    @Override
    public void onClick(View view) {
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
        return String.valueOf(totalCalories);
    }
}





