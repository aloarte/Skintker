package com.p4r4d0x.data.datasources

import com.p4r4d0x.domain.bo.Survey

interface SurveyDataSource {

    fun getSurvey(): Survey
}