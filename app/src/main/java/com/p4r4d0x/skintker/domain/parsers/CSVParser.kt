package com.p4r4d0x.skintker.domain.parsers

import android.content.res.Resources
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.data.Constants.LABEL_ALCOHOL
import com.p4r4d0x.skintker.data.Constants.LABEL_BEERS
import com.p4r4d0x.skintker.data.Constants.LABEL_CITY
import com.p4r4d0x.skintker.data.Constants.LABEL_DATE
import com.p4r4d0x.skintker.data.Constants.LABEL_FOODS
import com.p4r4d0x.skintker.data.Constants.LABEL_ID
import com.p4r4d0x.skintker.data.Constants.LABEL_IRRITATED_ZONES
import com.p4r4d0x.skintker.data.Constants.LABEL_IRRITATION
import com.p4r4d0x.skintker.data.Constants.LABEL_STRESS
import com.p4r4d0x.skintker.data.Constants.LABEL_TRAVELED
import com.p4r4d0x.skintker.data.Constants.LABEL_WEATHER_HUMIDITY
import com.p4r4d0x.skintker.data.Constants.LABEL_WEATHER_TEMPERATURE
import com.p4r4d0x.skintker.data.enums.AlcoholLevel
import com.p4r4d0x.skintker.domain.bo.AdditionalDataBO
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.bo.IrritationBO
import com.p4r4d0x.skintker.domain.cleanString
import com.p4r4d0x.skintker.domain.getDDMMYYYYDate
import com.p4r4d0x.skintker.domain.parsers.DataParser.stringToDate

object CSVParser {

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

    private fun getReferenceMap(
        resources: Resources,
        referenceResourceMap: Map<Int, Int>
    ): Map<String, Int> {
        val transformedMap = mutableMapOf<String, Int>()
        referenceResourceMap.forEach {
            transformedMap[resources.getString(it.value).cleanString()] = it.key
        }
        return transformedMap
    }

    fun getZonesReferenceMap(resources: Resources) =
        getReferenceMap(resources, zonesReferenceMap)

    fun getFoodReferenceMap(resources: Resources) =
        getReferenceMap(resources, foodReferenceMap)

    fun getBeerTypesReferenceMap(resources: Resources) =
        getReferenceMap(resources, beerTypesReferenceMap)

    /**
     * Build a header CSV row for the export process
     */
    fun getHeaderCSVRow(
        referenceZonesList: Map<String, Int>,
        referenceFoodList: Map<String, Int>,
        referenceBeerTypesList: Map<String, Int>

    ): List<String> {
        val zonesHeaderList = referenceZonesList.toList().map { it.first }
        val foodHeaderList = referenceFoodList.toList().map { it.first }
        val beerTypesHeaderList = referenceBeerTypesList.toList().map { it.first }

        val headerList = mutableListOf(
            LABEL_ID,
            LABEL_DATE,
            LABEL_FOODS,
            LABEL_IRRITATION,
            LABEL_IRRITATED_ZONES,
            LABEL_ALCOHOL,
            LABEL_BEERS,
            LABEL_STRESS,
            LABEL_WEATHER_HUMIDITY,
            LABEL_WEATHER_TEMPERATURE,
            LABEL_CITY,
            LABEL_TRAVELED
        )
        headerList.addAll(zonesHeaderList)
        headerList.addAll(foodHeaderList)
        headerList.addAll(beerTypesHeaderList)

        return headerList
    }

    /**
     * Build a data CSV row for the export process with the data from a DailyLogBO
     */
    fun getCSVRowFromData(
        index: Int,
        log: DailyLogBO,
        referenceZonesList: Map<String, Int>,
        referenceFoodList: Map<String, Int>,
        referenceBeerTypesList: Map<String, Int>
    ): List<Any?> {

        val dataList = mutableListOf(
            index.toString(),
            log.date.getDDMMYYYYDate(),
            log.foodList.joinToString(separator = ",") { food ->
                food.cleanString()
            },
            log.irritation?.overallValue.toString(),
            log.irritation?.zoneValues?.joinToString(separator = ",") { zone ->
                zone.cleanString()
            },
            log.additionalData?.alcoholLevel?.name,
            log.additionalData?.beerTypes?.joinToString(separator = ",") { beerType ->
                beerType.cleanString()
            },
            log.additionalData?.stressLevel.toString(),
            log.additionalData?.weather?.humidity.toString(),
            log.additionalData?.weather?.temperature.toString(),
            log.additionalData?.travel?.city,
            log.additionalData?.travel?.traveled.toString()

        )

        dataList.addAll(getDataList(log.irritation?.zoneValues, referenceZonesList))
        dataList.addAll(getDataList(log.foodList, referenceFoodList))
        log.additionalData?.beerTypes?.let {
            dataList.addAll(getDataList(it, referenceBeerTypesList))

        }

        return dataList
    }

    /**
     * Get a data list (food/zones) export process cleaning the strings from emojis/accents
     */
    private fun getDataList(
        dataValues: List<String>?,
        referenceMap: Map<String, Int>
    ): List<String> {
        val rowMap = mutableMapOf<Int, String>()
        var indexCnt = 0
        referenceMap.forEach { _ ->
            rowMap[indexCnt] = ""
            indexCnt++
        }

        dataValues?.forEach { dataValue ->
            val cleanedData = dataValue.cleanString()
            val position = referenceMap[cleanedData]
            position?.let {
                rowMap[it] = cleanedData
            }
        }
        return rowMap.toList().map { it.second }
    }

    /**
     * Build a DailyLogBO list from the data CSV row for the import process
     */
    fun getDataFromCSVRow(
        csvRow: List<String>,
        referenceZonesList: Map<String, Int>,
        referenceFoodList: Map<String, Int>,
        referenceBeerTypeList: Map<String, Int>,
        resources: Resources
    ): DailyLogBO {

        return DailyLogBO(
            date = stringToDate(csvRow[1]),
            foodList = getListFromCSV(
                csvRow[2],
                referenceFoodList,
                foodReferenceMap,
                resources
            ),
            irritation = IrritationBO(
                csvRow[3].toInt(),
                getListFromCSV(
                    csvRow[4],
                    referenceZonesList,
                    zonesReferenceMap,
                    resources
                )
            ),
            additionalData = AdditionalDataBO(
                alcoholLevel = AlcoholLevel.valueOf(csvRow[5]),
                beerTypes = getListFromCSV(
                    csvRow[6],
                    referenceBeerTypeList,
                    beerTypesReferenceMap,
                    resources
                ),
                stressLevel = csvRow[7].toInt(),
                weather = AdditionalDataBO.WeatherBO(
                    humidity = csvRow[8].toInt(),
                    temperature = csvRow[9].toInt()
                ),
                travel = AdditionalDataBO.TravelBO(
                    city = csvRow[9],
                    traveled = csvRow[10].toBoolean()
                )
            )
        )
    }

    /**
     * Get the list from the CSV adding the emojis/accents removed previously
     */
    private fun getListFromCSV(
        row: String,
        cleanedReferenceMap: Map<String, Int>,
        referenceMap: Map<Int, Int>,
        resources: Resources
    ): List<String> {
        val list = listOf(*row.split(",").toTypedArray())
        val transformedList = mutableListOf<String>()
        list.map { item ->
            transformedList.add(
                cleanedReferenceMap.toList()
                    .find { it.first.contains(item) }?.second?.let { referenceKey ->
                        referenceMap[referenceKey]?.let { referenceValue ->
                            resources.getString(referenceValue)
                        } ?: run {
                            item
                        }
                    } ?: run {
                    item
                })
        }
        return transformedList
    }
}