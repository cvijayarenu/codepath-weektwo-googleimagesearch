package ru.chand.googleimagesearch.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.chand.googleimagesearch.R;
import ru.chand.googleimagesearch.adaptor.PhotoAdaptor;
import ru.chand.googleimagesearch.model.Photo;



public class SearchActivity extends ActionBarActivity {

    private EditText etQuery ;
    private GridView gvResults;
    private ArrayList<Photo> photos;
    private PhotoAdaptor photosAdaptor;

    public final static String GOOGLE_IMAGE_URL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        photos = new ArrayList<Photo>();
        photosAdaptor = new PhotoAdaptor(this, photos);
        setupViews();
    }

    private void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
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
        return true;
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

    public void doImageSearch(View view) {
        if (isNetworkAvailable() == false){
            Toast.makeText(getApplicationContext(), R.string.noconnection,Toast.LENGTH_SHORT).show();
            return;
        }

        String url = GOOGLE_IMAGE_URL + etQuery.getText().toString() + "&rsz=8";
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
