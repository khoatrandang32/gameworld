package com.kflower.gameworld.ui.login

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : BaseObservable() {

    val username = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")

}