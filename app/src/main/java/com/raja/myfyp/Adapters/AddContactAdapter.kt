package com.raja.myfyp.Adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raja.myfyp.Activities.EmergencyContactsActivity
import com.raja.myfyp.ModelClasses.Contact
import com.raja.myfyp.databinding.ItemContactBinding

class AddContactAdapter(var contactList: MutableList<Contact>?) :
    RecyclerView.Adapter<AddContactAdapter.viewHolder>() {
    lateinit var binding: ItemContactBinding
    var errorMessage: String? = null

    inner class viewHolder(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun getItemCount(): Int {
        return contactList?.size ?: 0
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val contact = contactList?.get(position)

        holder.binding.apply {
            errorMessage?.let {
                editTextname.error = errorMessage
                editTextPhone.error = errorMessage
            }

            // Add text change listeners
            editTextname.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // No implementation needed
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    contact?.name = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                    // No implementation needed
                }
            })
            editTextPhone.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // No implementation needed
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    contact?.phone = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                    // No implementation needed
                }
            })
        }



    }

    fun addContact() {
        contactList?.add(Contact("",""))
        notifyItemInserted(contactList?.size?.minus(1) ?: 0)
    }

    fun getContacts(): MutableList<Contact>{
        return contactList?: mutableListOf()
    }


    fun updateErrorForPosition(position: Int, errorMessage: String?) {
        this.errorMessage=errorMessage
        notifyItemChanged(position)
    }

}