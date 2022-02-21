package com.p4r4d0x.skintker.presenter.main

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.p4r4d0x.skintker.R
import java.security.InvalidParameterException

enum class FragmentScreen { Welcome, Survey, Home, Settings, Login }

fun Fragment.navigate(to: FragmentScreen, from: FragmentScreen) {
    val navController = findNavController()
    if (to == from) {
        throw InvalidParameterException("Can't navigate to $to")
    }
    when (from) {
        FragmentScreen.Login -> {
            when (to) {
                FragmentScreen.Welcome -> navController.navigate(R.id.action_loginFragment_to_welcomeFragment)
                else -> {}
            }
        }
        FragmentScreen.Welcome -> {
            when (to) {
                FragmentScreen.Login -> navController.navigate(R.id.action_welcomeFragment_to_loginFragment)
                FragmentScreen.Survey -> navController.navigate(R.id.action_welcomeFragment_to_surveyFragment)
                FragmentScreen.Home -> navController.navigate(R.id.action_welcomeFragment_to_homeFragment)
                else -> {}
            }
        }
        FragmentScreen.Survey -> {
            navController.navigate(R.id.action_surveyFragment_to_homeFragment)
        }
        FragmentScreen.Home -> {
            when (to) {
                FragmentScreen.Survey -> navController.navigate(R.id.action_homeFragment_to_surveyFragment)
                FragmentScreen.Settings -> navController.navigate(R.id.action_homeFragment_to_settingsFragment)
                else -> {}
            }
        }
        FragmentScreen.Settings -> {
            when (to) {
                FragmentScreen.Welcome -> navController.navigate(R.id.action_settingsFragment_to_welcomeFragment)
                else -> {}
            }
        }
        else -> {}
    }
}
