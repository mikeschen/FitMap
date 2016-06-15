package com.mikeschen.www.fitnessapp;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
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
    private Context context;



    @Before
    public void setUpTipPresenter() {
        MockitoAnnotations.initMocks(this);
        context = new MainActivity();

        mTipPresenter = new TipPresenter(mTipView, context);
    }

    @Test
    public void loadTipsIntoView() {
        mTipPresenter.loadTip();
        verify(mTipPresenter, times(1)).loadTip();
    }

}
