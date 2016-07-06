package com.mikeschen.www.fitnessapp;

public class Constants {
    public static final String GOOGLE_MAPS_KEY = BuildConfig.GOOGLE_MAPS_KEY;
    public static final String NUTRITIONIX_API_KEY = BuildConfig.NUTRITIONIX_API_KEY;
    public static final String NUTRITIONIX_APP_ID = BuildConfig.NUTRITIONIX_APP_ID;

    public static final String NUTRITIONIX_BASE_URL = "https://api.nutritionix.com/v1_1/";
    public static final String SEARCH_PATH_SEGMENT = "search";
    public static final String BRAND_PATH_SEGMENT = "brand";
    public static final String ITEM_PATH_SEGMENT = "item";
    public static final String CALORIES_FIELD_SEGMENT = "nf_calories";

    public static final String APPID_QUERY = "appId";
    public static final String APPKEY_QUERY = "appKey";
    public static final String UPC_QUERY = "upc";
    public static final String FIELDS_QUERY = "fields";
    public static final String RESULTS_QUERY = "results";

    public static final String RESULT_QUANTITY_DEFAULT = "0:20";
    public static final String QUERY_FIELD_DEFAULTS = "item_name,brand_name,item_id";

    public static final String PREFERENCES_CURRENT_STEPS_KEY = "currentSteps";
    public static final String PREFERENCES_CURRENT_CALORIES_BURNED_KEY = "caloriesBurned";
    public static final String PREFERENCES_CURRENT_CALORIES_CONSUMED_KEY = "caloriesConsumed";
    public static final String PREFERENCES_CURRENT_DATE = "date";


    public static final String PREFERENCES_STEPS_ID_KEY ="steps_id";
    public static final String PREFERENCES_LAST_KNOWN_STEPS_KEY = "lastKnownSteps";
    public static final String PREFERENCES_LAST_KNOWN_TIME_KEY = "time";

    public static final String PREFERENCES_CALORIES_ID_KEY = "calories_id";

    public static final String PREFERENCES_HOME = "myHome";
    public static final String PREFERENCES_WORK = "myWork";
    public static final String PREFERENCES_HEIGHT = "myHeight";
    public static final String PREFERENCES_WEIGHT = "myWeight";
}
