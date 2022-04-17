package com.p4r4d0x.domain.usecases

import com.p4r4d0x.domain.bo.Survey
import com.p4r4d0x.domain.repository.SurveyRepository

class GetSurveyUseCase(
    private val surveyRepository: SurveyRepository
) :
    BaseUseCaseResult<Survey>() {

    override suspend fun run(): Survey {
        return surveyRepository.getSurvey()
    }
}
