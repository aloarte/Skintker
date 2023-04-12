package com.p4r4d0x.skintker.presenter.survey.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.material.datepicker.MaterialDatePicker
import com.p4r4d0x.data.parsers.DataParser.stringToDateFromPicker
import com.p4r4d0x.domain.bo.SurveyActionType
import com.p4r4d0x.domain.utils.Constants
import com.p4r4d0x.domain.utils.Constants.YEAR_DAYS
import com.p4r4d0x.domain.utils.getDateWithoutTime
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.presenter.main.FragmentScreen
import com.p4r4d0x.skintker.presenter.main.MainActivity
import com.p4r4d0x.skintker.presenter.main.navigate
import com.p4r4d0x.skintker.presenter.survey.SurveyState
import com.p4r4d0x.skintker.presenter.survey.view.compose.LogQuestionScreen
import com.p4r4d0x.skintker.presenter.survey.view.compose.PickDateScreen
import com.p4r4d0x.skintker.presenter.survey.viewmodel.SurveyViewModel
import com.p4r4d0x.skintker.theme.SkintkerTheme
import org.koin.android.ext.android.inject
import java.time.LocalDate
import java.util.*

@ExperimentalPermissionsApi
class SurveyFragment : Fragment() {

    private val viewModel: SurveyViewModel by inject()

    private val args: SurveyFragmentArgs by navArgs()

    private var wasGpsActivationRequested = false

    override fun onResume() {
        super.onResume()
        if (wasGpsActivationRequested) {
            requestGPS()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val prefs: SharedPreferences? =
            activity?.getSharedPreferences(Constants.SKINTKER_PREFERENCES, Context.MODE_PRIVATE)

        observeViewModel()
        viewModel.loadDate(args.pickDate)

        return ComposeView(requireContext()).apply {
            id = R.id.survey_fragment

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setContent {
                SkintkerTheme {
                    viewModel.uiState.observeAsState().value?.let { logState ->
                        when (logState) {
                            is SurveyState.PickDate -> {
                                PickDateScreen(
                                    onAction = {
                                        handleActionAction(it)
                                    },
                                    onBackPressed = {
                                        viewModel.checkIfLogIsAlreadyInserted()
                                    },
                                    datePicked = {
                                        viewModel.loadQuestions()
                                    }
                                )
                            }
                            is SurveyState.Questions -> {
                                LogQuestionScreen(
                                    viewModel = viewModel,
                                    questions = logState,
                                    onDonePressed = {
                                        viewModel.computeResult(
                                            userId = prefs?.getString(
                                                Constants.PREFERENCES_USER_ID,
                                                ""
                                            ) ?: "",
                                            surveyQuestions = logState,
                                            resources = resources
                                        )
                                    },
                                    shouldAskPermissions = viewModel.askForPermissions,
                                    onDoNotAskForPermissions = { viewModel.shouldAskForPermissions() },
                                    onBackPressed = {
                                        viewModel.checkIfLogIsAlreadyInserted()
                                    },
                                    onAction = {
                                        handleActionAction(it)
                                    })
                            }
                            is SurveyState.Result -> {
                                navigate(FragmentScreen.Home, FragmentScreen.Survey)
                            }
                            is SurveyState.ResultError -> {
                                Toast.makeText(
                                    activity,
                                    getString(R.string.survey_log_create_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                                navigate(FragmentScreen.Home, FragmentScreen.Survey)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.logReported.observe(viewLifecycleOwner) { logAlreadyReported ->
            if (logAlreadyReported) {
                activity?.onBackPressedDispatcher?.onBackPressed()
            } else {
                navigate(FragmentScreen.Home, FragmentScreen.Survey)
            }
        }
    }

    private fun handleActionAction(actionType: SurveyActionType) {
        when (actionType) {
            SurveyActionType.PICK_DATE -> {
                showDatePicker()
            }
            SurveyActionType.GET_LOCATION -> {
                wasGpsActivationRequested = false
                requestGPS()
            }
            SurveyActionType.SETTINGS_GPS -> {
                wasGpsActivationRequested = true
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }
    }

    private fun requestGPS() {
        (requireActivity() as? MainActivity)?.let {
            if (it.isGpsEnabled()) {
                it.getLocation { city ->
                    viewModel.updateCityValue(city)
                }
                viewModel.setGpsStatus(active = true)
            } else {
                viewModel.setGpsStatus(active = false)
            }
        }
    }

    private fun showDatePicker() {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setSelection(Date().time)
            .build()
        activity?.let {
            picker.show(it.supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener { timePicked ->
                when {
                    Date(timePicked) > Date() -> {
                        Toast.makeText(
                            activity,
                            resources.getString(R.string.pick_date_error_future),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Date(timePicked) < stringToDateFromPicker(
                        LocalDate.now().minusDays(YEAR_DAYS).toString()
                    ) -> {
                        Toast.makeText(
                            activity,
                            resources.getString(R.string.pick_date_error_past),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        viewModel.loadQuestions(Date(timePicked).getDateWithoutTime())

                    }
                }
            }
        }
    }
}