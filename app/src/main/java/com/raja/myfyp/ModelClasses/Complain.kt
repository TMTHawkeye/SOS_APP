package com.raja.myfyp.ModelClasses

import java.io.Serializable

data class Complain(
    var complainId : String?="",
    var userId : String?="",
    var userName : String?="",
    var userEmail : String?="",
    var userAddress : String?="",
    var date : String?="",
    var complainDesc : String?="",
    var status : String?="",
) : Serializable
