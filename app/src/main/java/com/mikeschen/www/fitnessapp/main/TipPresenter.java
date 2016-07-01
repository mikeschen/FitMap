package com.mikeschen.www.fitnessapp.main;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class TipPresenter implements TipInterface.Presenter {

    private final TipInterface.View mTipView;

    public TipPresenter(TipInterface.View tipView) {
        mTipView = tipView;
    }

    //Will implement Presenter interface
    @Override
    public void loadTip(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            Random randomNumberGenerator = new Random();
            int randomNumber = randomNumberGenerator.nextInt(jsonObject.length());
            String tip = jsonObject.getString(randomNumber + "");
            mTipView.showTip(tip);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

