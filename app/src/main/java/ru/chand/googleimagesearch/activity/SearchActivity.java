package ru.chand.googleimagesearch.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ru.chand.googleimagesearch.R;
import ru.chand.googleimagesearch.adaptor.PhotoAdaptor;
import ru.chand.googleimagesearch.model.Photo;
import ru.chand.googleimagesearch.utilities.Constants;


public class SearchActivity extends ActionBarActivity {

    private GridView gvResults;
    private ArrayList<Photo> photos;
    private SearchView searchView;
    private int nextIndex;
    private PhotoAdaptor photosAdaptor;

    public final static String GOOGLE_IMAGE_SEARCH_PROTOCOL = "https";
    public final static String GOOGLE_IMAGE_SEARCH_DOMAIN = "ajax.googleapis.com";
    public final static String GOOGLE_IMAGE_SEARCH_PATH = "/ajax/services/search/images";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        photos = new ArrayList<Photo>();
        photosAdaptor = new PhotoAdaptor(this, photos);
        nextIndex = 0;
        // Set a ToolBar to replace the ActionBar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
    }

    private void setupViews() {
        gvResults = (GridView) findViewById(R.id.gvResults);
        gvResults.setAdapter(photosAdaptor);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Photo p = photos.get(position);

                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                i.putExtra("photo", p);
                startActivity(i);
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doGoogleImageSearch(getCurrentOptions());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
        
    }
    
    
    private Map<String, String> getCurrentOptions(){
        String query = searchView.getQuery().toString();
        Map<String,String> options = new HashMap<>();
        options.put("v","1.0");
        options.put("q",query);
        options.put("rsz", Constants.NUMBER_OF_RESULTS);
        options.put("start", String.valueOf(nextIndex));
        return options;
    }

    
    


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    private String urlBuilder(Map options){
        String url = "";
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        Iterator it = options.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            qparams.add(new BasicNameValuePair(pairs.getKey().toString(), pairs.getValue().toString()));
        }

        try {
            URI uri = URIUtils.createURI(
                    GOOGLE_IMAGE_SEARCH_PROTOCOL,
                    GOOGLE_IMAGE_SEARCH_DOMAIN, 
                    -1,
                    GOOGLE_IMAGE_SEARCH_PATH,
                    URLEncodedUtils.format(qparams, "UTF-8"),
                    null
            );
            return uri.toString();

        } catch (URISyntaxException e) {
            Log.d("DEBUG", "Could not form url ");
            e.printStackTrace();
        }
        
        return url;
    }
    
    public void doGoogleImageSearch(Map options){

        if (!isNetworkAvailable()){
            Toast.makeText(getApplicationContext(), R.string.noconnection,Toast.LENGTH_SHORT).show();
            return;
        }
        String url = urlBuilder(options);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.i("DEBUG",response.toString());

                try {
                    JSONObject responseData = response.getJSONObject("responseData");
                    JSONArray photosJsonArray = responseData.getJSONArray("results");
                    photosAdaptor.clear();
                    photosAdaptor.addAll(Photo.FromJsonArray(photosJsonArray));

                } catch (JSONException ex){
                    ex.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", "Fetch timeline error: ");
                Toast.makeText(getApplicationContext(), R.string.cant_connect, Toast.LENGTH_SHORT);
            }
        });

    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


}
