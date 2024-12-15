package com.example.rmmconlineexam.api;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.example.rmmconlineexam.util.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class PostTask extends AsyncTask<JSONObject, String, String> {

    private PostCallback callback;
    private String errorMessage = "";
    private String apiRequest;
    private Context context;


    public PostTask(Context context, PostCallback callback, String errorMessage, String apiRequest) {
        this.context = context;
        this.callback = callback;
        this.apiRequest = apiRequest;
        this.errorMessage = errorMessage;
    }

    @Override
    protected String doInBackground(JSONObject... params) {
        JSONObject postData = params[0];
        Log.d("post", postData.toString());
        String response = "";
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(ApiAddress.url + apiRequest);
            Log.d("PostTask", "URL: " + url.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

            if (SessionManager.getInstance(context).getToken() != null) {
                urlConnection.setRequestProperty("Authorization", "Bearer " + SessionManager.getInstance(context).getToken());
            } else if (postData.has("token")) {
                urlConnection.setRequestProperty("Authorization", "Bearer " + postData.getString("token"));
            }

            // Write request body
            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.writeBytes(postData.toString());
            outputStream.flush();
            outputStream.close();

            // Get the response code
            int responseCode = urlConnection.getResponseCode();
            Log.d("PostTask", "Response Code: " + responseCode);

            BufferedReader reader;
            if (responseCode >= 200 && responseCode < 300) {
                // Success responses (2xx)
                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            } else {
                // Error responses (e.g., 422)
                reader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
            }

            // Read the response
            String line;
            while ((line = reader.readLine()) != null) {
                response += line;
            }
            reader.close();


        } catch (Exception e) {
            Log.e("PostTask", "Error in doInBackground: " + e.getMessage());
            response = "error";
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("PostTask", "Response: " + result);


        if (result.contains("error")) {
            try {
                JSONObject jsonResponse = new JSONObject(result);
                String message = jsonResponse.getString("message");
                callback.onPostError(message);
            } catch (JSONException e) {
                Log.e("PostTask", "Error parsing error response: " + result);
                e.printStackTrace();
            }
        } else {

            try {

                callback.onPostSuccess(result);
            } catch (Exception e) {
                Log.e("PostTask", "Error parsing 422 response: " + e.getMessage());
                e.printStackTrace();
                callback.onPostError("An error occurred. Please try again.");
            }


        }
    }


}
