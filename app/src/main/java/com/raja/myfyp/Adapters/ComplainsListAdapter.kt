package com.raja.myfyp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raja.myfyp.ModelClasses.Complain
import com.raja.myfyp.databinding.ItemViewComplainBinding

class ComplainsListAdapter(
    val ctxt: Context,
    val complainsList: ArrayList<Complain>?
) : RecyclerView.Adapter<ComplainsListAdapter.viewHolder>() {
    lateinit var binding : ItemViewComplainBinding

    inner class viewHolder(val binding: ItemViewComplainBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        binding=ItemViewComplainBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return viewHolder(binding)
    }

    override fun getItemCount(): Int {
        return complainsList?.size?:0
     }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val complainItem = complainsList?.get(position)
        complainItem?.let {
            holder.binding.nameId.setText(it.userName)
            holder.binding.emailId.setText(it.userEmail)
            holder.binding.addressId.setText(it.userAddress)
            holder.binding.dateId.setText(it.date)
            holder.binding.complainET.setText(it.complainDesc)
        }
     }
}