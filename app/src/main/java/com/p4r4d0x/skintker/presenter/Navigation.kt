package com.p4r4d0x.skintker.presenter

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.p4r4d0x.skintker.R
import java.security.InvalidParameterException

enum class FragmentScreen { Welcome, Survey, Home }

fun Fragment.navigate(to: FragmentScreen, from: FragmentScreen) {
    if (to == from) {
        throw InvalidParameterException("Can't navigate to $to")
    }
    when (to) {
        FragmentScreen.Welcome -> {
            findNavController().navigate(R.id.welcome_fragment)
        }
        FragmentScreen.Survey -> {
            findNavController().navigate(R.id.survey_fragment)
        }
        FragmentScreen.Home -> {
            findNavController().navigate(R.id.home_fragment)
        }
    }
}
