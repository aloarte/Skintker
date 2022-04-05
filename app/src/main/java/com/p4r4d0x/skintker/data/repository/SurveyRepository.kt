package com.p4r4d0x.skintker.data.repository

import com.p4r4d0x.skintker.domain.log.Survey


interface SurveyRepository {

    fun getSurvey(): Survey
}