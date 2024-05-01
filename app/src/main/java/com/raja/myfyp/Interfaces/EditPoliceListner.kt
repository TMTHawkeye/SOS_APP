package com.raja.myfyp.Interfaces

import com.raja.myfyp.ModelClasses.PoliceDataClass

interface EditPoliceListner {
    fun deletePoliceItem(policeId: String?, position: Int)
    fun editPolice(policeId: PoliceDataClass?, position: Int)
}