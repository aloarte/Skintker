package com.p4r4d0x.skintker.data.enums

import android.content.res.Resources
import com.p4r4d0x.skintker.R

enum class AlcoholLevel(val value: Int) {
    None(0), Few(1), Some(2);

    companion object {
        fun fromStringResource(alcoholStr: String, resources: Resources): AlcoholLevel {
            val alcoholNone = resources.getString(R.string.question_4_answer_1)
            val alcoholFew = resources.getString(R.string.question_4_answer_2)
            val alcoholSome = resources.getString(R.string.question_4_answer_3)

            return when (alcoholStr) {
                alcoholNone -> None
                alcoholFew -> Few
                alcoholSome -> Some
                else -> None
            }

        }
    }

}