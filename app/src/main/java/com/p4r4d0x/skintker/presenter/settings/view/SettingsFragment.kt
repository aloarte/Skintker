package com.p4r4d0x.skintker.presenter.settings.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.presenter.home.view.compose.SettingScreen
import com.p4r4d0x.skintker.theme.SkintkerTheme

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            id = R.id.settings_fragment
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            setContent {
                SkintkerTheme {
                    SettingScreen {
                        activity?.onBackPressed()
                    }
                }
            }
        }
    }

}