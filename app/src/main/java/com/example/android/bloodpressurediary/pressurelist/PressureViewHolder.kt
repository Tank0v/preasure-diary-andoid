package com.example.android.bloodpressurediary.pressurelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.bloodpressurediary.R
import com.example.android.bloodpressurediary.database.Pressure
import java.time.format.DateTimeFormatter

class PressureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val measurementDateTime: TextView = itemView.findViewById(R.id.measurement_date_time)
    val systolicPressure: TextView = itemView.findViewById(R.id.systolic_pressure)
    val diastolicPressure: TextView = itemView.findViewById(R.id.diastolic_pressure)
    val pulse: TextView = itemView.findViewById(R.id.pulse)

    fun bind(item: Pressure, position: Int) {
        val context = itemView.context
        var formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val formattedDate = item.measurementDateTime.format(formatter)

        measurementDateTime.text = "${position + 1}. $formattedDate"
        systolicPressure.text = "${context.getString(R.string.systolic_pressure_textview_text)} ${item.systolicPressure}"
        diastolicPressure.text = "${context.getString(R.string.diastolic_pressure_textview_text)} ${item.diastolicPressure}"
        pulse.text = "${context.getString(R.string.pulse_textview_text)} ${item.pulse}"
    }

    companion object {
        fun from(parent: ViewGroup): PressureViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(R.layout.list_item_pressure, parent, false)
            return PressureViewHolder(view)
        }
    }
}