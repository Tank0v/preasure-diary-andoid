package com.example.android.bloodpressurediary.pressurelist

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.bloodpressurediary.database.Pressure

class PressureListAdapter(private  val listener: OnItemClickListener) : RecyclerView.Adapter<PressureViewHolder>() {

    var data = listOf<Pressure>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PressureViewHolder {
        return PressureViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PressureViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, position)

        holder.itemView.setOnClickListener {
            listener.onItemClick(item.id)
        }
    }
}