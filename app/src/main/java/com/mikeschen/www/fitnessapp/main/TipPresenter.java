package com.mikeschen.www.fitnessapp.main;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class TipPresenter implements MainInterface.Presenter {

    private final MainInterface.View mMainActivityView;

    public TipPresenter(MainInterface.View mainActivityView) {
        mMainActivityView = mainActivityView;
    }

    //Will implement Presenter interface
    @Override
    public void loadTip(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            Random randomNumberGenerator = new Random();
            int randomNumber = randomNumberGenerator.nextInt(jsonObject.length());
            String tip = jsonObject.getString(randomNumber + "");
            mMainActivityView.showTip(tip);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

