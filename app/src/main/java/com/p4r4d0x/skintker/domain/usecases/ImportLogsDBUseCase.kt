package com.p4r4d0x.skintker.domain.usecases

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.p4r4d0x.skintker.data.repository.LogsManagementRepository
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.openFile
import com.p4r4d0x.skintker.domain.parsers.CSVParser.getDataFromCSVRow
import com.p4r4d0x.skintker.domain.parsers.CSVParser.getFoodReferenceMap
import com.p4r4d0x.skintker.domain.parsers.CSVParser.getZonesReferenceMap
import java.io.File

class ImportLogsDBUseCase(private val repository: LogsManagementRepository) :
    BaseUseCaseParamsResult<ImportLogsDBUseCase.Params, Boolean>() {

    data class Params(val context: Context, val resources: Resources, val uri: Uri)

    override suspend fun run(params: Params): Boolean {
        val csvFile = openFile(params.uri)

        return csvFile?.let {
            val logList = importDatabaseToCSVFile(csvFile, params.resources)
            repository.addAllLogs(logList)
            true
        } ?: run {
            false
        }
    }

    private fun importDatabaseToCSVFile(
        csvFile: File,
        resources: Resources
    ): List<DailyLogBO> {
        val importedList = mutableListOf<DailyLogBO>()
        val referenceZonesList = getZonesReferenceMap(resources)
        val referenceFoodList = getFoodReferenceMap(resources)

        csvReader().open(csvFile) {
            // Header
            readNext()
            do {
                val readRow = readNext()
                readRow?.let {
                    importedList.add(
                        getDataFromCSVRow(
                            readRow,
                            referenceZonesList,
                            referenceFoodList,
                            resources
                        )
                    )
                }
            } while (!readRow.isNullOrEmpty())
        }
        return importedList
    }
}