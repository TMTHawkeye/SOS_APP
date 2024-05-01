package com.raja.myfyp.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.raja.myfyp.Activities.MainActivity
import com.raja.myfyp.ModelClasses.UserData
import com.raja.myfyp.R
import com.raja.myfyp.databinding.FragmentProfileBinding
import io.paperdb.Paper

class ProfileFragment : Fragment() {
    lateinit var profileBinding: FragmentProfileBinding
    var userData : UserData?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileBinding = FragmentProfileBinding.inflate(inflater, container, false)


        return profileBinding.getRoot()
    }

    override fun onResume() {
        super.onResume()

        val ctxt = (requireContext() as MainActivity)
        ctxt.setTitleFrgment(requireContext().getString(R.string.manage_your_profile))

        userData = Paper.book().read("USER_DATA${FirebaseAuth.getInstance().currentUser?.uid}",null)
        userData?.let {
            profileBinding.editTextPhone.setText(it.phone)
            profileBinding.nameEt.setText(it.name)
            profileBinding.ageEt.setText(it.age)
            profileBinding.adressEt.setText(it.address)
            profileBinding.emailEt.setText(it.email)
        }

        setupTextChangeListeners()

    }

    private fun setupTextChangeListeners() {
        profileBinding.editTextPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                userData?.phone = s.toString()
                saveUserData(userData)
            }
        })

        profileBinding.nameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                userData?.name = s.toString()
                saveUserData(userData)
            }
        })

        profileBinding.ageEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                userData?.age = s.toString()
                saveUserData(userData)
            }
        })

        profileBinding.adressEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                userData?.address = s.toString()
                saveUserData(userData)
            }
        })

        profileBinding.emailEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                userData?.email = s.toString()
                saveUserData(userData)
            }
        })
    }

    private fun saveUserData(userData: UserData?) {
        userData?.let {
            Paper.book().write("USER_DATA${FirebaseAuth.getInstance().currentUser?.uid}", it)
        }
    }

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}