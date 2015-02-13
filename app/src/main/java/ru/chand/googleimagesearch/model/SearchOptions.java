package ru.chand.googleimagesearch.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ru.chand.googleimagesearch.utilities.Constants;

/**
 * Created by chandrav on 2/12/15.
 */
public class SearchOptions implements Serializable {
    
    private Map<String, String> options;
    
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

    public void reset(){
        options = new HashMap<>();
        options.put("v","1.0");
        options.put("rsz", Constants.NUMBER_OF_RESULTS);
        options.put("start", "0");
    }
    
    public Map<String, String > getAllOptions(){
        return options;
    }
}
