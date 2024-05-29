package com.example.weatherapp.fragments.home;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.data.Hour;
import com.example.weatherapp.data.HourlyAdapter;

import java.util.ArrayList;

public class Forecast7DaysActivity extends AppCompatActivity {
    RecyclerView recyclerViewHourly;
//    ListView listView7days;
    HourlyAdapter hourlyAdapter;
    ArrayList<Hour> hourlyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forecast_7days);

        recyclerViewHourly = findViewById(R.id.forecast7daysRecyclerView);
        recyclerViewHourly.setLayoutManager(new LinearLayoutManager(this));

        hourlyList = getIntent().getParcelableArrayListExtra("hourlyList");
        hourlyAdapter = new HourlyAdapter(this, hourlyList);
        recyclerViewHourly.setAdapter(hourlyAdapter);
    }
}