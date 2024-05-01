package com.raja.myfyp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raja.myfyp.Interfaces.EditPoliceListner
import com.raja.myfyp.ModelClasses.PoliceDataClass
import com.raja.myfyp.databinding.ItemEditPoliceDetailsBinding

class PoliceListAdapter(
    val ctxt: Context,
    val policeList: List<PoliceDataClass>?
) : RecyclerView.Adapter<PoliceListAdapter.viewHolder>() {
    lateinit var binding : ItemEditPoliceDetailsBinding
    lateinit var editPoliceListner : EditPoliceListner

    inner class viewHolder(val binding: ItemEditPoliceDetailsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        binding = ItemEditPoliceDetailsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return viewHolder(binding)
    }

    fun setListner(listner : EditPoliceListner){
        this.editPoliceListner = listner
    }

    override fun getItemCount(): Int {
        return policeList?.size?:0
     }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val policeItem = policeList?.get(position)
        policeItem?.let {
            holder.binding.nameId.text = it.name
            holder.binding.passwordET.text = it.password
            holder.binding.addressId.text = it.address
            holder.binding.emailId.text = it.email
            holder.binding.rankET.text = it.rank

        }

        holder.binding.deleteBtn.setOnClickListener {
            editPoliceListner.deletePoliceItem(policeItem?.id)
        }

     }


}