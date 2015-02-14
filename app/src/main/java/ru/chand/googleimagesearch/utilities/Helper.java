package ru.chand.googleimagesearch.utilities;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by chandrav on 2/11/15.
 */
public class Helper {
    
    public final static String NUMBER_OF_RESULTS = "8";
    public final static String FRAGEMENT_PARAMETER_TITLE = "title";
    public final static String FRAGEMENT_PARAMETER_SEARCH_OPTIONS = "searchoptions";

    public final static String GOOGLE_IMAGE_SEARCH_PROTOCOL = "https";
    public final static String GOOGLE_IMAGE_SEARCH_DOMAIN = "ajax.googleapis.com";
    public final static String GOOGLE_IMAGE_SEARCH_PATH = "/ajax/services/search/images";



    public static  String UrlBuilder(String protocol, String domain, String path, Map options){
        String url = "";
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        Iterator it = options.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            qparams.add(new BasicNameValuePair(pairs.getKey().toString(), pairs.getValue().toString()));
        }

        try {
            URI uri = URIUtils.createURI(
                    protocol,
                    domain,
                    -1,
                    path,
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


}
