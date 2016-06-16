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
    private static final String TABLE_CALORIES = "calories";

    // STEPS column names
    private static final String KEY_STEPS_ID = "id";
    private static final String KEY_STEPS = "steps";
    private static final String KEY_STEPS_DAY = "day";

    // CALORIES Table - column names
    private static final String KEY_CALORIES_ID = "id";
    private static final String KEY_CALORIES = "calories";
    private static final String KEY_CALORIES_DAY = "day";

    // Table Create Statements
    // Steps table create statement
    private static final String CREATE_TABLE_STEPS = "CREATE TABLE "
            + TABLE_STEPS + "(" + KEY_STEPS_ID + " INTEGER PRIMARY KEY," + KEY_STEPS
            + " TEXT," + KEY_STEPS_DAY + " INTEGER" + ")";

    // Table Create Statements
    // Calories table create statement
    private static final String CREATE_TABLE_CALORIES = "CREATE TABLE "
            + TABLE_CALORIES + "(" + KEY_CALORIES_ID + " INTEGER PRIMARY KEY," + KEY_CALORIES
            + " TEXT," + KEY_CALORIES_DAY + " INTEGER" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_STEPS);
        db.execSQL(CREATE_TABLE_CALORIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALORIES);

        // create new tables
        onCreate(db);
    }

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

        Steps steps = new Steps();
        steps.setId(c.getInt(c.getColumnIndex(KEY_STEPS_ID)));
        steps.setStepsTaken((c.getInt(c.getColumnIndex(KEY_STEPS))));
        steps.setDate(c.getInt(c.getColumnIndex(KEY_STEPS_DAY)));

        return steps;
    }

    /*
 * getting all todos
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
                Steps steps = new Steps();
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


    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
