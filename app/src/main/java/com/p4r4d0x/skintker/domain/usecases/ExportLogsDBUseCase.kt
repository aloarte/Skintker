package com.p4r4d0x.skintker.domain.usecases

import android.content.res.Resources
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.p4r4d0x.skintker.data.Constants.EXPORT_FILE_NAME
import com.p4r4d0x.skintker.data.repository.LogsManagementRepository
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.generateFile
import com.p4r4d0x.skintker.domain.parsers.CSVParser.getCSVRowFromData
import com.p4r4d0x.skintker.domain.parsers.CSVParser.getFoodReferenceMap
import com.p4r4d0x.skintker.domain.parsers.CSVParser.getHeaderCSVRow
import com.p4r4d0x.skintker.domain.parsers.CSVParser.getZonesReferenceMap

class ExportLogsDBUseCase(private val repository: LogsManagementRepository) :
    BaseUseCaseParamsResult<ExportLogsDBUseCase.Params, Boolean>() {

    data class Params(val resources: Resources)

    override suspend fun run(params: Params): Boolean {
        val logList = repository.getAllLogs()
        return exportDatabaseToCSVFile(params.resources, logList)
    }

    private fun exportDatabaseToCSVFile(
        resources: Resources,
        logList: List<DailyLogBO>
    ): Boolean {
        val csvFile = generateFile(EXPORT_FILE_NAME)
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
                        getCSVRowFromData(
                            index = index,
                            log = log,
                            referenceZonesList = referenceZonesList,
                            referenceFoodList = referenceFoodList
                        )
                    )
                }
                close()
            }
            true
        } else {
            false
        }
    }
}