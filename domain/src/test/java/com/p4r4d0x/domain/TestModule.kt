package com.p4r4d0x.domain

import com.p4r4d0x.domain.repository.LogsManagementRepository
import com.p4r4d0x.domain.repository.ResourcesRepository
import com.p4r4d0x.domain.repository.SurveyRepository
import io.mockk.mockk
import org.koin.dsl.module

val testRepositoriesModule = module {
    factory { mockk<SurveyRepository>() }
    factory { mockk<LogsManagementRepository>() }
    factory { mockk<ResourcesRepository>() }
}