package com.p4r4d0x.domain.di

import com.p4r4d0x.domain.repository.ReportsManagementRepository
import com.p4r4d0x.domain.repository.ResourcesRepository
import com.p4r4d0x.domain.repository.SurveyRepository
import io.mockk.mockk
import org.koin.dsl.module

val testRepositoriesModule = module {
    factory { mockk<SurveyRepository>() }
    factory { mockk<ResourcesRepository>() }
    factory { mockk<ReportsManagementRepository>() }

}