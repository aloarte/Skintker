package com.p4r4d0x.data.datasources

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.p4r4d0x.data.api.SkintkvaultApi
import com.p4r4d0x.data.datasources.impl.UserDataSourceImpl
import com.p4r4d0x.data.dto.*
import com.p4r4d0x.data.dto.user.UserDto
import com.p4r4d0x.data.dto.user.UserResultEnum
import com.p4r4d0x.data.room.*
import com.p4r4d0x.data.testutils.TestData.USER_ID
import com.p4r4d0x.data.testutils.Utils.buildResponse
import com.p4r4d0x.data.testutils.apiModule
import com.p4r4d0x.data.testutils.testDatasourcesModule
import com.p4r4d0x.domain.bo.*
import com.p4r4d0x.test.KoinBaseTest
import com.p4r4d0x.test.KoinTestApplication
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
class UserDataSourceTest :    KoinBaseTest(testDatasourcesModule, apiModule) {

    companion object {
        private val USER_DTO  = UserDto(USER_ID)

        private val JSON_USER_LOGIN = "{\n" +
                "    \"result\": \"UserExist\"\n" +
                "}"
    }

    private val api: SkintkvaultApi by inject()

    private val mediaType: MediaType by inject()

    private lateinit var datasource: UserDataSource

    private var gson: Gson = Gson()

    @Before
    fun setUp() {
        datasource = UserDataSourceImpl(api, gson)
    }

    @Test
    fun `test login user success`() {
        coEvery { api.addUser(USER_DTO) } returns
                mediaType.buildResponse(resultCode = 200, json = JSON_USER_LOGIN)

        val loginResult = runBlocking { datasource.loginUser(USER_ID) }

        coVerify { api.addUser(USER_DTO) }
        Assertions.assertEquals(ApiResult.Success(UserResultEnum.UserExist), loginResult)
    }

    @Test
    fun `test login user error`() {
        coEvery { api.addUser(USER_DTO) } returns mediaType.buildResponse(resultCode = 404)

        val loginResult = runBlocking { datasource.loginUser(USER_ID) }

        val expected =
            ApiResult.Error<UserResultEnum>(errorCode = 404, errorMessage = "Response.error()")
        coVerify { api.addUser(USER_DTO) }
        Assertions.assertEquals(expected, loginResult)
    }
}