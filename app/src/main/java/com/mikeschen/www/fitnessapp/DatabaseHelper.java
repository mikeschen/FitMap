package com.mikeschen.www.fitnessapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "stepsAndCalories";

    // Table Names
    private static final String TABLE_STEPS = "steps";
    private static final String TABLE_CALORIES_BURNED = "caloriesBurned";
    private static final String TABLE_CALORIES_CONSUMED = "caloriesConsumed";

    // STEPS column names
    private static final String KEY_STEPS_ID = "id";
    private static final String KEY_STEPS = "steps";
    private static final String KEY_STEPS_DAY = "day";

    // CALORIES BURNED Table - column names
    private static final String KEY_CALORIES_BURNED_ID = "burnedId";
    private static final String KEY_CALORIES_BURNED = "calories";
    private static final String KEY_CALORIES_BURNED_DAY = "day";

    // CALORIES Table - column names
    private static final String KEY_CALORIES_CONSUMED_ID = "consumedId";
    private static final String KEY_CALORIES_CONSUMED = "caloriesConsumed";
    private static final String KEY_CALORIES_CONSUMED_DAY = "day";

    // Table Create Statements
    // Steps table create statement
    private static final String CREATE_TABLE_STEPS = "CREATE TABLE "
            + TABLE_STEPS + "(" + KEY_STEPS_ID + " INTEGER PRIMARY KEY," + KEY_STEPS
            + " TEXT," + KEY_STEPS_DAY + " INTEGER" + ")";

    // Calories burned table create statement
    private static final String CREATE_TABLE_CALORIES_BURNED = "CREATE TABLE "
            + TABLE_CALORIES_BURNED + "(" + KEY_CALORIES_BURNED_ID + " INTEGER PRIMARY KEY," + KEY_CALORIES_BURNED
            + " TEXT," + KEY_CALORIES_BURNED_DAY + " INTEGER" + ")";

    // Calories consumed table create statement
    private static final String CREATE_TABLE_CALORIES_CONSUMED = "CREATE TABLE "
            + TABLE_CALORIES_CONSUMED + "(" + KEY_CALORIES_CONSUMED_ID + " INTEGER PRIMARY KEY," + KEY_CALORIES_CONSUMED
            + " TEXT," + KEY_CALORIES_CONSUMED_DAY + " INTEGER" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_STEPS);
        db.execSQL(CREATE_TABLE_CALORIES_BURNED);
        db.execSQL(CREATE_TABLE_CALORIES_CONSUMED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALORIES_BURNED);
        db.execSQL("DROP TALBE IF EXISTS " + TABLE_CALORIES_CONSUMED);

        // create new tables
        onCreate(db);
    }


    /*
    STEPS
     */
    /*
 * Logging steps taken
 */
    public long logSteps(Steps steps) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STEPS, steps.getStepsTaken());
        values.put(KEY_STEPS_DAY, steps.getDate());

        // insert row
        long steps_id = db.insert(TABLE_STEPS, null, values);

        return steps_id;
    }

    /*
 * get single steps record
 */
    public Steps getSteps(long steps_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_STEPS + " WHERE "
                + KEY_STEPS_ID + " = " + steps_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Steps steps = new Steps(0, 0, 0);
        steps.setId(c.getInt(c.getColumnIndex(KEY_STEPS_ID)));
        steps.setStepsTaken((c.getInt(c.getColumnIndex(KEY_STEPS))));
        steps.setDate(c.getInt(c.getColumnIndex(KEY_STEPS_DAY)));

        return steps;
    }

    /*
 * getting all calories
 * */
    public List<Steps> getAllStepRecords() {
        List<Steps> allStepRecords = new ArrayList<Steps>();
        String selectQuery = "SELECT  * FROM " + TABLE_STEPS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Steps steps = new Steps(1, 1001, 345);
                steps.setId(c.getInt((c.getColumnIndex(KEY_STEPS_ID))));
                steps.setStepsTaken((c.getInt(c.getColumnIndex(KEY_STEPS))));
                steps.setDate(c.getInt(c.getColumnIndex(KEY_STEPS_DAY)));

                // adding to todo list
                allStepRecords.add(steps);
            } while (c.moveToNext());
        }

        return allStepRecords;
    }

    /*
 * Updating a steps record
 */
    public int updateSteps(Steps steps) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STEPS, steps.getStepsTaken());
        values.put(KEY_STEPS_DAY, steps.getDate());

        // updating row
        return db.update(TABLE_STEPS, values, KEY_STEPS_ID + " = ?",
                new String[] { String.valueOf(steps.getId()) });
    }

    /*
 * Deleting a steps record
 */
    public void deleteStepsRecord(long steps_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STEPS, KEY_STEPS_ID + " = ?",
                new String[] { String.valueOf(steps_id)});
    }

    public void deleteAllStepsRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_STEPS);
    }


    /*
    CALORIES BURNED

     */
    /*
* Logging calories BURNED
*/
    public long logCaloriesBurned(Calories calories) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CALORIES_BURNED, calories.getCalories());
        values.put(KEY_CALORIES_BURNED_DAY, calories.getDate());

        // insert row
        long calories_id = db.insert(TABLE_CALORIES_BURNED, null, values);

        return calories_id;
    }

    /*
 * get single calories burned record
 */
    public Calories getCaloriesBurned(long calories_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CALORIES_BURNED + " WHERE "
                + KEY_CALORIES_BURNED_ID + " = " + calories_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Calories calories = new Calories(0, 0, 0);
        calories.setId(c.getInt(c.getColumnIndex(KEY_CALORIES_BURNED_ID)));
        calories.setCalories((c.getInt(c.getColumnIndex(KEY_CALORIES_BURNED))));
        calories.setDate(c.getInt(c.getColumnIndex(KEY_CALORIES_BURNED_DAY)));

        return calories;
    }

    /*
 * getting all calories
 * */
    public List<Calories> getAllCaloriesBurnedRecords() {
        List<Calories> allCalorieRecords = new ArrayList<Calories>();
        String selectQuery = "SELECT  * FROM " + TABLE_CALORIES_BURNED;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Calories calories = new Calories(1, 1001, 345);
                calories.setId(c.getInt((c.getColumnIndex(KEY_CALORIES_BURNED_ID))));
                calories.setCalories((c.getInt(c.getColumnIndex(KEY_CALORIES_BURNED))));
                calories.setDate(c.getInt(c.getColumnIndex(KEY_CALORIES_BURNED_DAY)));

                // adding to todo list
                allCalorieRecords.add(calories);
            } while (c.moveToNext());
        }

        return allCalorieRecords;
    }

    /*
 * Updating a calorie record
 */
    public int updateCaloriesBurned(Calories calories) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CALORIES_BURNED, calories.getCalories());
        values.put(KEY_CALORIES_BURNED_DAY, calories.getDate());

        // updating row
        return db.update(TABLE_CALORIES_BURNED, values, KEY_CALORIES_BURNED_ID + " = ?",
                new String[] { String.valueOf(calories.getId()) });
    }

    /*
 * Deleting a calorie record
 */
    public void deleteCaloriesBurnedRecord(long calories_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CALORIES_BURNED, KEY_CALORIES_BURNED_ID + " = ?",
                new String[] { String.valueOf(calories_id)});
    }

    public void deleteAllCaloriesBurnedRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CALORIES_BURNED);
    }


    /*
    CALORIES CONSUMED
     */
    /*
* Logging calories consumed
*/
    public long logCaloriesConsumed(Calories calories) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CALORIES_CONSUMED, calories.getCalories());
        values.put(KEY_CALORIES_CONSUMED_DAY, calories.getDate());

        // insert row
        long calories_id = db.insert(TABLE_CALORIES_CONSUMED, null, values);
        Log.d("Inside DB Helper", "fires on save " + calories_id);

        return calories_id;
    }

    /*
 * get single calories consumed record
 */
    public Calories getCaloriesConsumed(long calories_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CALORIES_CONSUMED + " WHERE "
                + KEY_CALORIES_CONSUMED_ID + " = " + calories_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Calories calories = new Calories(0, 0, 0);
        calories.setId(c.getInt(c.getColumnIndex(KEY_CALORIES_CONSUMED_ID)));
        calories.setCalories((c.getInt(c.getColumnIndex(KEY_CALORIES_CONSUMED))));
        calories.setDate(c.getInt(c.getColumnIndex(KEY_CALORIES_CONSUMED_DAY)));

        return calories;
    }

    /*
 * getting all calories
 * */
    public List<Calories> getAllCalorieConsumedRecords() {
        List<Calories> allCalorieRecords = new ArrayList<Calories>();
        String selectQuery = "SELECT  * FROM " + TABLE_CALORIES_CONSUMED;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Calories calories = new Calories(1, 1001, 345);
                calories.setId(c.getInt((c.getColumnIndex(KEY_CALORIES_CONSUMED_ID))));
                calories.setCalories((c.getInt(c.getColumnIndex(KEY_CALORIES_CONSUMED))));
                calories.setDate(c.getInt(c.getColumnIndex(KEY_CALORIES_CONSUMED_DAY)));

                // adding to todo list
                allCalorieRecords.add(calories);
            } while (c.moveToNext());
        }

        return allCalorieRecords;
    }

    /*
 * Updating a calorie record
 */
    public int updateCaloriesConsumed(Calories calories) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CALORIES_CONSUMED, calories.getCalories());
        values.put(KEY_CALORIES_CONSUMED_DAY, calories.getDate());

        // updating row
        return db.update(TABLE_CALORIES_CONSUMED, values, KEY_CALORIES_CONSUMED_ID + " = ?",
                new String[] { String.valueOf(calories.getId()) });
    }

    /*
 * Deleting a calorie record
 */
    public void deleteCalorieConsumedRecord(long calories_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CALORIES_CONSUMED, KEY_CALORIES_CONSUMED_ID + " = ?",
                new String[] { String.valueOf(calories_id)});
    }

    public void deleteAllCaloriesConsumedRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CALORIES_CONSUMED);
    }


    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
