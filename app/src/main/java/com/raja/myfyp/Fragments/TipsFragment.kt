package com.raja.myfyp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.raja.myfyp.Activities.MainActivity
import com.raja.myfyp.R
import com.raja.myfyp.databinding.FragmentTipsBinding

class TipsFragment : Fragment() {
    var tipsBinding: FragmentTipsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        tipsBinding = FragmentTipsBinding.inflate(inflater, container, false)
        return tipsBinding!!.getRoot()
    }

    companion object {
        fun newInstance(): TipsFragment {
            return TipsFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        val ctxt = (requireContext() as MainActivity)
        ctxt.setTitleFrgment(requireContext().getString(R.string.safety_tips))
    }
}