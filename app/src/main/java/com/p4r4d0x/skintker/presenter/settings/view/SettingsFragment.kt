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
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.p4r4d0x.domain.utils.Constants
import com.p4r4d0x.domain.utils.Constants.SKITNKER_PREFERENCES
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.getUserId
import com.p4r4d0x.skintker.presenter.common.utils.AlarmUtils.addAlarmPreferences
import com.p4r4d0x.skintker.presenter.common.utils.AlarmUtils.cancelAlarm
import com.p4r4d0x.skintker.presenter.common.utils.AlarmUtils.setAlarm
import com.p4r4d0x.skintker.presenter.main.FragmentScreen
import com.p4r4d0x.skintker.presenter.main.navigate
import com.p4r4d0x.skintker.presenter.settings.SettingsStatus
import com.p4r4d0x.skintker.presenter.settings.view.compose.SettingScreen
import com.p4r4d0x.skintker.presenter.settings.viewmodel.SettingsViewModel
import com.p4r4d0x.skintker.theme.SkintkerTheme
import org.koin.android.ext.android.inject


class SettingsFragment : Fragment() {

    private val userId: String = getUserId(activity)

    private val viewModel: SettingsViewModel by inject()

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var prefs: SharedPreferences

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
            updateReminderTime(prefs)
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
            activity?.let {
                prefs = it.getSharedPreferences(SKITNKER_PREFERENCES, Context.MODE_PRIVATE)
            }
            setContent {
                SkintkerTheme {
                    SettingScreen(
                        settingsViewModel = viewModel,
                        prefs = prefs,
                        onBackIconPressed = {
                            activity?.onBackPressed()
                        },
                        onExportPressed = {
                            viewModel.launchExportUseCase(resources, requireContext(), userId)
                        },
                        onLogoutPressed = {
                            // Google sign out
                            mGoogleSignInClient.signOut().addOnCompleteListener {
                                navigate(FragmentScreen.Welcome, FragmentScreen.Settings)
                            }
                        },
                        settingsCallback = {
                            launchToast(it)
                        },
                        onAlarmPressed = { addAlarm ->
                            if (addAlarm) {
                                showTimePicker()
                            } else {
                                activity?.let { fragmentActivity ->
                                    cancelAlarm(fragmentActivity, prefs)
                                    viewModel.updateReminderTime(prefs)
                                    launchToast(SettingsStatus.ReminderCleared)
                                }
                            }
                        },
                        onRemoveLogsPressed = {
                            viewModel.removeUserData(
                                prefs.getString(Constants.PREFERENCES_USER_ID, "") ?: ""
                            )
                        }
                    )
                }
            }
        }
    }

    private fun showTimePicker() {
        val materialTimePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build()
        materialTimePicker.addOnPositiveButtonClickListener {
            val reminderConfigured = prefs.getBoolean(Constants.PREFERENCES_ALARM_CREATED, false)
            addAlarmPreferences(materialTimePicker.hour, materialTimePicker.minute, prefs)
            activity?.let { fragmentActivity -> setAlarm(fragmentActivity) }
            viewModel.updateReminderTime(prefs)
            launchToast(
                if (reminderConfigured) {
                    SettingsStatus.ReminderUpdated
                } else {
                    SettingsStatus.ReminderCreated
                }
            )
        }
        activity?.let {
            materialTimePicker.show(it.supportFragmentManager, materialTimePicker.toString())
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
                SettingsStatus.ReminderCreated -> resources.getString(R.string.settings_toast_preferences_reminder_created)
                SettingsStatus.ReminderUpdated -> resources.getString(R.string.settings_toast_preferences_reminder_updated)
                SettingsStatus.ReminderCleared -> resources.getString(R.string.settings_toast_preferences_reminder_cleared)
                SettingsStatus.ReminderFailed -> resources.getString(R.string.settings_toast_preferences_reminder_fail)
            },
            Toast.LENGTH_SHORT
        ).show()
    }
}