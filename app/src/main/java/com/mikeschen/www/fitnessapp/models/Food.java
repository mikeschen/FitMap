package com.mikeschen.www.fitnessapp.models;

import org.parceler.Parcel;

@Parcel
public class Food {
    long itemId;
    String itemName;
    String brandName;
    String itemDescription;
    double calories;
    double totalFat;
    double servingsPerContainer;
    double servingSizeQuantity;
    String servingSizeUnit;
    double servingWeightGrams;

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public Food() {}

    public Food(long itemId, String itemName, double calories){
            this.itemId = itemId;
            this.itemName = itemName;
            this.brandName = brandName;
            this.itemDescription = itemDescription;
            this.calories = calories;
            this.totalFat = totalFat;
            this.servingsPerContainer = servingsPerContainer;
            this.servingSizeQuantity = servingSizeQuantity;
            this.servingSizeUnit = servingSizeUnit;
            this.servingWeightGrams = servingWeightGrams;
        }

    public long getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public double getCalories() {
        return calories;
    }

    public double getTotalFat() {
        return totalFat;
    }

    public double getServingsPerContainer() {
        return servingsPerContainer;
    }

    public double getServingSizeQuantity() {
        return servingSizeQuantity;
    }

    public String getServingSizeUnit() {
        return servingSizeUnit;
    }

    public double getServingWeightGrams() {
        return servingWeightGrams;
    }
}
