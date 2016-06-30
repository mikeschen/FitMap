package com.mikeschen.www.fitnessapp;

import com.mikeschen.www.fitnessapp.main.StepCounterInterface;
import com.mikeschen.www.fitnessapp.main.StepCounterPresenter;
import com.mikeschen.www.fitnessapp.models.Days;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class StepCounterPresenterTest {

    @Mock
    private StepCounterInterface.View mStepCounterView;

    private StepCounterPresenter mStepCounterPresenter;

    @Before
    public void setUpStepCounterPresenter() {
        MockitoAnnotations.initMocks(this);
        mStepCounterPresenter = new StepCounterPresenter(mStepCounterView);
    }

    @Test
    public void loadSteps() {
        mStepCounterPresenter.loadSteps();
        verify(mStepCounterView).showSteps(any(Days.class));
    }

    @Test
    public void checkTimerAtMidnight() {
        long timeSinceMidnight = System.currentTimeMillis()%(24*60*60*1000);
        long lastMidnight = (System.currentTimeMillis()-timeSinceMidnight)/60000;
        mStepCounterPresenter.checkMidnight(lastMidnight);
        verify(mStepCounterView).buildNotification(0);
    }

    @Test
    public void checkTimerAtElevenThirty() {
        long timeSinceMidnight = System.currentTimeMillis()%(24*60*60*1000);
        long lastMidnight = (System.currentTimeMillis()-timeSinceMidnight)/60000;
        long lastElevenThirty = lastMidnight-30;
        mStepCounterPresenter.checkMidnight(lastElevenThirty);
        verify(mStepCounterView).buildNotification(0);
    }
}
