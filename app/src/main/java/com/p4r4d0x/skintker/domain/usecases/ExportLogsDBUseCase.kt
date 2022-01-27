package com.p4r4d0x.skintker.domain.usecases

import android.content.Context
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.p4r4d0x.skintker.data.Constants.CHARACTER_FILTER_REGEX
import com.p4r4d0x.skintker.data.repository.LogManagementRepository
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.cleanFoodString
import com.p4r4d0x.skintker.domain.getDDMMYYYYDate
import java.io.File

class ExportLogsDBUseCase(private val repository: LogManagementRepository) :
    BaseUseCaseParamsResult<ExportLogsDBUseCase.Params, Boolean>() {


    data class Params(val context: Context)

    override suspend fun run(params: Params): Boolean {
        val logList = repository.getAllLogs()
        exportDatabaseToCSVFile(params.context, logList)
        return false
    }

    private fun exportDatabaseToCSVFile(context: Context, logList: List<DailyLogBO>): Boolean {
        val csvFile = generateFile(context, "filename.csv")
        return if (csvFile != null) {
            csvWriter().open(csvFile, append = false) {
                // Header
                writeRow(
                    listOf(
                        "[id]",
                        "[date]",
                        "[foodList]",
                        "[irritation]",
                        "[irritationZones]",
                        "[alcohol]",
                        "[stress]",
                        "[humidity]",
                        "[temperature]",
                        "[city]",
                        "[traveled]"
                    )
                )
                logList.forEachIndexed { index, log ->

                    writeRow(
                        listOf(
                            index,
                            log.date.getDDMMYYYYDate(),
                            log.foodList.joinToString(separator = ",") { food ->
                                food.cleanFoodString()
                            },
                            log.irritation?.overallValue,
                            log.irritation?.zoneValues?.joinToString(separator = ",") { zone ->
                                zone.replace(
                                    CHARACTER_FILTER_REGEX.toRegex(),
                                    ""
                                )
                            },
                            log.additionalData?.alcoholLevel,
                            log.additionalData?.stressLevel,
                            log.additionalData?.weather?.humidity,
                            log.additionalData?.weather?.temperature,
                            log.additionalData?.travel?.city,
                            log.additionalData?.travel?.traveled

                        )
                    )
                }
            }

            true
        } else {
            false
        }
    }

    fun generateFile(context: Context, fileName: String): File? {
        val csvFile = File(context.filesDir, fileName)
        csvFile.createNewFile()

        return if (csvFile.exists()) {
            csvFile
        } else {
            null
        }
    }


}