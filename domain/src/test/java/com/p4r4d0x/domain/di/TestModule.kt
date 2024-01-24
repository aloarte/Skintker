package com.p4r4d0x.domain.di

import com.p4r4d0x.domain.repository.ReportsManagementRepository
import com.p4r4d0x.domain.repository.ResourcesRepository
import com.p4r4d0x.domain.repository.StatsRepository
import com.p4r4d0x.domain.repository.SurveyRepository
import com.p4r4d0x.domain.repository.UserRepository
import io.mockk.mockk
import org.koin.dsl.module

val testRepositoriesModule = module {
    factory { mockk<SurveyRepository>() }
    factory { mockk<ResourcesRepository>() }
    factory { mockk<ReportsManagementRepository>() }
    factory { mockk<StatsRepository>() }
    factory { mockk<UserRepository>() }

}