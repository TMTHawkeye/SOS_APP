package com.raja.myfyp.ModelClasses

import java.io.Serializable

data class PoliceDataClass(
    var id:String?="",
    var name: String?="",
    var password: String?="",
    var address: String?="",
    var email: String?="",
    var rank: String?="",
    var phone: String?="",
) : Serializable