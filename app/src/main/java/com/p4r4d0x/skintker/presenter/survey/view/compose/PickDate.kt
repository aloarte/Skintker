package com.p4r4d0x.skintker.presenter.survey.view.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.domain.log.SurveyActionType
import com.p4r4d0x.skintker.presenter.common.compose.Description

@Composable
fun PickDateScreen(
    onAction: (SurveyActionType) -> Unit,
    onBackPressed: () -> Unit,
    datePicked: () -> Unit
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxWidth()
    ) {
        Scaffold(
            topBar = {
                PickDateTopAppBar(
                    onBackPressed = onBackPressed
                )
            },
            content = {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    CalendarAnimation()
                    Description(descriptionResourceId = R.string.pick_date_description)
                    Spacer(Modifier.height(10.dp))
                    DateTodayButton(datePicked)
                    PickDateButton(onAction)

                }
            }
        )
    }
}

@Composable
private fun PickDateTopAppBar(
    onBackPressed: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.pick_date_title),
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .align(Alignment.Center)
            )

            IconButton(
                onClick = onBackPressed,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 20.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "",
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

        }
    }
}

@Composable
private fun PickDateButton(
    onAction: (SurveyActionType) -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onAction(SurveyActionType.PICK_DATE) },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary
        ),
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .padding(vertical = 10.dp)
            .height(54.dp),
        elevation = ButtonDefaults.elevation(0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f))

    ) {
        Text(
            text = stringResource(id = R.string.pick_date),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.8f)
        )
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f)
        )
    }
}


@Composable
private fun DateTodayButton(
    datePicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { datePicked() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary
        ),
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .padding(vertical = 10.dp)
            .height(54.dp),
        elevation = ButtonDefaults.elevation(0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f))

    ) {
        Text(
            text = stringResource(id = R.string.pick_date_update),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun CalendarAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.calendar))
    Surface(
        Modifier
            .fillMaxWidth()
            .height(150.dp)
            .width(150.dp)
    ) {
        LottieAnimation(composition, restartOnPlay = true, iterations = 20)

    }

}