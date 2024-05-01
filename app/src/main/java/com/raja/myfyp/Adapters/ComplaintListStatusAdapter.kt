package com.raja.myfyp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raja.myfyp.ModelClasses.Complain
import com.raja.myfyp.databinding.ItemComplainStatusBinding

class ComplaintListStatusAdapter(
    val ctxt: Context,
    val complainsList: ArrayList<Complain>?,
 ) : RecyclerView.Adapter<ComplaintListStatusAdapter.viewHolder>() {
    lateinit var binding : ItemComplainStatusBinding
    inner class viewHolder(val binding: ItemComplainStatusBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        binding = ItemComplainStatusBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return viewHolder(binding)
    }

    override fun getItemCount(): Int {
        return complainsList?.size?:0
     }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        val complainStatusItem = complainsList?.get(position)
        complainStatusItem?.let {
            holder.binding.nameId.text = it.userName
            holder.binding.emailId.text = it.userEmail
            holder.binding.addressId.text = it.userAddress
            holder.binding.complainId.text = it.complainDesc
            holder.binding.statusId.text = it.status
        }
    }
}