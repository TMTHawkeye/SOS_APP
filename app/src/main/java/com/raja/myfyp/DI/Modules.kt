package com.raja.myfyp.DI

 import com.raja.myfyp.Repositories.PermissionsRepository
import com.raja.myfyp.Repositories.UserRepository
import com.raja.myfyp.ViewModels.PermissionsViewModel
import com.raja.myfyp.ViewModels.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule= module {
    single{ UserRepository(get()) }
    viewModel{UserViewModel(get())}

    single{PermissionsRepository(get())}
    viewModel { PermissionsViewModel(get()) }

}