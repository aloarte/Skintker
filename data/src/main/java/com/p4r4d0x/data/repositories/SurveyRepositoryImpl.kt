package com.p4r4d0x.data.repositories

import com.p4r4d0x.domain.repository.SurveyRepository
import com.p4r4d0x.data.datasources.SurveyDataSource
import com.p4r4d0x.domain.bo.Survey

class SurveyRepositoryImpl(private val dataSource: SurveyDataSource) : SurveyRepository {
    override fun getSurvey(): Survey {
        return dataSource.getSurvey()
    }
}