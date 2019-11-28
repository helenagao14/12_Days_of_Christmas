package com.example.a12daysofchristmas;

import android.nfc.Tag;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
	import java.util.Date;


public class MainActivity extends AppCompatActivity  {
    static final String API_BASE = "https://api.spotify.com/v1/search?q=NNNN&type=track%2Cartist&market=US&limit=10&offset=5";
    static final String endpoint =  "https://api.spotify.com/v1/me/player/play";
    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    Date christmasday = new Date();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("1");
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
System.out.println("2");
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTracks(getSearch());
            }
        });
        Button playbtn = findViewById(R.id.play);
        System.out.println("Before setting event");
        playbtn.setOnClickListener(unused -> getTracks(getSearch()));
        Button nextbtn = findViewById(R.id.next);
        Button previousbtn = findViewById(R.id.previous);

       TextView datetxt = findViewById(R.id.Date);
       datetxt.setText(date.toString());
        System.out.println("Before setting event");
        previousbtn.setOnClickListener(unused -> previousButton());



        System.out.println("Before setting event");
        nextbtn.setOnClickListener(unused -> nextButton());
        try {
            christmasday = formatter.parse("25-12-2019");

        } catch (ParseException e) {
            System.out.println(e);
        }



    }

        public Date addDays(Date date, int days)
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, days); //minus number would decrement the days
            return cal.getTime();
        }


    private void nextButton() {

        Button nextbtn = findViewById(R.id.next);
        Button previousbtn = findViewById(R.id.previous);


        if (addDays(date, 1).after(christmasday)) {
            nextbtn.setVisibility(View.GONE);
        } else {
            date = addDays(date, 1);
            if (date.before(addDays(christmasday, -12))) {
                date = addDays(christmasday, -12);
            }
            previousbtn.setVisibility(View.VISIBLE);
            TextView datetxt = findViewById(R.id.Date);
            datetxt.setText(date.toString());
            getTracks(getSearch());

        }


    }
    private void previousButton() {
        Button previousbtn = findViewById(R.id.previous);
        Button nextbtn = findViewById(R.id.next);
        if (addDays(date, -1).before(addDays(christmasday, -12))) {
            previousbtn.setVisibility(View.GONE);
            date = addDays(christmasday, -12);
            getTracks(getSearch());
        } else {
            date = addDays(date, -1);
            nextbtn.setVisibility(View.VISIBLE);
            TextView datetxt = findViewById(R.id.Date);
            datetxt.setText(date.toString());
            getTracks(getSearch());

        }


    }
    private String getSearch() {
        System.out.println("GtSearch");

        String[] searchNames = getResources().getStringArray(R.array.twelve_days);

        int diffInDays = (int)( (christmasday.getTime() - date.getTime())   / (1000 * 60 * 60 * 24) );
        if (diffInDays >= searchNames.length) {
            diffInDays = searchNames.length - 1;
        } else if (diffInDays < 0) {
            diffInDays = 0;
        }
        System.out.println(searchNames[diffInDays]);
        return searchNames[diffInDays];
    }
    private void getTracks(String search) {

        // Make any "loading" UI adjustments you like
        // Use WebApi.startRequest to fetch the games lists
        // In the response callback, call setUpUi with the received data
        Map<String, String> obj = new HashMap<>();

        //obj.put("q", search);
        //    obj.put("type", "track%2Cartist");
        //    obj.put("market", "US");
        //    obj.put("limit", "10");
        //    obj.put("offset", "5");
            obj.put("Accept", "application/json");
            obj.put("Content-Type", "application/json");

            sendRequest(obj,search);

    }


    void sendRequest(Map<String,String> obj,String search)
    {

        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                API_BASE.replaceFirst("NNNN",search),
                null,
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

                })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                SharedPreferences msharedPreferences = getSharedPreferences("SPOTIFY", 0);
                String token = msharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                for (String key : obj.keySet()){
                    headers.put(key,obj.get(key));
                }
                System.out.println(headers);
                return headers;
            }

        };




        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }


    private void Playmusic(String url)
    {

        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();
        array.put(url);
        try {
            //obj.put("uris", array);
            obj.put("context_uri",url);
        }catch (JSONException e)
        {
            System.out.println(e);
        }

        System.out.println(obj);
        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                endpoint,
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


                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        System.out.println(error);
                    }

                })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                SharedPreferences msharedPreferences = getSharedPreferences("SPOTIFY", 0);
                String token = msharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);

                return headers;
            }

        };




        // Add JsonObjectRequest to the RequestQueue
        System.out.println(jsonObjectRequest);
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
        Log.i("WebApi", result.toString());
        Spinner albums = findViewById(R.id.album);
        albums.setVisibility(View.VISIBLE);
        //albums.removeAllViews();
        ArrayList<String> arrayList = new ArrayList<>();
        Map<String, String> trackMap = new HashMap<>();
        try {


            JSONArray tracks = result.getJSONObject("artists").getJSONArray("items");
            for (int i = 0; i < tracks.length(); i++) {
                String id = tracks.getJSONObject(i).getString("id");
                String href = tracks.getJSONObject(i).getString("uri");
                String name = tracks.getJSONObject(i).getString("name");
                arrayList.add(name);
                trackMap.put(name,href);

            }
            System.out.println("Print trackmap");
            System.out.println(trackMap);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, arrayList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            albums.setAdapter(arrayAdapter);
            albums.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println(position);
                    String name = parent.getItemAtPosition(position).toString();
                    System.out.println(name);
                    String href=trackMap.get(name);
                    System.out.println(href);
                    Playmusic(href);
                }
                @Override
                public void onNothingSelected(AdapterView <?> parent) {
                }
            });
        }catch (JSONException e) {
            System.out.println(e);
            }

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
