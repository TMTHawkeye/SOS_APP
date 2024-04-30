package com.raja.myfyp.Activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.raja.myfyp.ModelClasses.UserData
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityManageProfileBinding
import io.paperdb.Paper

class ManageProfileActivity : BaseActivity() {
    lateinit var profileBinding: ActivityManageProfileBinding
    var userData: UserData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileBinding = ActivityManageProfileBinding.inflate(layoutInflater)
        setContentView(profileBinding.root)

        profileBinding.updateBtn.setOnClickListener {
            saveUserData()
            Toast.makeText(this@ManageProfileActivity, "Profile Updated Successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }

        profileBinding.backBtnImg.setOnClickListener {
            finish()
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

    }

    override fun onResume() {
        super.onResume()

        userData = Paper.book().read("USER_DATA", null)
        userData?.let {
            profileBinding.editTextPhone.setText(it.phone)
            profileBinding.editTextname.setText(it.name)
            profileBinding.editTextage.setText(it.age)
            profileBinding.editTextaddress.setText(it.address)
            profileBinding.editTextemail.setText(it.email)
        }
    }

    private fun saveUserData() {
        userData?.apply {
            phone = profileBinding.editTextPhone.text.toString()
            name = profileBinding.editTextname.text.toString()
            age = profileBinding.editTextage.text.toString()
            address = profileBinding.editTextaddress.text.toString()
            email = profileBinding.editTextemail.text.toString()
        }
        userData?.let {
            Paper.book().write("USER_DATA", it)
        }
    }
}
