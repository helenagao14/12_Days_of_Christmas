package com.example.a12daysofchristmas;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    static final String API_BASE = "https://api.spotify.com/v1/search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect(getSearch());
            }
        });
        Button playbtn = findViewById(R.id.play);
        System.out.println("Before setting event");
        playbtn.setOnClickListener(unused -> connect(getSearch()));

    }
    private String getSearch() {
        System.out.println("GtSearch");
        return "christmas";
    }
    private void connect(String search) {

        // Make any "loading" UI adjustments you like
        // Use WebApi.startRequest to fetch the games lists
        // In the response callback, call setUpUi with the received data
        JSONObject obj = new JSONObject();
        try {
            obj.put("q", search);
            obj.put("type", "track,artist");
            obj.put("market", "US");
            obj.put("limit", 10);
            obj.put("offset", 5);
            obj.put("Accept", "application/json");
            obj.put("Content-Type", "application/json");
            obj.put("Authorization", "Bearer " +
                    "BQDxIUumViaQXayd7alLjHQejbakpdRTsu_fZI76vkl19C4f88jz78eBqxqQI-uqF1IL-L8wUw5QVdQaRrUa-OReFZAcXmKbDGyVRkvuhuyUXGCWPy-VTJwgwmoXLKsLuAmdAQ0lCcrGu4z1jY-QwlMvm8V-");
       
            sendRequest(obj);

        } catch (JSONException e) {
            System.out.print(e);

        }


    }

    private void sendRequest(JSONObject obj)
    {

        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                API_BASE,
                obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with response

                        // Process the JSON

                            // Get the JSON array
                            //JSONArray array = response.getJSONArray("students");

                            // Loop through the array elements
                            System.out.println(response);
                            setUpUi(response);

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        System.out.println(error);
                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);


    }
    /**
     * Populates the games lists UI with data retrieved from the server.
     *
     * @param result parsed JSON from the server
     */
    private void setUpUi(final JSONObject result) {
        System.out.println("Actual result");
        System.out.println(result);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
