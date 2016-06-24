package com.mikeschen.www.fitnessapp.utils;

/**
 * Created by Ramon on 6/23/16.
 */
public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemValueChange(int position);
}
