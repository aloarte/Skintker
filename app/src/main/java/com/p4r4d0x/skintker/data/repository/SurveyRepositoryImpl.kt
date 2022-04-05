/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.p4r4d0x.skintker.data.repository


import com.p4r4d0x.skintker.data.datasources.SurveyDataSource
import com.p4r4d0x.skintker.domain.log.Survey

class SurveyRepositoryImpl(private val dataSource: SurveyDataSource) : SurveyRepository {
    override fun getSurvey(): Survey {
        return dataSource.getSurvey()
    }
}