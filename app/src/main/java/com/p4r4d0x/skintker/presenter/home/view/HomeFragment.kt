package com.p4r4d0x.skintker.presenter.home.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.p4r4d0x.domain.utils.Constants
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.presenter.home.view.compose.TabScreen
import com.p4r4d0x.skintker.presenter.home.viewmodel.HomeViewModel
import com.p4r4d0x.skintker.presenter.main.FragmentScreen
import com.p4r4d0x.skintker.presenter.main.navigate
import com.p4r4d0x.skintker.theme.SkintkerTheme
import org.koin.android.ext.android.inject
import java.util.*

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by inject()

    private fun getPreferences() =
        activity?.getSharedPreferences(Constants.SKITNKER_PREFERENCES, Context.MODE_PRIVATE)

    private lateinit var userId: String
    override fun onResume() {
        super.onResume()
        userId = getPreferences()?.getString(Constants.PREFERENCES_USER_ID, "") ?: ""
        viewModel.getLogs(userId)
        viewModel.getUserStats(userId)
        context?.let { ctx ->
            with(NotificationManagerCompat.from(ctx)) {
                cancel(Constants.NOTIFICATION_ID)
            }
        }
        observeViewModel()
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
                    val deleteCallback = { logDate: Date ->
                        viewModel.removeLog(userId, logDate)
                    }
                    val navigateCallback = { screenDestination: FragmentScreen ->
                        navigate(screenDestination, FragmentScreen.Home)
                    }
                    TabScreen(viewModel, navigateCallback, deleteCallback)
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.logDeleted.observe(viewLifecycleOwner) { logDeleted ->
            if (logDeleted) {
                Toast.makeText(
                    activity, getString(
                        if (logDeleted) {
                            R.string.log_removed
                        } else {
                            R.string.log_not_removed
                        }
                    ), Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}