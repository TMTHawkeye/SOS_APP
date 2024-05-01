package com.raja.myfyp.BottomSheet

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.raja.myfyp.Activities.ChangePinActivity
import com.raja.myfyp.Activities.MainActivity
import com.raja.myfyp.Activities.ManageProfileActivity
import com.raja.myfyp.Activities.PinSettingsActivity
import com.raja.myfyp.Activities.SOSSettingActivity
import com.raja.myfyp.Interfaces.ExitListner
import com.raja.myfyp.ModelClasses.UserData
//import com.raja.myfyp.clearCredentials
import com.raja.myfyp.databinding.FragmentExitBottomSheetBinding
import io.paperdb.Paper

class ExitBottomSheet(val ctxt: MainActivity) : BottomSheetDialogFragment() {
    lateinit var binding :FragmentExitBottomSheetBinding
    lateinit var exitListener: ExitListner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExitBottomSheetBinding.inflate(layoutInflater)

        val userData = Paper.book().read<UserData>("USER_DATA${FirebaseAuth.getInstance().currentUser?.uid}")
        userData?.let {
            binding.userNameId.text=it.name
            binding.phoneId.text=it.phone
        }

        binding.logoutId.setOnClickListener {
//            clearCredentials(ctxt)
            exitListener.exitBottomSheetCallback(true)
            dismiss()
        }

        binding.manageProfileId.setOnClickListener {
            startActivity(Intent(ctxt,ManageProfileActivity::class.java))
            exitListener.exitBottomSheetCallback(false)
            dismiss()
        }

        binding.sosSettingsId.setOnClickListener {
            startActivity(Intent(ctxt,SOSSettingActivity::class.java).putExtra("intentFrom","fromMain"))
            exitListener.exitBottomSheetCallback(false)
            dismiss()
        }

        binding.pinSettingsId.setOnClickListener {
            startActivity(Intent(ctxt,ChangePinActivity::class.java))
            exitListener.exitBottomSheetCallback(false)
            dismiss()
        }


        return binding.root
    }

    fun setListener(listener: ExitListner) {
        this.exitListener = listener
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        exitListener.exitBottomSheetCallback(false)

    }

 }