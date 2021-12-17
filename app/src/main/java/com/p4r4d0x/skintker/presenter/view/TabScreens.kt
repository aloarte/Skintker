package com.p4r4d0x.skintker.presenter.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.p4r4d0x.skintker.domain.log.SurveyState
import com.p4r4d0x.skintker.presenter.view.compose.LogQuestionScreen
import com.p4r4d0x.skintker.presenter.view.compose.SurveyResultScreen
import com.p4r4d0x.skintker.presenter.viewmodel.MainViewModel
import com.p4r4d0x.skintker.theme.SkintkerTheme


@Composable
fun LogScreen(viewModel: MainViewModel) {
    SkintkerTheme {
        viewModel.uiState.observeAsState().value?.let { logState ->
            when (logState) {
                is SurveyState.LogQuestions -> {
                    LogQuestionScreen(
                        logQuestions = logState,
                        onDonePressed = { viewModel.computeResult(logState) },
                        onBackPressed = {
//                            activity?.onBackPressedDispatcher?.onBackPressed()
                        }
                    )
                }
                is SurveyState.Result -> {
                    SurveyResultScreen(
                        result = logState,
                        onDonePressed = {
//                        activity?.onBackPressedDispatcher?.onBackPressed()
                        }
                    )
                }
            }
        }
    }

}


@Composable
fun ResumeScreen() {
}

@Composable
fun HistoryScreen() {

}


