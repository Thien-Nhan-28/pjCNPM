package com.example.weatherapp.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.R;
import com.example.weatherapp.Thoitiet;
import com.example.weatherapp.data.CustomAdapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);
        Anhxa();
        Intent intent = getIntent();
        // 3. Lấy tọa độ của thành phố muốn hiển thị từ activity trước đó.
        String locate = intent.getStringExtra("name");
        if (locate == null || locate.isEmpty()) {
            Get7DayData("Saigon");
        } else {
            Get7DayData(locate);
        }
        // 8. nhấn biểu tượng quay lại trang thông tin thời tiết trong ngày khi đã xem xong thông tin trong tuần.
        back.setOnClickListener(new View.OnClickListener() {
            // 9. hàm onBackPressed() để chuyển về activity trước đó.
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // 2. Khởi tạo các thành phần trong trang giao diện
    private void Anhxa() {
        back = findViewById(R.id.back);
        nameCity = findViewById(R.id.textViewNameCity);
        lv = findViewById(R.id.listView);
        manThoitiet = new ArrayList<>();
        // khỏi tạo ra CustomAdapter, lớp này nhằm gán dữ liệu từ danh sách thời tiết sang giao diện item_lv
        customAdapter = new CustomAdapter(WeekActivity.this, manThoitiet);
        lv.setAdapter(customAdapter);
    }

    // 4. khai báo url của API và kết nối với API để nhận về dữ liệu qua hàm get7DayData().
    private void Get7DayData(String locate) {
        RequestQueue requestQueue = Volley.newRequestQueue(WeekActivity.this);
        String url = "http://api.weatherapi.com/v1/forecast.json?key=29931f8e9e1745d6b3882750241005&q=" + locate + "&days=7";
        Log.d("Ket qua", "Request URL: " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectcity = jsonObject.getJSONObject("location");
                            String name = jsonObjectcity.getString("name");
                            nameCity.setText(name);

                            JSONObject jsonObjectForecast = jsonObject.getJSONObject("forecast");
                            JSONArray jsonArray = jsonObjectForecast.getJSONArray("forecastday");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjecItem = jsonArray.getJSONObject(i);
                                String ngay = jsonObjecItem.getString("date");

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = simpleDateFormat.parse(ngay);
                                simpleDateFormat.applyPattern("EEEE yyyy-MM-dd");
                                String day = simpleDateFormat.format(date);

                                JSONObject jsonObjecTemp = jsonObjecItem.getJSONObject("day");
                                String max = jsonObjecTemp.getString("maxtemp_c");
                                String min = jsonObjecTemp.getString("mintemp_c");

                                String nhietdoMax = String.valueOf((int) Double.parseDouble(max));
                                String nhietdoMin = String.valueOf((int) Double.parseDouble(min));

                                JSONObject jsonObjectCondition = jsonObjecTemp.getJSONObject("condition");
                                String status = jsonObjectCondition.getString("text");
                                String icon = "https:" + jsonObjectCondition.getString("icon");
                                // 5. gán các trường dữ liệu với các thành phần của giao diện ở bước 2.
                                // 6. thêm các đối tượng Thoitiet vừa tạo vào danh sách thông tin thời tiết
                                manThoitiet.add(new Thoitiet(day, status, icon, nhietdoMax, nhietdoMin));
                            }
                            // 7. hệ thống cập nhật danh sách thông tin thời tiết vào CustomAdapter qua hàm getView() để hiện thị ra giao diện.
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
