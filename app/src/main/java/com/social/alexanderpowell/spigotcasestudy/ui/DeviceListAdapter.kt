package com.social.alexanderpowell.spigotcasestudy.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.social.alexanderpowell.spigotcasestudy.R
import com.social.alexanderpowell.spigotcasestudy.persistence.Device

class DeviceListAdapter internal constructor(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var rows = mutableListOf<Pair<String, String>>()

    companion object {
        const val DEVICE_VIEW_TYPE = 1
        const val PARAMETER_VIEW_TYPE = 2
    }

    inner class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconImageView: ImageView = itemView.findViewById(R.id.icon_image_view)
        private val keyTextView: TextView = itemView.findViewById(R.id.key)
        private val valueTextView: TextView = itemView.findViewById(R.id.value)
        fun bind (position: Int) {
            val recyclerViewModel = rows[position]
            when (position) {
                0 -> {
                    iconImageView.setBackgroundResource(R.drawable.ic_baseline_perm_identity_24)
                }
                1 -> {
                    iconImageView.setBackgroundResource(R.drawable.ic_baseline_perm_device_information_24)
                }
                2 -> {
                    iconImageView.setBackgroundResource(R.drawable.ic_baseline_android_24)
                }
            }
            keyTextView.text = recyclerViewModel.first
            valueTextView.text = recyclerViewModel.second
        }
    }

    inner class ParameterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val keyTextView: TextView = itemView.findViewById(R.id.key)
        private val valueTextView: TextView = itemView.findViewById(R.id.value)
        fun bind (position: Int) {
            val recyclerViewModel = rows[position]
            keyTextView.text = recyclerViewModel.first
            valueTextView.text = recyclerViewModel.second
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DEVICE_VIEW_TYPE) {
            val itemView = inflater.inflate(R.layout.recyclerview_device_item, parent, false)
            DeviceViewHolder(itemView)
        } else {// if (viewType == PARAMETER_VIEW_TYPE) {
            val itemView = inflater.inflate(R.layout.recyclerview_parameter_item, parent, false)
            ParameterViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position <= 2) {
            (holder as DeviceViewHolder).bind(position)
        } else {
            (holder as ParameterViewHolder).bind(position)
        }
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

    override fun getItemViewType(position: Int): Int {
        return if (position <= 2) DEVICE_VIEW_TYPE else PARAMETER_VIEW_TYPE

    }
}