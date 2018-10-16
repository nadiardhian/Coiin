package com.apps.bandung.bandungcoin;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class Weather extends AppCompatActivity {

    private FusedLocationProviderClient client;
    public String lat;
    public String lon;

    private TextView mDate, mTemp, mCity, mWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        requestPermission();

        client = LocationServices.getFusedLocationProviderClient(this);
        mCity = (TextView) findViewById(R.id.textView3);
        mTemp = (TextView) findViewById(R.id.textView);
        mDate = (TextView) findViewById(R.id.textView2);
        mWeather = (TextView) findViewById(R.id.textView4);

        find_weather();

    }


    public void find_weather(){

        if (ActivityCompat.checkSelfPermission(Weather.this,ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            return;
        }

        client.getLastLocation().addOnSuccessListener(Weather.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                lat = Double.toString(location.getLatitude());
                Log.d("latitude", lat);

                lon = Double.toString(location.getLongitude());
                Log.d("longitude", lon);
                String url ="https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=01dc66faa405ef775c91e115cba3cd15&units=metric";

                JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            JSONObject main_object = response.getJSONObject("main");
                            JSONArray array = response.getJSONArray("weather");
                            JSONObject object = array.getJSONObject(0);
                            String temp = String.valueOf(main_object.getDouble("temp"));
                            String description = object.getString("description");
                            String city = response.getString("name");

                            //  t1_temp.setText(temp);
                            mCity.setText(city);

                            Calendar calendar = Calendar.getInstance();
                            Date date = calendar.getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy ");
                            String formatted_date = sdf.format(date);

                            mDate.setText(formatted_date);

                            double temp_int = Double.parseDouble(temp);
                            double centi = (temp_int - 32) /1.8000;
                            centi = Math.round(centi);
                            int i = (int)centi;
                            mTemp.setText(String.valueOf(temp_int));

                            mWeather.setText(description);


                        }catch(JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
                );
                RequestQueue queue = Volley.newRequestQueue(Weather.this);
                queue.add(jor);
            }
        });



    }


    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }
}


