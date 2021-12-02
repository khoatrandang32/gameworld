package com.kflower.gameworld.ui.splash

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SplashViewModel : BaseObservable() {

    val username = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")

}