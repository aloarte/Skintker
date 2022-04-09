package com.p4r4d0x.skintker.presenter.home.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.data.Constants
import com.p4r4d0x.skintker.presenter.home.view.compose.TabScreen
import com.p4r4d0x.skintker.presenter.home.viewmodel.HomeViewModel
import com.p4r4d0x.skintker.presenter.main.FragmentScreen
import com.p4r4d0x.skintker.presenter.main.navigate
import com.p4r4d0x.skintker.theme.SkintkerTheme
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by inject()

    override fun onResume() {
        super.onResume()
        val prefs: SharedPreferences? =
            activity?.getSharedPreferences(Constants.SKITNKER_PREFERENCES, Context.MODE_PRIVATE)
        viewModel.getLogs(prefs?.getString(Constants.PREFERENCES_USER_ID, "") ?: "")
        viewModel.getLogsByIntensityLevel(prefs)
        context?.let { ctx ->
            with(NotificationManagerCompat.from(ctx)) {
                cancel(Constants.NOTIFICATION_ID)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            id = R.id.survey_fragment
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setContent {
                SkintkerTheme {
                    TabScreen(viewModel) { fragmentScreenDestination ->
                        navigate(fragmentScreenDestination, FragmentScreen.Home)
                    }
                }
            }
        }
    }

}