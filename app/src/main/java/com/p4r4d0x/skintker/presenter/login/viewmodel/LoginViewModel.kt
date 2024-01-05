package com.p4r4d0x.skintker.presenter.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.p4r4d0x.skintker.presenter.login.LoginLoadingState
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loadingState = MutableLiveData<LoginLoadingState>()
    val loadingState: LiveData<LoginLoadingState> = _loadingState

    private val auth = FirebaseAuth.getInstance()

    fun signWithCredential(credential: AuthCredential) = viewModelScope.launch {
        try {
            _loadingState.value = LoginLoadingState.LOADING
            auth.signInWithCredential(credential).addOnCompleteListener {
                _loadingState.value = LoginLoadingState.LOADED
            }
        } catch (e: Exception) {
            _loadingState.value = LoginLoadingState.error(e.localizedMessage)
        }
    }

    fun signAnonymous() = viewModelScope.launch {
        try {
            _loadingState.value = LoginLoadingState.LOADING
            auth.signInAnonymously().addOnCompleteListener{
                _loadingState.value = LoginLoadingState.LOADED

            }
        } catch (e: Exception) {
            _loadingState.value = LoginLoadingState.error(e.localizedMessage)
        }
    }

}
