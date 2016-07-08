package com.mikeschen.www.fitnessapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mikeschen.www.fitnessapp.models.Days;
import com.mikeschen.www.fitnessapp.models.Food;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 4;
    //v.1 DB had three tables, one for steps, one for calories burned, and one for calories consumed
    //v.2 Refactored DB so everything is compiled into one table
    //v.3 Added Food table to the database
    //v.4 Added height/weight database to be installed on first use of app

    // Database Name
    private static final String DATABASE_NAME = "savedDays";

    // Table Names
    private static final String TABLE_DAY = "day";
    private static final String TABLE_FOOD = "food";


    //DAYS COLUMN NAMES
    private static final String KEY_DAY_ID = "_id";
    private static final String KEY_STEPS = "steps";
    private static final String KEY_CALORIES_BURNED = "caloriesBurned";
    private static final String KEY_CALORIES_CONSUMED = "caloriesConsumed";
    private static final String KEY_DATE = "date";

    //FOOD COLUMN NAMES
    private static final String KEY_FOOD_ID = "id";
    private static final String KEY_FOOD_NAME = "foodName";
    private static final String KEY_FOOD_CALORIES = "foodCalories";


    // Table Create Statements

    //Days table create statement
    private static final String CREATE_TABLE_DAY = "CREATE TABLE "
            + TABLE_DAY + "(" + KEY_DAY_ID + " INTEGER PRIMARY KEY,"
            + KEY_STEPS + " INTEGER,"
            + KEY_CALORIES_BURNED + " INTEGER,"
            + KEY_CALORIES_CONSUMED + " INTEGER,"
            + KEY_DATE + " INTEGER" + ")";

    // Food table create statement
    private static final String CREATE_TABLE_FOOD = "CREATE TABLE "
            + TABLE_FOOD + "(" + KEY_FOOD_ID + " INTEGER PRIMARY KEY,"
            + KEY_FOOD_NAME + " TEXT,"
            + KEY_FOOD_CALORIES + " INTEGER" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_DAY);
        db.execSQL(CREATE_TABLE_FOOD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);

        // create new tables
        onCreate(db);
    }

    /*
    DAYS
     */
    /*
    *Logging data for the day
     */

    public long logDays(Days days) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STEPS, days.getStepsTaken());
        values.put(KEY_CALORIES_BURNED, days.getCaloriesBurned());
        values.put(KEY_CALORIES_CONSUMED, days.getCaloriesConsumed());
        values.put(KEY_DATE, days.getDate());

        //insert row
        long day_id = db.insert(TABLE_DAY, null, values);
        return day_id;
    }

    /*
    * Get single day record
    */
    public Days getDay(long day_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_DAY + " WHERE "
                + KEY_DAY_ID + " = " + day_id;

        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Days days = new Days(0, 0, 0, 0, "");
        days.setId(c.getInt(c.getColumnIndex(KEY_DAY_ID)));
        days.setStepsTaken((c.getInt(c.getColumnIndex(KEY_STEPS))));
        days.setCaloriesBurned((c.getInt(c.getColumnIndex(KEY_CALORIES_BURNED))));
        days.setCaloriesConsumed((c.getInt(c.getColumnIndex(KEY_CALORIES_CONSUMED))));
        days.setDate((c.getString(c.getColumnIndex(KEY_DATE))));
        return days;
    }

    /*
    * Get all day records
    */
    public List<Days> getAllDaysRecords() {
        List<Days> allDaysRecords = new ArrayList<Days>();
        String selectQuery = "SELECT * FROM " + TABLE_DAY;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Days days = new Days(1, 1, 1, 1, "");
                days.setId(c.getInt((c.getColumnIndex(KEY_DAY_ID))));
                days.setStepsTaken((c.getInt(c.getColumnIndex(KEY_STEPS))));
                days.setCaloriesBurned((c.getInt(c.getColumnIndex(KEY_CALORIES_BURNED))));
                days.setCaloriesConsumed((c.getInt(c.getColumnIndex(KEY_CALORIES_CONSUMED))));
                days.setDate((c.getString(c.getColumnIndex(KEY_DATE))));

                allDaysRecords.add(days);
            } while (c.moveToNext());
        }
        return allDaysRecords;
    }

    /*
    * Updating a day record
    */
    public int updateDays(Days days) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STEPS, days.getStepsTaken());
        values.put(KEY_CALORIES_BURNED, days.getCaloriesBurned());
        values.put(KEY_CALORIES_CONSUMED, days.getCaloriesConsumed());
        values.put(KEY_DATE, days.getDate());

        //updating row
        return db.update(TABLE_DAY, values, KEY_DAY_ID + " = ?",
                new String[]{String.valueOf(days.getId())});
    }

    /*
    * Deleting a day record
    */
    public void deleteDayRecord(long day_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DAY, KEY_DAY_ID + " = ?",
                new String[]{String.valueOf(day_id)});
    }

    /*
    * Deleting all day records
    */

    public void deleteAllDayRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_DAY);
    }


    /*
    * FOOD
    */
    /*
    * Logging food data
    */

    public long logFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FOOD_NAME, food.getItemName());
        values.put(KEY_FOOD_CALORIES, food.getCalories());

        //insert row
        long food_id = db.insert(TABLE_FOOD, null, values);
        return food_id;
    }

    /*
    * Get single food record
    */
    public Food getFood(long food_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_FOOD + " WHERE "
                + KEY_FOOD_ID + " = " + food_id;

        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Food food = new Food(0, null, 0);
        food.setItemId(c.getInt(c.getColumnIndex(KEY_FOOD_ID)));
        food.setItemName((c.getString(c.getColumnIndex(KEY_FOOD_NAME))));
        food.setCalories((c.getInt(c.getColumnIndex(KEY_FOOD_CALORIES))));
        return food;
    }

    /*
    * Get all food records
    */
    public List<Food> getAllFoodRecords() {
        List<Food> allFoodRecords = new ArrayList<Food>();
        String selectQuery = "SELECT * FROM " + TABLE_FOOD;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Food food = new Food(1, "", 0);
                food.setItemId(c.getInt((c.getColumnIndex(KEY_FOOD_ID))));
                food.setItemName((c.getString(c.getColumnIndex(KEY_FOOD_NAME))));
                food.setCalories((c.getInt(c.getColumnIndex(KEY_FOOD_CALORIES))));
                allFoodRecords.add(food);
            } while (c.moveToNext());
        }
        return allFoodRecords;
    }

    /*
    * Updating a day record
    */
    public int updateFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FOOD_NAME, food.getItemName());
        values.put(KEY_CALORIES_BURNED, food.getCalories());

        //updating row
        return db.update(TABLE_FOOD, values, KEY_FOOD_ID + " = ?",
                new String[]{String.valueOf(food.getItemId())});
    }

    /*
    *Deleting a day record
    */
    public void deleteFoodRecord(long food_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOOD, KEY_FOOD_ID + " = ?",
                new String[]{String.valueOf(food_id)});
    }

    /*
    * Deleting all day records
    */

    public void deleteAllFoodRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_FOOD);
    }

    /*
    * Close the database
    */
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

}