package com.rabbit.application.util;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by Rabbit on 2015/3/27.
 */
public class ActivityCollecter {

    private static ArrayList<Activity> activitiesList = new ArrayList<Activity>();

    public static void addAvtivty(Activity activity) {
        if (activity != null)
            activitiesList.add(activity);

    }

    public static void removeActivity(Activity activity) {
        if (activity != null)
            activitiesList.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activitiesList) {

            if (!activity.isFinishing()) {

                activity.finish();
            }
        }

    }
}
