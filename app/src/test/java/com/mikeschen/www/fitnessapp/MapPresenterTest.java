package com.mikeschen.www.fitnessapp;

import android.content.Context;

import com.google.android.gms.maps.SupportMapFragment;
import com.mikeschen.www.fitnessapp.main.MainActivity;
import com.mikeschen.www.fitnessapp.maps.MapInterface;
import com.mikeschen.www.fitnessapp.maps.MapPresenter;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by Ramon on 6/13/16.
 */
public class MapPresenterTest {

    private MapPresenter mMapPresenter;

    @Mock
    private SupportMapFragment mMapFragment;

    @Mock
    private MapInterface.View mMapView;

    @Mock
    private Context context;


    @Before
    public void setUpMapPresenter() {
        MockitoAnnotations.initMocks(this);
        context = new MainActivity();

        mMapPresenter = new MapPresenter(mMapView, context, mMapFragment);
    }

//    @Test
//    public void loadMapIntoView() {
//        mMapPresenter.loadMap();
//        verify(mMapView).showMap();
//    }
}


//since we are testing the presenter we don't need to have @Mock for the presenter-only write "private MapPresenter mMapPresenter;
