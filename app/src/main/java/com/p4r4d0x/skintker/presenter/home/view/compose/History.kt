package com.p4r4d0x.skintker.presenter.home.view.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.presenter.common.compose.DeleteDialog
import com.p4r4d0x.skintker.presenter.common.compose.DeleteDialogContent
import java.util.*

@Composable
fun HistoryContents(logs: List<DailyLogBO>, removeLog: (Date) -> Unit) {
    val showDeleteDialog: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    val logDate: MutableState<Date?> = remember {
        mutableStateOf(null)
    }
    if (showDeleteDialog.value) {
        DeleteDialog({ DeleteDialogContent(logDate.value) }) { confirm ->
            if (confirm) {
                logDate.value?.let {
                    removeLog(it)

                }
            }
            showDeleteDialog.value = false


        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 14.dp)
    ) {
        item {
            logs.forEach { log ->
                DailyLogCard(log = log) { date ->
                    showDeleteDialog.value = true
                    logDate.value = date
                }
                Spacer(modifier = Modifier.height(5.dp))

            }
        }
    }
}

@Composable
fun EmptyHistoryContents() {
    Column(Modifier.background(MaterialTheme.colors.background)) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center

        ) {

            Text(
                stringResource(
                    R.string.resume_no_causes_title
                ),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h3,
                modifier = Modifier
                    .padding(vertical = 15.dp, horizontal = 10.dp)
            )
        }
        Text(
            stringResource(id = R.string.history_no_logs),
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 30.dp)
        )
        TumbleweedRolling()
    }
}