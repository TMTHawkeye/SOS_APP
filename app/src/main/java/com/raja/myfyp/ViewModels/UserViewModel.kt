package com.raja.myfyp.ViewModels

import androidx.lifecycle.ViewModel
import com.raja.myfyp.Repositories.UserRepository

class UserViewModel(val repository: UserRepository) : ViewModel() {

    fun getCountryCodes(callback:(List<String>)->Unit){
        repository.getCountryCodes(callback)
    }
}