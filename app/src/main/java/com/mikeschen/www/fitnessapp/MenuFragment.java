package com.mikeschen.www.fitnessapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mikeschen.www.fitnessapp.Meals.MealsActivity;
import com.mikeschen.www.fitnessapp.main.MainActivity;
import com.mikeschen.www.fitnessapp.maps.MapsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MenuFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.homeMainButton) Button mHomeMainButton;
    @Bind(R.id.mapsMainButton) Button mMapsMainButton;
    @Bind(R.id.mealsMainButton) Button mMealsMainButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.bind(this, view);

        mHomeMainButton.setOnClickListener(this);
        mMapsMainButton.setOnClickListener(this);
        mMealsMainButton.setOnClickListener(this);

        handleBackgrounds();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.homeMainButton):
                Intent intent = new Intent();
                intent.setClass(getActivity(), MainActivity.class);
                getActivity().startActivity(intent);
                break;
            case (R.id.mapsMainButton):
                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), MapsActivity.class);
                getActivity().startActivity(intent2);
                break;
            case (R.id.mealsMainButton):
                Intent intent3 = new Intent();
                intent3.setClass(getActivity(), MealsActivity.class);
                getActivity().startActivity(intent3);
                break;
        }
    }

    public void handleBackgrounds() {
        if (getActivity() instanceof MainActivity) {
            mHomeMainButton.setBackgroundColor(Color.parseColor("#D32F2F"));
            mMapsMainButton.setBackgroundColor(Color.parseColor("#f32f34"));
            mMealsMainButton.setBackgroundColor(Color.parseColor("#f32f34"));
        } else if (getActivity() instanceof MapsActivity) {
            mHomeMainButton.setBackgroundColor(Color.parseColor("#f32f34"));
            mMapsMainButton.setBackgroundColor(Color.parseColor("#D32F2F"));
            mMealsMainButton.setBackgroundColor(Color.parseColor("#f32f34"));
        } else if (getActivity() instanceof MealsActivity) {
            mHomeMainButton.setBackgroundColor(Color.parseColor("#f32f34"));
            mMapsMainButton.setBackgroundColor(Color.parseColor("#f32f34"));
            mMealsMainButton.setBackgroundColor(Color.parseColor("#D32F2F"));
        }
    }
}
