package com.mikeschen.www.fitnessapp;

import com.mikeschen.www.fitnessapp.Meals.MealsInterface;
import com.mikeschen.www.fitnessapp.Meals.MealsPresenter;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MealsPresenterTest {

    @Mock
    private MealsInterface.View mMealsView;

    private MealsPresenter mMealsPresenter;


    @Before
    public void setUpMealsPresenter() {
        MockitoAnnotations.initMocks(this);
        mMealsPresenter = new MealsPresenter(mMealsView);
    }
//
//    @Test
//    public void searchFoods(String foodItem) {
//        mMealsPresenter.searchFoods(foodItem);
//        verify(mMealsView).displayFoodByItem(foods);
//    }
}
