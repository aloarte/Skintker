package com.p4r4d0x.data.datasources.impl

import com.example.data.R
import com.p4r4d0x.data.datasources.ResourcesDatasource

class ResourcesDatasourceImpl:ResourcesDatasource {

    private val zonesReferenceMap = mapOf(
        0 to R.string.question_2_answer_1,
        1 to R.string.question_2_answer_2,
        2 to R.string.question_2_answer_3,
        3 to R.string.question_2_answer_4,
        4 to R.string.question_2_answer_5,
        5 to R.string.question_2_answer_6,
        6 to R.string.question_2_answer_7,
        7 to R.string.question_2_answer_8,
        8 to R.string.question_2_answer_9,
        9 to R.string.question_2_answer_10,
        10 to R.string.question_2_answer_11,
        11 to R.string.question_2_answer_12
    )

    private val foodReferenceMap = mapOf(
        0 to R.string.question_8_answer_1,
        1 to R.string.question_8_answer_2,
        2 to R.string.question_8_answer_3,
        3 to R.string.question_8_answer_4,
        4 to R.string.question_8_answer_5,
        5 to R.string.question_8_answer_6,
        6 to R.string.question_8_answer_7,
        7 to R.string.question_8_answer_8,
        8 to R.string.question_8_answer_9,
        9 to R.string.question_8_answer_10,
        10 to R.string.question_8_answer_11,
        11 to R.string.question_8_answer_12,
        12 to R.string.question_8_answer_13,
        13 to R.string.question_8_answer_14,
        14 to R.string.question_8_answer_15,
        15 to R.string.question_8_answer_16,
        16 to R.string.question_8_answer_17,
        17 to R.string.question_8_answer_18,
        18 to R.string.question_9_answer_1,
        19 to R.string.question_9_answer_2,
        20 to R.string.question_9_answer_3,
        21 to R.string.question_9_answer_4,
        22 to R.string.question_9_answer_5,
        23 to R.string.question_9_answer_6,
        24 to R.string.question_9_answer_7,
        25 to R.string.question_9_answer_8,
        26 to R.string.question_9_answer_9,
        27 to R.string.question_9_answer_10,
        28 to R.string.question_9_answer_11,
        29 to R.string.question_9_answer_12,
        30 to R.string.question_9_answer_13
    )

    private val beerTypesReferenceMap = mapOf(
        0 to R.string.question_5_answer_1,
        1 to R.string.question_5_answer_2,
        2 to R.string.question_5_answer_3,
        3 to R.string.question_5_answer_4,
        4 to R.string.question_5_answer_5,
        5 to R.string.question_5_answer_6,
        6 to R.string.question_5_answer_7,
        7 to R.string.question_5_answer_8,
        8 to R.string.question_5_answer_9
    )

    override fun getZonesReferenceMap(): Map<Int, Int> {
       return zonesReferenceMap
    }

    override fun getFoodReferenceMap(): Map<Int, Int> {
        return foodReferenceMap
    }

    override fun getBeerTypesReferenceMap(): Map<Int, Int> {
        return beerTypesReferenceMap
    }
}