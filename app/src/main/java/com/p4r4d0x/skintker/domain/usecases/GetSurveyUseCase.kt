package com.p4r4d0x.skintker.domain.usecases

import com.p4r4d0x.skintker.data.repository.SurveyRepository
import com.p4r4d0x.skintker.domain.log.Survey

class GetSurveyUseCase(
    private val surveyRepository: SurveyRepository
) :
    BaseUseCaseResult<Survey>() {

    override suspend fun run(): Survey {
        return surveyRepository.getSurvey()
    }
}
