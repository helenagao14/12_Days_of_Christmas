package com.example.a12daysofchristmas;

import android.os.Bundle;

import com.android.volley.Request;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        connect();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Button playbtn = findViewById(R.id.play);

    }
    private void connect(String search) {

        // Make any "loading" UI adjustments you like
        // Use WebApi.startRequest to fetch the games lists
        // In the response callback, call setUpUi with the received data
        JsonObject obj = new JsonObject();




        obj.addProperty("q", search);
        obj.addProperty("type", "track,artist");
        obj.addProperty("market", "US");
        obj.addProperty("limit", 10);
        obj.addProperty("offset", 5);
        obj.addProperty("Accept:", "application/json");
        obj.addProperty("Content-Type", "application/json");
        obj.addProperty("Authorization", "Bearer " +
                "BQCKl5nCOBq_sEPyTBeNHUe4NNcNmYj8BvsC-" +
                "O1b3mV5npP466DJaSMJdq38jd0GapA2r5_tSn0yZmkSj" +
                "Ouib4tT2sL8d3D1afvfPggdDEU4uc06RlAb6XaXHV5VOtJfZHM_c6A4za_yrAfDCPzDpwzQKtXC");


        WebApi.startRequest(this, WebApi.API_BASE, Request.Method.GET, obj, response -> {
            // Code in this handler will run when the request completes successfully
            // Do something with the response?
            //the response object will contain the response data as a JsonObject if the endpoint returns a result,
            setUpUi(response.getAsJsonObject());
        }, error -> {
            System.out.println("Does not work");
            // Code in this handler will run if the request fails
            // Maybe notify the user of the error?

        });
    }
    /**
     * Populates the games lists UI with data retrieved from the server.
     *
     * @param result parsed JSON from the server
     */
    private void setUpUi(final JsonObject result) {
        
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
