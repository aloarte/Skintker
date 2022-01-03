package com.p4r4d0x.skintker.presenter.survey.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.domain.log.SurveyState
import com.p4r4d0x.skintker.presenter.FragmentScreen
import com.p4r4d0x.skintker.presenter.navigate
import com.p4r4d0x.skintker.presenter.survey.view.compose.LogQuestionScreen
import com.p4r4d0x.skintker.presenter.survey.view.compose.SurveyResultScreen
import com.p4r4d0x.skintker.presenter.survey.viewmodel.SurveyViewModel
import com.p4r4d0x.skintker.theme.SkintkerTheme
import org.koin.android.ext.android.inject

class SurveyFragment : Fragment() {

    private val viewModel: SurveyViewModel by inject()
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
                                    logQuestions = logState,
                                    onDonePressed = {
                                        viewModel.computeResult(
                                            logState,
                                            resources = resources
                                        )
                                    },
                                    onBackPressed = {
                                        activity?.onBackPressedDispatcher?.onBackPressed()
                                    }
                                )
                            }
                            is SurveyState.Result -> {
                                SurveyResultScreen(
                                    result = logState,
                                    onDonePressed = {
                                        navigate(FragmentScreen.Home, FragmentScreen.Welcome)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}