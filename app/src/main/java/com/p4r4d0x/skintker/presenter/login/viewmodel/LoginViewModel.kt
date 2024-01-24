package com.p4r4d0x.skintker.presenter.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.p4r4d0x.domain.usecases.LoginUserUseCase
import com.p4r4d0x.skintker.presenter.login.LoginLoadingState
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUserUseCase: LoginUserUseCase) : ViewModel() {

    private val _loadingState = MutableLiveData<LoginLoadingState>()
    val loadingState: LiveData<LoginLoadingState> = _loadingState

    private val auth = FirebaseAuth.getInstance()

    fun signWithCredential(credential: AuthCredential) = viewModelScope.launch {
        try {
            _loadingState.value = LoginLoadingState.LOADING
            auth.signInWithCredential(credential).addOnCompleteListener {
                loginIntoSktVault()
            }
        } catch (e: Exception) {
            _loadingState.value = LoginLoadingState.error(e.localizedMessage)
        }
    }

    fun signAnonymous() = viewModelScope.launch {
        try {
            _loadingState.value = LoginLoadingState.LOADING
            auth.signInAnonymously().addOnCompleteListener{
                loginIntoSktVault()
            }
        } catch (e: Exception) {
            _loadingState.value = LoginLoadingState.error(e.localizedMessage)
        }
    }

    private fun loginIntoSktVault(){
        FirebaseAuth.getInstance().currentUser?.uid?.let{ userId->
            loginUserUseCase.invoke(viewModelScope,params = LoginUserUseCase.Params(userId)){
                if(it){
                    _loadingState.value = LoginLoadingState.LOADED
                }else{
                    FirebaseAuth.getInstance().signOut()
                }
            }
        }

    }

}
