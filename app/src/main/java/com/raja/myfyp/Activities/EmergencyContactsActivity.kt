package com.raja.myfyp.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.raja.myfyp.Adapters.AddContactAdapter
import com.raja.myfyp.ModelClasses.Contact
import com.raja.myfyp.databinding.ActivityEmergencyContactsBinding
import io.paperdb.Paper

class EmergencyContactsActivity : BaseActivity() {
    lateinit var binding: ActivityEmergencyContactsBinding
    private lateinit var adapter: AddContactAdapter
    private val contactList = mutableListOf<Contact>()
     var emergencyContactList = ArrayList<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergencyContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        contactList.addAll(listOf(Contact("",""),Contact("",""),Contact("","")))

        adapter = AddContactAdapter(contactList)
        binding.contactsRV.layoutManager = LinearLayoutManager(this)
        binding.contactsRV.adapter = adapter

        binding.addAnotherAccId.setOnClickListener {
            if (adapter.contactList?.size!! < 5) {
                adapter.addContact()
            }
            else{
                Toast.makeText(this@EmergencyContactsActivity, "Maximum contacts have been entered!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.submitBtn.setOnClickListener {
            val invalidPosition = validateContacts()
            if (invalidPosition == -1) {
                Paper.book().write("EMERGENCY_CONTACTS",adapter.getContacts())
                Toast.makeText(this@EmergencyContactsActivity, "Contacts have been added!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@EmergencyContactsActivity,SOSSettingActivity::class.java))
                finish()
            } else {
                 val errorMessage = "Please fill all contact fields!"
//                Toast.makeText(this@EmergencyContactsActivity, errorMessage, Toast.LENGTH_SHORT).show()
                val layoutManager = binding.contactsRV.layoutManager as LinearLayoutManager
                layoutManager.scrollToPositionWithOffset(invalidPosition, 0)
                adapter.updateErrorForPosition(invalidPosition, errorMessage)
            }
        }

        binding.backBtnImg.setOnClickListener {
            finish()
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

    }

    private fun validateContacts(): Int {
        for (i in 0 until adapter.itemCount) {
            val viewHolder = binding.contactsRV.findViewHolderForAdapterPosition(i)
            if (viewHolder is AddContactAdapter.viewHolder) {
                val name = viewHolder.binding.editTextname.text.toString().trim()
                val phone = viewHolder.binding.editTextPhone.text.toString().trim()
                if (name.isEmpty() && phone.isEmpty()) {
                    return i // Return the position of the invalid contact
                }
            }
        }
        return -1
    }


}