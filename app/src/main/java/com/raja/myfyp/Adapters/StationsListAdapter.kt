package com.raja.myfyp.Adapters

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raja.myfyp.ModelClasses.PoliceModel
import com.raja.myfyp.databinding.ItemNearestPoiceStationsBinding
import java.io.IOException
import java.util.Locale

class StationsListAdapter(val ctxt: Context, val stationsList: List<PoliceModel>?) :
    RecyclerView.Adapter<StationsListAdapter.viewHolder>() {
    lateinit var binding: ItemNearestPoiceStationsBinding

    inner class viewHolder(val binding: ItemNearestPoiceStationsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        binding = ItemNearestPoiceStationsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return viewHolder(binding)
    }

    override fun getItemCount(): Int {
        return stationsList?.size ?: 0
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        stationsList?.get(position)?.let {
            holder.binding.policeStationIdTv.text = it.policeStationName
            it?.policeStationLocation.let { geoPoint ->
                val geocoder = Geocoder(ctxt, Locale.getDefault())
                try {
                    val addresses: MutableList<Address>? =
                        geocoder.getFromLocation(geoPoint!!.latitude, geoPoint.longitude, 1)
                    if (addresses!!.isNotEmpty()) {
                        holder.binding.policeAddressId.text = addresses[0].getAddressLine(0)
                    } else {
                        holder.binding.policeAddressId.text = "Address not found"
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    holder.binding.policeAddressId.text = "Error getting address"
                }
            }


        }

        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${stationsList?.get(position)?.policeStationPhone}")
            ctxt.startActivity(intent)
        }


    }
}