package com.raja.myfyp.Interfaces

import com.raja.myfyp.ModelClasses.PoliceModel

interface SelectedPoliceStationListner {

    fun selectedStation(policeStation : PoliceModel?)
    fun dismissBottomSheetCallback(isDismissed:Boolean)

}