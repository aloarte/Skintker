package com.p4r4d0x.skintker.presenter.welcome.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.p4r4d0x.skintker.data.Constants
import com.p4r4d0x.skintker.data.Event
import com.p4r4d0x.skintker.domain.parsers.DataParser.getCurrentFormattedDate
import com.p4r4d0x.skintker.domain.usecases.GetLogUseCase
import com.p4r4d0x.skintker.presenter.main.FragmentScreen

class WelcomeViewModel(private val getLogUseCase: GetLogUseCase) : ViewModel() {

    private val _navigateTo = MutableLiveData<Event<FragmentScreen>>()
    val navigateTo: LiveData<Event<FragmentScreen>> = _navigateTo

    private val _userAuthenticated = MutableLiveData<Boolean>()
    val userAuthenticated: LiveData<Boolean> = _userAuthenticated

    private val _logReported = MutableLiveData<Boolean>()
    val logReported: LiveData<Boolean> = _logReported

    fun handleContinueLogin(userAuthenticated: Boolean) {
        if (userAuthenticated) {
            checkLogReportedToday()
        } else {
            _navigateTo.value = Event(FragmentScreen.Login)
        }
    }

    fun handleContinueHome(logAlreadyReported: Boolean) {
        if (logAlreadyReported) {
            _navigateTo.value = Event(FragmentScreen.Home)
        } else {
            _navigateTo.value = Event(FragmentScreen.Survey)
        }
    }

    fun checkUserLogin(mGoogleSignInClient: GoogleSignInAccount?, prefs: SharedPreferences?) {
        if (mGoogleSignInClient != null) {
            val editor: SharedPreferences.Editor? = prefs?.edit()
            editor?.let {
                it.putString(Constants.PREFERENCES_USER_ID, mGoogleSignInClient.id)
                editor.apply()
            }
        }
        _userAuthenticated.value = mGoogleSignInClient != null
    }


    fun checkLogReportedToday() {
        getLogUseCase.invoke(params = GetLogUseCase.Params(date = getCurrentFormattedDate())) { log ->
            _logReported.value = log != null
        }
    }

}