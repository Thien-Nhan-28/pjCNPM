package com.example.weatherapp.fragments.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.data.CustomAdapter;
import com.example.weatherapp.R;
import com.example.weatherapp.data.Thoitiet;
import com.example.weatherapp.data.Hour;
//import com.example.weatherapp.data.forecast7daysAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeekActivity extends AppCompatActivity {
    String cityName = "";
    ImageView back;
    TextView nameCity;
    ListView lv;
    CustomAdapter customAdapter;
    ArrayList<Thoitiet> manThoitiet;
//    ArrayList<Hour> hourly;
//    forecast7daysAdapter forecast7days;
    RecyclerView forecast7days_LV;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);
        Anhxa();
        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
        Log.d("Ket qua", "City received: " + city);
        if (city == null || city.isEmpty()) {
            Get7DayData("SaiGon");
        } else {
            Get7DayData(city);
        }
        lv.setOnItemClickListener((parent, view, position, id) -> {
            Log.d("lv", "onCreate: " + position);
            Thoitiet selectedDay = manThoitiet.get(position);
            // Log thông tin của selectedDay
            Log.d("selectedDay", "onCreate: " + selectedDay);
            Log.d("selectedDay", "Day: " + selectedDay.getDay());
            Log.d("selectedDay", "Hourly size: " + selectedDay.getHourly().size());

            for (Hour hour : selectedDay.getHourly()) {
                Log.d("hour", "Time: " + hour.getTime() + ", Temp: " + hour.getTemp_c());
            }

            Intent intent1 = new Intent(WeekActivity.this, Forecast7DaysActivity.class);
            intent1.putParcelableArrayListExtra("hourlyList", selectedDay.getHourly());
            this.startActivity(intent1);
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void Anhxa() {
        back = findViewById(R.id.back);
        nameCity = findViewById(R.id.textViewNameCity);
        lv = findViewById(R.id.listView);
        manThoitiet = new ArrayList<>();
//        hourly = new ArrayList<>();
        customAdapter = new CustomAdapter(WeekActivity.this, manThoitiet);
        lv.setAdapter(customAdapter);
//        forecast7days_LV = findViewById(R.id.forecast7daysRecyclerView);
//        forecast7days = new forecast7daysAdapter(WeekActivity.this, manThoitiet);
//        forecast7days_LV.setAdapter(forecast7days);
    }

    private void Get7DayData(String city) {
        RequestQueue requestQueue = Volley.newRequestQueue(WeekActivity.this);
        String url = "http://api.weatherapi.com/v1/forecast.json?key=29931f8e9e1745d6b3882750241005&q=" + city + "&days=7";
        Log.d("Ket qua", "Request URL: " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Ket qua", "Response received");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectcity = jsonObject.getJSONObject("location");
                            String name = jsonObjectcity.getString("name");
                            nameCity.setText(name);

                            JSONObject jsonObjectForecast = jsonObject.getJSONObject("forecast");
                            JSONArray jsonArray = jsonObjectForecast.getJSONArray("forecastday");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectItem = jsonArray.getJSONObject(i);
                                String ngay = jsonObjectItem.getString("date");

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = simpleDateFormat.parse(ngay);
                                simpleDateFormat.applyPattern("EEEE dd-MM-yyyy");
                                String day = simpleDateFormat.format(date);

                                JSONObject jsonObjectTemp = jsonObjectItem.getJSONObject("day");
                                String max = jsonObjectTemp.getString("maxtemp_c");
                                String min = jsonObjectTemp.getString("mintemp_c");

                                String nhietdoMax = String.valueOf((int) Double.parseDouble(max));
                                String nhietdoMin = String.valueOf((int) Double.parseDouble(min));

                                JSONObject jsonObjectCondition = jsonObjectTemp.getJSONObject("condition");
                                String status = jsonObjectCondition.getString("text");
                                String icon = "https:" + jsonObjectCondition.getString("icon");
                                ArrayList<Hour> hourly = new ArrayList<>();
                                JSONArray jsonArrayHour = jsonObjectItem.getJSONArray("hour");
                                for (int j = 0; j < jsonArrayHour.length(); j++) {
                                    JSONObject jsonObjectItemHour = jsonArrayHour.getJSONObject(j);
                                    String time = jsonObjectItemHour.getString("time");
                                    String temp = jsonObjectItemHour.getString("temp_c");
                                    String temp_c = String.valueOf((int) Double.parseDouble(temp));
                                    String feelslike_c = jsonObjectItemHour.getString("feelslike_c");
                                    JSONObject jsonObjectConditionHour = jsonObjectItemHour.getJSONObject("condition");
                                    String iconHour = "https:" + jsonObjectConditionHour.getString("icon");
                                    hourly.add(new Hour(time, temp_c, iconHour, feelslike_c));
                                }
                                manThoitiet.add(new Thoitiet(day, status, icon, nhietdoMax, nhietdoMin, hourly));
                                Log.d("Ket qua", "Reponse: " + hourly);
//                                hourly.clear();
                            }
                            customAdapter.notifyDataSetChanged();
                        } catch (JSONException | ParseException e) {
                            Log.e("Ket qua", "JSON Parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Ket qua", "Volley error: " + error.toString());
                    }
                });
        requestQueue.add(stringRequest);
    }
}
