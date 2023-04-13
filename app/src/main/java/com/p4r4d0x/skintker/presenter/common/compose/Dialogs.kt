package com.p4r4d0x.skintker.presenter.common.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.p4r4d0x.domain.utils.getDDMMYYYYDate
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.presenter.common.utils.DialogsData
import java.util.*


@Composable
fun CustomDialog(
    dialogsData: DialogsData,
    content: @Composable () -> Unit,
    onActionClicked: (Boolean) -> Unit
) {
    Dialog(
        onDismissRequest = { },
        content = {
            CompleteDialogContent(
                title = stringResource(dialogsData.titleRes),
                onActionClicked = onActionClicked,
                successButtonText = stringResource(dialogsData.okButtonRes),
                content = content
            )
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    )
}

@Composable
fun DeleteDialogAllContent(dialogsData: DialogsData) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(
            stringResource(id = dialogsData.descriptionRes),
            Modifier.width(220.dp)
        )
    }
}


@Composable
fun DeleteDialogSingleContent(date: Date?, dialogsData: DialogsData) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(
            stringResource(id = dialogsData.descriptionRes, date?.getDDMMYYYYDate() ?: ""),
            Modifier.width(220.dp)
        )
    }
}

@Composable
fun DeleteLogoutContent() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(
            stringResource(id = R.string.dialog_description_single),
            Modifier.width(220.dp)
        )
    }
}

@Composable
fun CompleteDialogContent(
    title: String,
    onActionClicked: (Boolean) -> Unit,
    successButtonText: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxHeight(0.6f)
            .fillMaxWidth(1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            DialogTitle(title)
            AddBody(content)
            BottomButtons(successButtonText, onActionClicked)
        }

    }
}

@Composable
private fun DialogTitle(title: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(100.dp)
            )
        }
        SkintkerDivider()
    }
}

@Composable
private fun BottomButtons(
    successButtonText: String,
    onActionClicked: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxWidth(1f)
            .padding(20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { onActionClicked(false) },
            modifier = Modifier
                .width(120.dp)
                .padding(end = 5.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(text = stringResource(id = R.string.dialog_ko).uppercase(), fontSize = 14.sp)
        }
        Button(
            onClick = {
                onActionClicked(true)
            },
            modifier = Modifier.width(120.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(text = successButtonText.uppercase(), fontSize = 14.sp)
        }

    }
}

@Composable
private fun AddBody(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .padding(20.dp)
    ) {
        content()
    }
}