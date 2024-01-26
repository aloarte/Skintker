package com.p4r4d0x.data.datasources

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.data.api.SkintkvaultApi
import com.p4r4d0x.data.datasources.impl.StatsDataSourceImpl
import com.p4r4d0x.data.dto.*
import com.p4r4d0x.data.dto.stats.StatsResponse
import com.p4r4d0x.data.room.*
import com.p4r4d0x.data.testutils.TestData.USER_ID
import com.p4r4d0x.data.testutils.TestData.stats
import com.p4r4d0x.data.testutils.Utils.buildResponse
import com.p4r4d0x.data.testutils.apiModule
import com.p4r4d0x.data.testutils.testDatasourcesModule
import com.p4r4d0x.data.testutils.testRepositoriesModule
import com.p4r4d0x.domain.bo.*
import com.p4r4d0x.test.KoinBaseTest
import com.p4r4d0x.test.KoinTestApplication
import com.squareup.moshi.Moshi
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.koin.core.component.inject
import org.robolectric.annotation.Config
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class StatsDataSourceTest :
    KoinBaseTest(testRepositoriesModule, testDatasourcesModule, apiModule) {

    companion object {
        const val JSON_USER_STATS = "{\n" +
                "    \"statusCode\": 0,\n" +
                "    \"content\": {\n" +
                "        \"type\": \"com.skintker.domain.model.responses.StatsResponse\",\n" +
                "        \"stats\": {\n" +
                    "  \"dietaryCauses\": [\n" +
                            "    \"Meat\",\n" +
                            "    \"Blue fish\"\n" +
                            "  ],\n" +
                            "  \"mostAffectedZones\": [\n" +
                            "    \"IrritationZone\"\n" +
                            "  ],\n" +
                            "  \"alcohol\": {\n" +
                            "    \"isPossible\": true\n" +
                            "  },\n" +
                            "  \"stress\": true,\n" +
                            "  \"travel\": {\n" +
                            "    \"isPossible\": true,\n" +
                            "    \"city\": \"Madrid\"\n" +
                            "  },\n" +
                            "  \"weather\": {\n" +
                            "    \"temperature\": {\n" +
                            "      \"level\": 0\n" +
                            "    },\n" +
                            "    \"humidity\": {\n" +
                            "      \"level\": 5\n" +
                            "    }\n" +
                            "  }\n" +
                            "}"+
                "    }\n" +
                "}"
    }

    private val api: SkintkvaultApi by inject()

    private val mediaType: MediaType by inject()

    private lateinit var datasource: StatsDatasource

    private var moshi: Moshi = Moshi.Builder().build()

    @Before
    fun setUp() {
        datasource = StatsDataSourceImpl(api, moshi)
    }

    @Test
    fun `test get user stats success`() {
        coEvery { api.getStats(USER_ID) } returns
                mediaType.buildResponse(resultCode = 200, json = JSON_USER_STATS)

        val userStats = runBlocking { datasource.getUserStats(USER_ID) }

        coVerify { api.getStats(USER_ID) }
        Assertions.assertEquals(ApiResult.Success(stats), userStats)
    }

    @Test
    fun `test get report list error`() {
        coEvery { api.getStats(USER_ID) } returns mediaType.buildResponse(resultCode = 404)

        val userStats = runBlocking { datasource.getUserStats(USER_ID) }

        val expected =
            ApiResult.Error<StatsResponse>(errorCode = 404, errorMessage = "Response.error()")
        coVerify { api.getStats(USER_ID) }
        Assertions.assertEquals(expected, userStats)
    }
}