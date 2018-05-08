package com.example.aggoetey.myapplication.discover.helpers;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

/**
 * Created by amoryhoste on 06/04/2018.
 */

public class KeyProvider {

    public static String getPlacesApiKey(Context context) {

        String key = null;

        try {
            ApplicationInfo app = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = app.metaData;
            key = bundle.getString("google_places_api_key");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return key;
    }

}
