package com.p4r4d0x.skintker.data.enums

import android.content.res.Resources
import com.p4r4d0x.skintker.R

enum class AlcoholLevel(val value: Int) {
    None(0), Few(1), FewWine(2), Some(3);

    companion object {
        fun fromStringResource(alcoholStr: String, resources: Resources): AlcoholLevel {
            val alcoholNone = resources.getString(R.string.question_4_answer_1)
            val alcoholFewBeer = resources.getString(R.string.question_4_answer_2)
            val alcoholFewWine = resources.getString(R.string.question_4_answer_3)
            val alcoholSome = resources.getString(R.string.question_4_answer_4)
            return when (alcoholStr) {
                alcoholNone -> None
                alcoholFewBeer -> Few
                alcoholFewWine -> FewWine
                alcoholSome -> Some
                else -> None
            }
        }
    }

}