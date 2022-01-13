package com.p4r4d0x.skintker.presenter.home.view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.p4r4d0x.skintker.presenter.home.view.compose.DailyLogCard
import com.p4r4d0x.skintker.presenter.home.view.compose.ResumeBody
import com.p4r4d0x.skintker.presenter.home.viewmodel.HomeViewModel
import com.p4r4d0x.skintker.theme.SkintkerTheme

@Composable
fun ResumeScreen(viewModel: HomeViewModel) {
    viewModel.possibleCauses.observeAsState().value?.let { causes ->
        SkintkerTheme { ResumeBody(causes = causes) }
    }
}

@Composable
fun HistoryScreen(viewModel: HomeViewModel) {
    viewModel.logList.observeAsState().value?.let { logs ->
        SkintkerTheme {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 14.dp)
            ) {
                item {
                    logs.forEach { log ->
                        DailyLogCard(log = log)
                        Spacer(modifier = Modifier.height(5.dp))

                    }
                }
            }
        }
    }
}
