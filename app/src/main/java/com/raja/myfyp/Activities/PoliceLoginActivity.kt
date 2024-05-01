package com.raja.myfyp.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.raja.myfyp.ModelClasses.PoliceDataClass
import com.raja.myfyp.databinding.ActivityPoliceLoginBinding

class PoliceLoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityPoliceLoginBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPoliceLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance().getReference("PoliceData")


        binding.submitBtn.setOnClickListener {
            val email = binding.adminNameET.text.toString()
            val password = binding.adminPassET.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Query the database to find a user with matching email and password
                database.orderByChild("email").equalTo(email)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            var found = false
                            for (snapshot in dataSnapshot.children) {
                                val police = snapshot.getValue(PoliceDataClass::class.java)
                                if (police?.password == password) {
                                     found = true
                                    Toast.makeText(this@PoliceLoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(
                                        this@PoliceLoginActivity,
                                        PoliceDashboardActivity::class.java
                                    )
                                    startActivity(intent)
                                    break
                                }
                            }
                            if (!found) {
                                Toast.makeText(this@PoliceLoginActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle database error
                            Toast.makeText(this@PoliceLoginActivity, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}