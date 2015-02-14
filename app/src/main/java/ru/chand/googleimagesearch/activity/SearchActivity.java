package ru.chand.googleimagesearch.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import ru.chand.googleimagesearch.R;
import ru.chand.googleimagesearch.adaptor.PhotoAdaptor;
import ru.chand.googleimagesearch.fragment.SearchOptionsDialog;
import ru.chand.googleimagesearch.model.Photo;
import ru.chand.googleimagesearch.model.SearchOptions;
import ru.chand.googleimagesearch.utilities.Helper;


public class SearchActivity extends ActionBarActivity implements SearchOptionsDialog.SearchOptionsDialogListener {

    private GridView gvResults;
    private ArrayList<Photo> photos;
    private SearchView searchView;
    private PhotoAdaptor photosAdaptor;
    private SearchOptions searchOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        photos = new ArrayList<Photo>();
        photosAdaptor = new PhotoAdaptor(this, photos);
        searchOptions = new SearchOptions();
        // Set a ToolBar to replace the ActionBar.
        setupViews();
        
    }

    private void setupViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
                searchOptions.setOption("q", query);
                doGoogleImageSearch(searchOptions.getAllOptions());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
        
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SearchOptionsDialog editNameDialog = SearchOptionsDialog.newInstance(getString(R.string.settingsTitle), searchOptions);
            editNameDialog.show(getSupportFragmentManager(), "fragment_edit_options");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    

    public void doGoogleImageSearch(Map options){

        if (!isNetworkAvailable()){
            Toast.makeText(getApplicationContext(), R.string.noconnection,Toast.LENGTH_SHORT).show();
            return;
        }
        String url = Helper.UrlBuilder(
                Helper.GOOGLE_IMAGE_SEARCH_PROTOCOL,
                Helper.GOOGLE_IMAGE_SEARCH_DOMAIN,
                Helper.GOOGLE_IMAGE_SEARCH_PATH,
                options);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
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


    @Override
    public void onFinishingSearchOptions(SearchOptions so) {
        this.searchOptions = so;
        doGoogleImageSearch(searchOptions.getAllOptions());
    }
}
