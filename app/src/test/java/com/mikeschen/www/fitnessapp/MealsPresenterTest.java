package com.mikeschen.www.fitnessapp;

import com.mikeschen.www.fitnessapp.Meals.MealsInterface;
import com.mikeschen.www.fitnessapp.Meals.MealsPresenter;
import com.mikeschen.www.fitnessapp.Meals.NutritionixService;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MealsPresenterTest {

    @Mock
    private MealsInterface.View mMealsView;

    private MealsPresenter mMealsPresenter;

    @Mock
    private NutritionixService mNutritionixService;


    @Before
    public void setUpMealsPresenter() {
        MockitoAnnotations.initMocks(this);
        mMealsPresenter = new MealsPresenter(mMealsView);
    }

//    @Test
//    public void searchFoodsTest() {
//        ArrayList<Food> foods = new ArrayList<>();
//        Food food = new Food(1, "apple", 10);
//        foods.add(food);
//        doReturn(foods).when(mNutritionixService).searchFoods(any(String.class), any(Callback.class));
//        //some statement with when .... something ... do/return somehting
//        //define a response in the test- define a list of food objects
//        //return a null object because we want to make sure app works and/or return any kind of object
//        mMealsPresenter.searchFoods("as");
//        //service
//
//        verify(mMealsView).displayFoodByItem(any(ArrayList.class));
//    }

//    @Test
//    public void searchUPC(String scanResult) {
//        mMealsPresenter.searchUPC(scanResult);
//        verify(mMealsView).displayFoodByUPC(scanResult);
//    }
}
