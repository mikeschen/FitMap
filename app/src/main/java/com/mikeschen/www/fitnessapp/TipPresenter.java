package com.mikeschen.www.fitnessapp;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by Ramon on 6/9/16.
 */
public class TipPresenter implements MainInterface.Presenter {

    private final MainInterface.View mMainActivityView;
    private Context mContext;

    public TipPresenter(MainInterface.View mainActivityView, Context context) {
        mMainActivityView = mainActivityView;
        mContext = context;
    }

    //Will implement Presenter interface
    @Override
    public void loadTip() {

        String json = null;
        try {
            InputStream is = mContext.getAssets().open("tips.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            try {
                JSONObject jsonObject = new JSONObject(json);
                Random randomNumberGenerator = new Random();
                int randomNumber = randomNumberGenerator.nextInt(jsonObject.length());
                String tip = jsonObject.getString(randomNumber + "");
                //Instead of mTipTextView, will make method
                mMainActivityView.showTip(tip);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

