package com.p4r4d0x.skintker.presenter.settings.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.data.Constants.SKITNKER_PREFERENCES
import com.p4r4d0x.skintker.data.enums.SettingsStatus
import com.p4r4d0x.skintker.presenter.main.FragmentScreen
import com.p4r4d0x.skintker.presenter.main.navigate
import com.p4r4d0x.skintker.presenter.settings.view.compose.SettingScreen
import com.p4r4d0x.skintker.presenter.settings.viewmodel.SettingsViewModel
import com.p4r4d0x.skintker.theme.SkintkerTheme
import org.koin.android.ext.android.inject


class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by inject()

    private lateinit var mGoogleSignInClient: GoogleSignInClient


    private fun observeViewModel() {
        with(viewModel) {
            exportStatus.observe(viewLifecycleOwner) {
                launchToast(
                    if (it) {
                        SettingsStatus.LogsExported
                    } else {
                        SettingsStatus.ErrorExportingLogs
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        observeViewModel()
        viewModel.getLoggedUserInfo(GoogleSignIn.getLastSignedInAccount(requireActivity()))
    }

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
            val prefs: SharedPreferences? =
                activity?.getSharedPreferences(SKITNKER_PREFERENCES, Context.MODE_PRIVATE)
            setContent {
                SkintkerTheme {
                    SettingScreen(
                        settingsViewModel = viewModel,
                        prefs = prefs,
                        onBackIconPressed = {
                            activity?.onBackPressed()
                        },
                        onExportPressed = {
                            viewModel.launchExportUseCase(resources, requireContext())
                        },
                        onLogoutPressed = {
                            // Google sign out
                            mGoogleSignInClient.signOut().addOnCompleteListener {
                                navigate(FragmentScreen.Welcome, FragmentScreen.Settings)
                            }
                        },
                        settingsCallback = {
                            launchToast(it)
                        })
                }
            }
        }
    }

    private fun launchToast(status: SettingsStatus) {
        Toast.makeText(
            activity,
            when (status) {
                SettingsStatus.ErrorLoadPreferences -> resources.getString(R.string.settings_toast_preferences_load_error)
                SettingsStatus.ErrorSavePreferences -> resources.getString(R.string.settings_toast_preferences_save_error)
                SettingsStatus.PreferencesSaved -> resources.getString(R.string.settings_toast_preferences_saved)
                SettingsStatus.ErrorExportingLogs -> resources.getString(R.string.settings_toast_preferences_export_error)
                SettingsStatus.LogsExported -> resources.getString(R.string.settings_toast_preferences_logs_exported)
            },
            Toast.LENGTH_SHORT
        ).show()

    }
}