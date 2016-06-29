package com.mikeschen.www.fitnessapp;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mikeschen.www.fitnessapp.Meals.MealsActivity;
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

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.homeMainButton):

                break;
            case (R.id.mapsMainButton):
                Intent intent = new Intent();
                intent.setClass(getActivity(), MapsActivity.class);
                getActivity().startActivity(intent);
                break;
            case (R.id.mealsMainButton):
                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), MapsActivity.class);
                getActivity().startActivity(intent2);
                break;
        }
    }

    public void handleBackgrounds(View v) {
        if (v == mHomeMainButton) {
            mHomeMainButton.setBackgroundColor(Color.RED);
            mMapsMainButton.setBackgroundColor(Color.BLUE);
            mMealsMainButton.setBackgroundColor(Color.BLUE);

        } else if (v == mMapsMainButton) {
            mHomeMainButton.setBackgroundColor(Color.BLUE);
            mMapsMainButton.setBackgroundColor(Color.RED);
            mMealsMainButton.setBackgroundColor(Color.BLUE);

        } else if (v == mMealsMainButton) {
            mHomeMainButton.setBackgroundColor(Color.BLUE);
            mMapsMainButton.setBackgroundColor(Color.BLUE);
            mMealsMainButton.setBackgroundColor(Color.RED);
        }
    }
}
