package ru.chand.googleimagesearch.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ru.chand.googleimagesearch.utilities.Helper;

/**
 * Search options to store all the info
 * Created by chandrav on 2/12/15.
 */
public class SearchOptions implements Serializable {
    
    public static final String VERSION = "v";
    public static final String NO_OF_RESULTS = "rsz";
    public static final String START = "start";
    public static final String IMAGE_SIZE = "imgsz";
    public static final String IMAGE_COLOR = "imgcolor";
    public static final String IMAGE_TYPE = "imgtype";
    public static final String SITE_FILTER = "as_sitesearch";
    
    private Map<String, String> options = new HashMap<>();
    
    public SearchOptions(){
        reset();
    }
    
    public void setOption(String key, String value){
        options.put(key, value);
    }
    
    public String getOption(String key){
        return options.get(key);
    }
    
    public void incrementPage(){
        options.put("start", String.valueOf( 
                Integer.parseInt(options.get("rsz")) +
                Integer.parseInt(options.get("start"))
        ));
        
    }
    
    public void delete(String key){
        options.remove(key);
    }

    public void reset(){
        options.put(VERSION,"1.0");
        options.put(NO_OF_RESULTS, Helper.NUMBER_OF_RESULTS);
        options.put(START, "0");
    }
    
    public Map<String, String > getAllOptions(){
        return options;
    }
}
