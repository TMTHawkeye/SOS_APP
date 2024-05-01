package com.raja.myfyp.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.raja.myfyp.Adapters.PoliceListAdapter
import com.raja.myfyp.Interfaces.EditPoliceListner
import com.raja.myfyp.ModelClasses.PoliceDataClass
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityEditPoliceBinding

class EditPoliceActivity : BaseActivity(), EditPoliceListner {
    private lateinit var database: DatabaseReference
    lateinit var binding: ActivityEditPoliceBinding
    var adapter : PoliceListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPoliceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance().reference.child("PoliceData")

        getAllPolice { policeList ->
            if(policeList?.size!=0) {
                binding.recyclerPolice.visibility= View.VISIBLE
                binding.noDataId.visibility= View.GONE
                policeList.let {
                    binding.recyclerPolice.layoutManager =
                        LinearLayoutManager(this@EditPoliceActivity)
                    adapter = PoliceListAdapter(this@EditPoliceActivity, it)
                    adapter?.setListner(this)
                    binding.recyclerPolice.adapter = adapter
                }
            }
            else{
                binding.recyclerPolice.visibility= View.GONE
                binding.noDataId.visibility= View.VISIBLE
            }
        }

        binding.backBtnImg.setOnClickListener {
            finish()
        }


    }

    fun getAllPolice(callback: (ArrayList<PoliceDataClass>?) -> Unit) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val policeList = ArrayList<PoliceDataClass>()
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



     fun deletePolice(policeId: String, callback: (Boolean) -> Unit) {
        database.child(policeId).removeValue()
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener { exception ->
                callback(false)
            }
    }

    override fun deletePoliceItem(policeId: String?, position: Int) {
        policeId?.let { policeId ->

            deletePolice(policeId) {
                if (it) {
                    adapter?.let {
                        it.policeList?.removeAt(position)
                        it.notifyDataSetChanged()
                    }
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

    override fun editPolice(police: PoliceDataClass?, position: Int) {
        startActivity(Intent(this@EditPoliceActivity,AddPoliceActivity::class.java).putExtra("policeItem",police))
        finish()
    }
}