package com.p4r4d0x.domain.usecases

import android.content.Context
import android.content.res.Resources
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.p4r4d0x.data.generateFile
import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.repository.LogsManagementRepository
import com.p4r4d0x.domain.repository.ResourcesRepository
import com.p4r4d0x.domain.utils.CSVParser.getCSVRowFromData
import com.p4r4d0x.domain.utils.CSVParser.getHeaderCSVRow
import com.p4r4d0x.domain.utils.CSVParser.getReferenceMap
import com.p4r4d0x.domain.utils.Constants.EXPORT_FILE_NAME
import java.io.FileNotFoundException

class ExportLogsDBUseCase(private val logsRepository: LogsManagementRepository, private val resourcesRepository: ResourcesRepository) :
    BaseUseCaseParamsResult<ExportLogsDBUseCase.Params, Boolean>() {

    data class Params(
        val resources: Resources, val context: Context
    )

    override suspend fun run(params: Params): Boolean {
        val logList = logsRepository.getAllLogs()
        return try {
            exportDatabaseToCSVFile(params.context, params.resources, logList)
        } catch (foe: FileNotFoundException) {
            false
        }
    }

    private fun exportDatabaseToCSVFile(
        context: Context,
        resources: Resources,
        logList: List<DailyLogBO>
    ): Boolean {
        val csvFile = generateFile(context, EXPORT_FILE_NAME)
        val referenceZonesList = getReferenceMap(resources, resourcesRepository.getZonesReferenceMap())
        val referenceFoodList = getReferenceMap(resources, resourcesRepository.getFoodReferenceMap())
        val referenceBeerTypesList = getReferenceMap(resources, resourcesRepository.getBeerTypesReferenceMap())
        return if (csvFile != null) {
            csvWriter().open(csvFile, append = false) {
                // Header
                writeRow(
                    getHeaderCSVRow(referenceZonesList, referenceFoodList, referenceBeerTypesList)
                )
                logList.forEachIndexed { index, log ->
                    writeRow(
                        getCSVRowFromData(
                            index = index,
                            log = log,
                            referenceZonesList = referenceZonesList,
                            referenceFoodList = referenceFoodList,
                            referenceBeerTypesList = referenceBeerTypesList
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