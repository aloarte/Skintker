package com.p4r4d0x.data.dto.user

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(val userId: String)
