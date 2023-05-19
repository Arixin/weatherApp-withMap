package com.example.api_app;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url1 = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
    private String url2 = "?unitGroup=metric&key=GPRK6C3X3SC5EPKK2UUB5XZVC&contentType=json";
    TextView tvId1;
    TextView tvId2;
    TextView tvId3;
    TextView tvId4;
    TextView tvId5;
    SearchView sv1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvId1 = findViewById(R.id.textView2);
        tvId2 = findViewById(R.id.textView5);
        tvId3 = findViewById(R.id.textView7);
        tvId4 = findViewById(R.id.textView9);
        tvId5 = findViewById(R.id.textView11);
        sv1 = findViewById(R.id.searchview);

        getData();
    }

    public String getInfo(){
        String position = sv1.getQuery().toString();

        return position;
    }

    public void goToActivity2 (View view){
        getData();

    }

    public void getData() {
        mRequestQueue = Volley.newRequestQueue(this);
        String city = getInfo();
        String url = url1 + city + url2;

        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG,response.toString());

                try {
                    Intent i = new Intent(MainActivity.this, MapActivity.class);
                    JSONObject jObj = new JSONObject(response);
                    String address = jObj.getString("address");
                    i.putExtra("address",address);
                    tvId1.setText(address);

                    JSONObject jObj5 = new JSONObject(response);
                    String lang = jObj5.getString("longitude");
                    i.putExtra("lang",lang);
                    JSONObject jObj6 = new JSONObject(response);
                    String lat = jObj6.getString("latitude");
                    i.putExtra("lat",lat);

                    JSONObject jObj1 = new JSONObject(response);
                    String desc = jObj1.getString("description");
                    tvId4.setText(desc);

                    JSONObject jObj2 = new JSONObject(response);
                    String timezone = jObj2.getString("timezone");
                    tvId5.setText(timezone);

                    JSONObject jObj3 = new JSONObject(response);
                    JSONObject date = jObj3.getJSONObject("currentConditions");
                    String datetime = date.getString("datetime");
                    tvId2.setText(datetime);

                    JSONObject jObj4 = new JSONObject(response);
                    JSONObject temp = jObj4.getJSONObject("currentConditions");
                    String temperature = temp.getString("temp");
                    i.putExtra("temperature",temperature);
                    tvId3.setText(temperature + " C");
                    startActivity(i);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Toast.makeText(getApplicationContext(), "Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }
}