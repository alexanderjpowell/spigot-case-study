package com.social.alexanderpowell.spigotcasestudy.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.social.alexanderpowell.spigotcasestudy.R
import com.social.alexanderpowell.spigotcasestudy.persistence.Device

class DeviceListAdapter internal constructor(context: Context) : RecyclerView.Adapter<DeviceListAdapter.DeviceViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var rows = mutableListOf<Pair<String, String>>()

    inner class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val keyTextView: TextView = itemView.findViewById(R.id.key)
        val valueTextView: TextView = itemView.findViewById(R.id.value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return DeviceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val value = rows[position]
        holder.keyTextView.text = value.first
        holder.valueTextView.text = value.second
    }

    internal fun setDevice(device: Device) {
        this.rows.clear()
        this.rows.add(Pair("Device ID", device.device_id))
        this.rows.add(Pair("Manufacturer", device.manufacturer))
        this.rows.add(Pair("Model", device.model))
        device.parameters.map {
            this.rows.add(Pair(it.key, it.value))
        }
        notifyDataSetChanged()
    }

    override fun getItemCount() = rows.size
}