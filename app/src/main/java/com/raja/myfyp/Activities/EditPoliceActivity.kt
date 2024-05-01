package com.raja.myfyp.Activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.raja.myfyp.Adapters.PoliceListAdapter
import com.raja.myfyp.Interfaces.EditPoliceListner
import com.raja.myfyp.ModelClasses.PoliceDataClass
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityEditPoliceBinding

class EditPoliceActivity : BaseActivity(), EditPoliceListner {
    private lateinit var database: DatabaseReference
    lateinit var binding: ActivityEditPoliceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPoliceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance().reference.child("PoliceData")

        getAllPolice { policeList ->
            policeList?.let {
                binding.recyclerPolice.layoutManager = LinearLayoutManager(this@EditPoliceActivity)
                val adapter = PoliceListAdapter(this@EditPoliceActivity, it)
                adapter.setListner(this)
                binding.recyclerPolice.adapter = adapter
            }
        }

    }

    fun getAllPolice(callback: (List<PoliceDataClass>?) -> Unit) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val policeList = mutableListOf<PoliceDataClass>()
                for (policeSnapshot in snapshot.children) {
                    val police = policeSnapshot.getValue(PoliceDataClass::class.java)
                    police?.let { policeList.add(it) }
                }
                callback(policeList)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }

    fun editPolice(policeId: String, newData: PoliceDataClass, callback: (Boolean) -> Unit) {
        database.child(policeId).setValue(newData)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener { exception ->
                callback(false)
            }
    }

     fun deletePolice(policeId: String, callback: (Boolean) -> Unit) {
        database.child(policeId).removeValue()
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener { exception ->
                callback(false)
            }
    }

    override fun deletePoliceItem(policeId: String?) {
        policeId?.let { policeId ->

            deletePolice(policeId) {
                if (it) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.deleted_successfully),
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.unableToDelete),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}