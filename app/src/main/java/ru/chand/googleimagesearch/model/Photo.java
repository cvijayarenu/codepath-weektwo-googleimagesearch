package ru.chand.googleimagesearch.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by chandrav on 2/10/15.
 */
public class Photo implements Serializable {
    public String imageId;
    public String tbUrl;
    public String url;
    public String title;

    public Photo(JSONObject photoJson){
        try {
            imageId = photoJson.getString("imageId");
            tbUrl = photoJson.getString("tbUrl");
            url = photoJson.getString("url");
            title = photoJson.getString("title");
        } catch (JSONException ex){
            //
        }
    }

    public static ArrayList<Photo> FromJsonArray(JSONArray photoArray){
        ArrayList<Photo> results = new ArrayList<Photo>();
        try {
            for(int i = 0; i < photoArray.length(); i++ ){
                Photo p = new Photo(photoArray.getJSONObject(i));
                results.add(p);
            }
        } catch (JSONException ex){
            //
        }
        return results;
    }
}
