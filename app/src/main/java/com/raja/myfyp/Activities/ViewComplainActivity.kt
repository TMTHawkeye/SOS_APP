package com.raja.myfyp.Activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.raja.myfyp.Adapters.ComplainsListAdapter
import com.raja.myfyp.ModelClasses.Complain
import com.raja.myfyp.databinding.ActivityViewComplainBinding

class ViewComplainActivity : BaseActivity() {
    lateinit var binding : ActivityViewComplainBinding
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    var adapter : ComplainsListAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityViewComplainBinding.inflate(layoutInflater)
         setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("ComplainData")

        viewAllComplains {
            if(it?.size!=0) {
                binding.recyclerComplains.visibility= View.VISIBLE
                binding.noDataId.visibility= View.GONE
                it?.let { complainsList ->
                    binding.recyclerComplains.layoutManager =
                        LinearLayoutManager(this@ViewComplainActivity)
                    adapter = ComplainsListAdapter(this@ViewComplainActivity, complainsList)
                    binding.recyclerComplains.adapter = adapter
                }
            }
            else{
                binding.recyclerComplains.visibility= View.GONE
                binding.noDataId.visibility= View.VISIBLE
            }
        }

        binding.backBtnImg.setOnClickListener {
            finish()
        }



    }

    fun viewAllComplains(callback: (ArrayList<Complain>?)->Unit){
        // Filter complaints based on current user's userId
        val currentUserId = firebaseAuth.currentUser?.uid
        val query = database.orderByChild("userId").equalTo(currentUserId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userComplaints = ArrayList<Complain>()
                for (snapshot in dataSnapshot.children) {
                    val complain = snapshot.getValue(Complain::class.java)
                    if (complain != null) {
                        userComplaints.add(complain)
                    }
                }
                callback.invoke(userComplaints)

             }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                Toast.makeText(this@ViewComplainActivity, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                callback.invoke(null)
            }
        })
    }
}