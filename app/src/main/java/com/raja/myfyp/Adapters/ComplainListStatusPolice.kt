package com.raja.myfyp.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.raja.myfyp.Activities.ComplaintDescriptonActivity
import com.raja.myfyp.ModelClasses.Complain
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ItemViewComplainPoliceBinding

class ComplainListStatusPolice(
    val ctxt: Context,
    val complainsList: ArrayList<Complain>?
) : RecyclerView.Adapter<ComplainListStatusPolice.viewHolder>() {
    lateinit var binding: ItemViewComplainPoliceBinding

    inner class viewHolder(val binding: ItemViewComplainPoliceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        binding = ItemViewComplainPoliceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return viewHolder(binding)
    }

    override fun getItemCount(): Int {
        return complainsList?.size ?: 0
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val complainStatusItem = complainsList?.get(position)
        complainStatusItem?.let {
            holder.binding.nameId.text = it.userName
            holder.binding.emailId.text = it.userEmail
            setSpinner(holder,it)

            holder.binding.complainId.onItemSelectedListener= object :  AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    holder.binding.complainId.setSelection(p2)
                    val selectedItem = p0?.getItemAtPosition(p2).toString()
                    updateComplaintStatus(complainStatusItem,selectedItem)
                 }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
        }
        holder.binding.openBtn.setOnClickListener {
            ctxt.startActivity(Intent(ctxt,ComplaintDescriptonActivity::class.java).putExtra("complain",complainStatusItem))
        }
    }

    fun setSpinner(holder: viewHolder, complain: Complain) {

        val spinnerAdapter = ArrayAdapter.createFromResource(
            ctxt,
            R.array.spinner_items,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        )
        spinnerAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        holder.binding.complainId.adapter = spinnerAdapter
        when(complain.status){
            "Completed"->{
                holder.binding.complainId.setSelection(1)
            }
            "Under Process"->{
                holder.binding.complainId.setSelection(0)
            }
        }

    }

    private fun updateComplaintStatus(complaint: Complain, newStatus: String) {
        // Update the status of the complaint
        complaint.status = newStatus

        // Update the status in Firebase Realtime Database
        val databaseRef = FirebaseDatabase.getInstance().getReference("ComplainData")
        databaseRef.child(complaint.complainId ?: "").child("status").setValue(newStatus)
            .addOnSuccessListener {
//                 notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle failure to update status in Firebase
                Log.e("TAG_status_failed", "Failed to update status in Firebase: ${exception.message}")
            }
    }

}