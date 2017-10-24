package com.example.nmarsahanov.medbrat;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NM on 15/10/17.
 */

public class MedAsyncTask extends AsyncTask<URL, Void, List<Medicine>> {


    private Context mContext;

    public MedAsyncTask (Context context){
        mContext = context;
    }
    /** Tag for the log messages */
    public static final String LOG_TAG = MedAsyncTask.class.getSimpleName();

    /** URL to query the Medicine dataset from the server */
    private static final String Medicine_GET_URL = "http://rlspro.ru/RlsShortList";

    AsyncResponse asyncResponse = null;
    @Override
    protected List<Medicine> doInBackground(URL... urls) {

        List<Medicine> medList = new ArrayList<>();
        //getting Device Id
        String android_id = Secure.getString( mContext.getContentResolver(),
                Secure.ANDROID_ID);

        Uri baseUri = Uri.parse(Medicine_GET_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        String mockID = "1111";
        //TODO ПОМЕНЯТЬ НА DEVid
        uriBuilder.appendQueryParameter("deviceId", mockID);

        // Create URL object
        URL url = createUrl(uriBuilder.toString());

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error with creating URL ** " + e.getMessage());
            return medList;
        }

        // Extract relevant fields from the JSON response and create an {@link Medicine} object
        medList = extractFeatureFromJson(jsonResponse);

        // Return the {@link Medicine} object as the result fo the {@link MedAsyncTask}
        return medList;
    }


    /**
     * Returns new URL object from the given string URL.
     */
    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL  ** ", exception);
            return null;
        }
        return url;
    }

    /**
     * Update the screen with the given earthquake (which was the result of the
     * {@link MedAsyncTask}).
     */
    @Override
    protected void onPostExecute(List<Medicine> medList) {
        if (medList == null) {
            return;
        }
        asyncResponse.processFinish(medList);
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if( urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }

        } catch (IOException e) {
            // TODO: Handle the exception
            Log.e(LOG_TAG , "conn err. Conn code is " + urlConnection.getResponseCode());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    /**
     * Return a map of med id and names by parsing out information
     * about the first medicine from the input orderJSON string.
     */
    private List<Medicine> extractFeatureFromJson(String orderJSON) {
        try {

            List<Medicine> medList = new ArrayList<>();

            if (TextUtils.isEmpty(orderJSON)){
                return null;
            }
            //JSONObject baseJsonResponse = new JSONObject(orderJSON);
            JSONArray medArray = new JSONArray(orderJSON);

            for(int i=0;i<medArray.length(); i++){

                JSONObject element = medArray.getJSONObject(i);
                // Extract out the order values

                long id = element.getLong("id");
                String name = element.getString("name");

                Medicine medicine = new Medicine(id, name);
                medList.add(medicine);
            }

            return medList;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the order JSON results", e);
        }
        return null;
    }

}
