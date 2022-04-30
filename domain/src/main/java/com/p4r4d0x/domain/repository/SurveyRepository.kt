package com.p4r4d0x.domain.repository

import com.p4r4d0x.domain.bo.Survey

interface SurveyRepository {

    fun getSurvey(): Survey
}