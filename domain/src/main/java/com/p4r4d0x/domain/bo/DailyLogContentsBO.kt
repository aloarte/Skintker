package com.p4r4d0x.domain.bo

data class DailyLogContentsBO(val count: Int = -1, val logList: List<DailyLogBO> = emptyList())
