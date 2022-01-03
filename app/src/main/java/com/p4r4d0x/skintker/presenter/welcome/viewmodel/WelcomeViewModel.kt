package com.p4r4d0x.skintker.presenter.welcome.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.p4r4d0x.skintker.data.Event
import com.p4r4d0x.skintker.presenter.FragmentScreen

class WelcomeViewModel : ViewModel() {

    private val _navigateTo = MutableLiveData<Event<FragmentScreen>>()
    val navigateTo: LiveData<Event<FragmentScreen>> = _navigateTo

    fun handleContinue(loadSurvey: Boolean) {
        if (loadSurvey) {
            _navigateTo.value = Event(FragmentScreen.Survey)
        } else {
            _navigateTo.value = Event(FragmentScreen.Home)
        }
    }

}