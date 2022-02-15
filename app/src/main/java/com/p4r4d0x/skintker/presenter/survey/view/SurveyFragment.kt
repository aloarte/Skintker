package com.p4r4d0x.skintker.presenter.survey.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.domain.log.SurveyState
import com.p4r4d0x.skintker.presenter.main.FragmentScreen
import com.p4r4d0x.skintker.presenter.main.MainActivity
import com.p4r4d0x.skintker.presenter.main.navigate
import com.p4r4d0x.skintker.presenter.survey.view.compose.LogQuestionScreen
import com.p4r4d0x.skintker.presenter.survey.viewmodel.SurveyViewModel
import com.p4r4d0x.skintker.theme.SkintkerTheme
import org.koin.android.ext.android.inject

class SurveyFragment : Fragment() {

    private val viewModel: SurveyViewModel by inject()

    @ExperimentalPermissionsApi
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
                    viewModel.uiState.observeAsState().value?.let { logState ->
                        when (logState) {
                            is SurveyState.LogQuestions -> {
                                LogQuestionScreen(
                                    viewModel = viewModel,
                                    logQuestions = logState,
                                    onDonePressed = {
                                        viewModel.computeResult(
                                            logState,
                                            resources = resources
                                        )
                                    },
                                    shouldAskPermissions = viewModel.askForPermissions,
                                    onDoNotAskForPermissions = { viewModel.shouldAskForPermissions() },
                                    onBackPressed = {
                                        viewModel.checkIfLogIsAlreadyInserted()
                                    },
                                    onAction = {
                                        (requireActivity() as? MainActivity)?.getLocation {
                                            viewModel.updateCityValue(it)
                                        }
                                    })
                            }
                            is SurveyState.Result -> {
                                navigate(FragmentScreen.Home, FragmentScreen.Survey)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        observeViewModel()
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
}