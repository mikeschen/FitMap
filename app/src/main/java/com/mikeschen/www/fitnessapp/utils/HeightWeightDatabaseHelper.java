package com.mikeschen.www.fitnessapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.mikeschen.www.fitnessapp.models.Days;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ramon on 7/1/16.
 */
public class HeightWeightDatabaseHelper extends SQLiteOpenHelper {


    private Context context;
    private SQLiteDatabase myDataBase;

    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 1;
    //v.1 Prefilled database containing height and weight info to determine calories burned per step

    private static String DATABSE_PATH;
    private static String DATABASE_NAME = "hwdb.db";

    //Table name
    private static String TABLE_NAME = "heightweight";

    //Column names
    private static final String KEY_ID = "_id";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_STRIDE = "stride";
    private static final String KEY_CALORIES_BURNED= "calories";


    public HeightWeightDatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);
        this.context = context;

        DATABSE_PATH = "/data/data/" + context.getPackageName() + "/databases/";
    }


    public void createDatabase() throws IOException {
        boolean dbExist = checkDatabase();
        if (dbExist) {

        } else {
            this.getReadableDatabase();

            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error copying database");

            }
        }
    }

    private boolean checkDatabase() {
        SQLiteDatabase checkDB = null;

        try {
            String myPath = DATABSE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {

            //database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();
        }
        return checkDB != null;
    }

    private void copyDatabase() throws IOException {
        //Open local database as the input stream
        InputStream myInput = context.getAssets().open(DATABASE_NAME);

        //Path to the just created database
        String outFileName = DATABSE_PATH + DATABASE_NAME;

        //Open the empty database as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDatabase() throws SQLException {
        //Open the database
        String myPath = DATABSE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("HW DB", "did it work?");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Integer getCals(int weight, int stride) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + KEY_WEIGHT + " = " + weight + " and " + KEY_STRIDE + " = " + stride;

        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
        }
        Integer cals;
        cals = c.getInt(c.getColumnIndex(KEY_CALORIES_BURNED));
        return cals;
    }


}
