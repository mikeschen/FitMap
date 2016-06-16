package com.mikeschen.www.fitnessapp.main;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

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

        String json;
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
                mMainActivityView.showTip(tip);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

