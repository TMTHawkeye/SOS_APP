package com.raja.myfyp.BottomSheet

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.raja.myfyp.Adapters.StationsListAdapter
import com.raja.myfyp.Interfaces.SelectedPoliceStationListner
import com.raja.myfyp.ModelClasses.PoliceModel
import com.raja.myfyp.databinding.FragmentNearbyPoliceStationBottomSheetBinding

class NearbyPoliceStationBottomSheet  : BottomSheetDialogFragment() {
  lateinit var binding:  FragmentNearbyPoliceStationBottomSheetBinding
    lateinit var selectedPoliceStationListener: SelectedPoliceStationListner


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNearbyPoliceStationBottomSheetBinding.inflate(layoutInflater,container,false)

        binding.policeStationsRV.layoutManager=LinearLayoutManager(requireContext())
        binding.policeStationsRV.adapter = StationsListAdapter(requireContext(), stationsList)


        binding.backBtnImg.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    companion object{
        var stationsList : List<PoliceModel>?=null
        var cntxt :Context?=null
        fun getInstance(ctxt : Context, stations : List<PoliceModel>?) : NearbyPoliceStationBottomSheet{
            stationsList=stations
            cntxt=ctxt
            return NearbyPoliceStationBottomSheet()
        }
    }


    fun setListener(listener: SelectedPoliceStationListner) {
        this.selectedPoliceStationListener = listener
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        selectedPoliceStationListener.dismissBottomSheetCallback(true)
    }

 }