import org.mockito.Mock;

import com.mikeschen.www.fitnessapp.main.MainActivity;
import com.mikeschen.www.fitnessapp.main.MainInterface;
import com.mikeschen.www.fitnessapp.main.TipPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by Ramon on 6/13/16.
 */
public class TipPresenterTest {

    @Mock
    private TipPresenter mTipPresenter;

    @Mock
    private MainInterface.View mTipView;

    @Mock
    private MainActivity mMainActivity;

    @Before
    public void setUpTipPresenter() {
        MockitoAnnotations.initMocks(this);
        mMainActivity = new MainActivity();

        mTipPresenter = new TipPresenter(mTipView);
    }

//package com.mikeschen.www.fitnessapp;
//
//import com.mikeschen.www.fitnessapp.main.MainActivity;
//import com.mikeschen.www.fitnessapp.main.MainInterface;
//import com.mikeschen.www.fitnessapp.main.TipPresenter;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.mockito.Matchers.any;
//import static org.mockito.Mockito.verify;
//
///**
// * Created by Ramon on 6/13/16.
// */
//public class TipPresenterTest {
//
//    @Mock
//    private TipPresenter mTipPresenter;
//
//    @Mock
//    private MainInterface.View mTipView;
//
//    @Mock
//    private MainActivity mMainActivity;
//
//    @Before
//    public void setUpTipPresenter() {
//        MockitoAnnotations.initMocks(this);
//        mMainActivity = new MainActivity();
//
//        mTipPresenter = new TipPresenter(mTipView, mMainActivity);
//    }
//
//    @Test
//    public void loadTipsIntoView() {
//        mTipPresenter.loadTip();
//        verify(mTipView).showTip(any(String.class));
//    }

}
//}
