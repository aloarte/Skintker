package com.p4r4d0x.skintker.presenter.welcome.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.p4r4d0x.data.parsers.DataParser.getCurrentFormattedDate
import com.p4r4d0x.domain.Constants
import com.p4r4d0x.domain.usecases.GetLogUseCase
import com.p4r4d0x.skintker.presenter.main.FragmentScreen
import com.p4r4d0x.skintker.presenter.welcome.Event

class WelcomeViewModel(private val getLogUseCase: GetLogUseCase) : ViewModel() {

    private val _logReported = MutableLiveData<Boolean>()
    val logReported: LiveData<Boolean> = _logReported

    private val _navigateTo = MutableLiveData<Event<FragmentScreen>>()
    val navigateTo: LiveData<Event<FragmentScreen>> = _navigateTo

    private val _userAuthenticated = MutableLiveData<Boolean>()
    val userAuthenticated: LiveData<Boolean> = _userAuthenticated

    fun checkLogReportedToday() {
        getLogUseCase.invoke(params = GetLogUseCase.Params(date = getCurrentFormattedDate())) { log ->
            _logReported.value = log != null
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

    fun handleContinueHome(logAlreadyReported: Boolean) {
        if (logAlreadyReported) {
            _navigateTo.value = Event(FragmentScreen.Home)
        } else {
            _navigateTo.value = Event(FragmentScreen.Survey)
        }
    }

    fun handleContinueLogin(userAuthenticated: Boolean) {
        if (userAuthenticated) {
            checkLogReportedToday()
        } else {
            _navigateTo.value = Event(FragmentScreen.Login)
        }
    }
}