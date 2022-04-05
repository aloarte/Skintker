package com.p4r4d0x.skintker.data.datasources

import com.p4r4d0x.skintker.domain.log.Survey

interface SurveyDataSource {

    fun getSurvey(): Survey
}