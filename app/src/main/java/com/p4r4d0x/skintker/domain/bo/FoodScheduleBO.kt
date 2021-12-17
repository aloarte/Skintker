package com.p4r4d0x.skintker.domain.bo

class FoodScheduleBO(
    val breakfast: List<FoodItemBO>? = null,
    val morningSnack: List<FoodItemBO>? = null,
    val lunch: List<FoodItemBO>? = null,
    val afternoonSnack: List<FoodItemBO>? = null,
    val dinner: List<FoodItemBO>? = null
)
