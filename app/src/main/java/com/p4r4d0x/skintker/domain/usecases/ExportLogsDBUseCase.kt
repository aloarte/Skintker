package com.p4r4d0x.skintker.domain.usecases

import android.content.Context
import android.content.res.Resources
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.p4r4d0x.skintker.data.Constants.EXPORT_FILE_NAME
import com.p4r4d0x.skintker.data.repository.LogManagementRepository
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.generateFile
import com.p4r4d0x.skintker.domain.parsers.DataParser.getDataCSVRow
import com.p4r4d0x.skintker.domain.parsers.DataParser.getFoodReferenceMap
import com.p4r4d0x.skintker.domain.parsers.DataParser.getHeaderCSVRow
import com.p4r4d0x.skintker.domain.parsers.DataParser.getZonesReferenceMap

class ExportLogsDBUseCase(private val repository: LogManagementRepository) :
    BaseUseCaseParamsResult<ExportLogsDBUseCase.Params, Boolean>() {

    data class Params(val context: Context, val resources: Resources)

    override suspend fun run(params: Params): Boolean {
        val logList = repository.getAllLogs()
        exportDatabaseToCSVFile(params.context, params.resources, logList)
        return false
    }

    private fun exportDatabaseToCSVFile(
        context: Context,
        resources: Resources,
        logList: List<DailyLogBO>
    ): Boolean {
        val csvFile = generateFile(context, EXPORT_FILE_NAME)
        val referenceZonesList = getZonesReferenceMap(resources)
        val referenceFoodList = getFoodReferenceMap(resources)
        return if (csvFile != null) {
            csvWriter().open(csvFile, append = false) {
                // Header
                writeRow(
                    getHeaderCSVRow(referenceZonesList, referenceFoodList)
                )
                logList.forEachIndexed { index, log ->
                    writeRow(
                        getDataCSVRow(
                            index = index,
                            log = log,
                            referenceZonesList = referenceZonesList,
                            referenceFoodList = referenceFoodList
                        )
                    )
                }
            }

            true
        } else {
            false
        }
    }
}