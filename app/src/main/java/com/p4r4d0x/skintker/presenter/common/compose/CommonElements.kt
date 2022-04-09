package com.p4r4d0x.skintker.presenter.common.compose

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Description(descriptionResourceId: Int) {
    Text(
        stringResource(id = descriptionResourceId),
        fontSize = 10.sp,
        fontWeight = FontWeight.Light,
        style = MaterialTheme.typography.caption,
        modifier = Modifier
            .padding(vertical = 20.dp, horizontal = 30.dp)
    )
}

@Composable
fun SkintkerDivider() {
    Divider(
        modifier = Modifier
            .height(0.2.dp)
            .padding(horizontal = 80.dp),
        color = MaterialTheme.colors.primaryVariant
    )
}