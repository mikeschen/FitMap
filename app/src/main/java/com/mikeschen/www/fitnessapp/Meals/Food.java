package com.mikeschen.www.fitnessapp.Meals;

public class Food {
    String itemId;
    String itemName;
    String brandName;
    String itemDescription;
    double calories;
    double totalFat;
    double servingsPerContainer;
    double servingSizeQuantity;
    String servingSizeUnit;
    double servingWeightGrams;

    public Food() {}
    public Food(String itemId, String itemName, double calories) {
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

    public String getItemId() {
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
