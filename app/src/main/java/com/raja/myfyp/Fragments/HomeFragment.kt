package com.raja.myfyp.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieDrawable
import com.raja.myfyp.Activities.CreateComplainActivity
import com.raja.myfyp.Activities.FakeCallActivity
import com.raja.myfyp.Activities.MainActivity
import com.raja.myfyp.Activities.MyLocationActivity
import com.raja.myfyp.Activities.SoSActivity
import com.raja.myfyp.Activities.ViewComplainActivity
import com.raja.myfyp.R
import com.raja.myfyp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var homeBinding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)

        homeBinding.lottieBgId.repeatCount = LottieDrawable.INFINITE
        homeBinding.lottieBgId.playAnimation()


        homeBinding.lottieBgId.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    activity, SoSActivity::class.java
                )
            )
        }

        homeBinding.sosTv.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    activity, SoSActivity::class.java
                )
            )
        }

        homeBinding.callLayout.setOnClickListener {
            startActivity(Intent(requireContext(),FakeCallActivity::class.java))
        }

        homeBinding.cardLocation.setOnClickListener {
            startActivity(Intent(requireContext(),MyLocationActivity::class.java))
        }

        homeBinding.createComplaintLayout.setOnClickListener {
            startActivity(Intent(requireContext(),CreateComplainActivity::class.java))
        }

        homeBinding.cardViewComplain.setOnClickListener {
            startActivity(Intent(requireContext(),ViewComplainActivity::class.java))
        }

        return homeBinding.getRoot()
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        val ctxt = (requireContext() as MainActivity)
        ctxt.setTitleFrgment(requireContext().getString(R.string.home))
    }





}