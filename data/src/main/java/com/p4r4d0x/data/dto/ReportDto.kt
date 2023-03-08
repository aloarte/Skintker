package com.p4r4d0x.data.dto

data class ReportDto(
    val date: String,
    val foodList: List<String>,
    val irritation: IrritationDto,
    val additionalData: AdditionalDataDto
)