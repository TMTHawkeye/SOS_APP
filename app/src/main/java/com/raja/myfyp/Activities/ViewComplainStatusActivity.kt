package com.raja.myfyp.Activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.raja.myfyp.Adapters.ComplainListStatusPolice
import com.raja.myfyp.Adapters.ComplaintListStatusAdapter
import com.raja.myfyp.ModelClasses.Complain
import com.raja.myfyp.databinding.ActivityViewComplainStatusBinding

class ViewComplainStatusActivity : BaseActivity() {
    lateinit var binding: ActivityViewComplainStatusBinding
    private lateinit var database: DatabaseReference
     var intentFrom : String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewComplainStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance().getReference("ComplainData")
        intentFrom = intent?.getStringExtra("intentFrom")

        getAllComplains{
            if(it?.size!=0){
                binding.recyclerComplains.visibility= View.VISIBLE
                binding.noDataId.visibility= View.GONE
                binding.constrainHeadersId.visibility= View.VISIBLE

                it?.let { complainsList ->
                    binding.recyclerComplains.layoutManager =
                        LinearLayoutManager(this@ViewComplainStatusActivity)
                    if(intentFrom.equals("Admin")) {
                        binding.recyclerComplains.adapter = ComplaintListStatusAdapter(
                            this@ViewComplainStatusActivity,
                            complainsList
                         )

                    }
                    else{
                        binding.addressHeaderId.visibility=View.GONE
                        binding.complaintHeaderId.visibility=View.GONE
                        binding.recyclerComplains.adapter = ComplainListStatusPolice(this@ViewComplainStatusActivity,complainsList)
                    }
                }
            }
            else{
                binding.recyclerComplains.visibility= View.GONE
                binding.constrainHeadersId.visibility= View.GONE
                binding.noDataId.visibility= View.VISIBLE
            }
        }

        binding.backBtnImg.setOnClickListener {
            finish()
        }


    }

    fun getAllComplains(callback: (ArrayList<Complain>?)->Unit){
         database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val allComplaints = ArrayList<Complain>()
                for (snapshot in dataSnapshot.children) {
                    val complain = snapshot.getValue(Complain::class.java)
                    if (complain != null) {
                        allComplaints.add(complain)
                    }
                }
                callback.invoke(allComplaints)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                Toast.makeText(this@ViewComplainStatusActivity, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                callback.invoke(null)
            }
        })
    }
}