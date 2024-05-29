package com.example.weatherapp.data

import android.os.Parcel
import android.os.Parcelable

data class Thoitiet(
    val day: String,
    val status: String,
    val image: String,
    val maxTerm: String,
    val minTerm: String,
    val hourly: ArrayList<Hour>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(Hour.CREATOR)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(day)
        parcel.writeString(status)
        parcel.writeString(image)
        parcel.writeString(maxTerm)
        parcel.writeString(minTerm)
        parcel.writeTypedList(hourly)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Thoitiet> {
        override fun createFromParcel(parcel: Parcel): Thoitiet {
            return Thoitiet(parcel)
        }

        override fun newArray(size: Int): Array<Thoitiet?> {
            return arrayOfNulls(size)
        }
    }
}