package com.example.weatherapp.data


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.weatherapp.R
import com.example.weatherapp.Thoitiet
import com.squareup.picasso.Picasso

class CustomAdapter(private val context: Context, private val arrayList: ArrayList<Thoitiet>) : BaseAdapter() {

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return arrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    //
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_lv, null)

        val thoitiet = arrayList[position]
        val txtDay = view.findViewById<TextView>(R.id.textViewNgay)
        val txtStatus = view.findViewById<TextView>(R.id.textViewStatus)
        val txtMaxTemp = view.findViewById<TextView>(R.id.textViewMaxTemp)
        val txtMinTemp = view.findViewById<TextView>(R.id.textViewMinTemp)
        val imgStatus = view.findViewById<ImageView>(R.id.imageViewStatus)

        txtDay.text = thoitiet.day
        txtStatus.text = thoitiet.status
        txtMaxTemp.text = String.format("%s\u00B0C", thoitiet.maxTerm)
        txtMinTemp.text = String.format("%s\u00B0C", thoitiet.minTerm)

        Picasso.get().load(thoitiet.image).into(imgStatus)

        return view
    }
}
