package com.chongieball.salttest.di

import com.chongieball.salttest.ui.screen.home.HomeViewModel
import com.chongieball.salttest.ui.screen.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get()) }

}