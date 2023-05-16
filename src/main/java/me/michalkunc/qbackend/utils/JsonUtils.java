package me.michalkunc.qbackend.utils;

import org.json.JSONArray;

public class JsonUtils {

    public static boolean jsonContains(String val, JSONArray json) {
        boolean containsString = false;
        for (int i = 0; i < json.length(); i++) {
            String value = json.getString(i);
            if (value.equals(val)) {
                containsString = true;
                break;
            }
        }
        return containsString;
    }
    public static JSONArray removeFromJSON(String val, JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            String value = array.getString(i);
            if (value.equals(val)) {
                array.remove(i);
                break;
            }
        }
        return array;
    }


}
