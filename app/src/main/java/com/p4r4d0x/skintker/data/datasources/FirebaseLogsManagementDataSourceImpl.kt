package com.p4r4d0x.skintker.data.datasources

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.p4r4d0x.skintker.data.Constants.LABEL_ALCOHOL
import com.p4r4d0x.skintker.data.Constants.LABEL_BEERS
import com.p4r4d0x.skintker.data.Constants.LABEL_CITY
import com.p4r4d0x.skintker.data.Constants.LABEL_DATABASE_NAME
import com.p4r4d0x.skintker.data.Constants.LABEL_DATE
import com.p4r4d0x.skintker.data.Constants.LABEL_FOODS
import com.p4r4d0x.skintker.data.Constants.LABEL_IRRITATED_ZONES
import com.p4r4d0x.skintker.data.Constants.LABEL_IRRITATION
import com.p4r4d0x.skintker.data.Constants.LABEL_STRESS
import com.p4r4d0x.skintker.data.Constants.LABEL_TRAVELED
import com.p4r4d0x.skintker.data.Constants.LABEL_WEATHER_HUMIDITY
import com.p4r4d0x.skintker.data.Constants.LABEL_WEATHER_TEMPERATURE
import com.p4r4d0x.skintker.data.Constants.TAG_FIREBASE
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.parsers.DataParser.parseDocumentData
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseLogsManagementDataSourceImpl : FirebaseLogsManagementDataSource {

    private val firebaseDb = Firebase.firestore

    override suspend fun addSyncLog(userId: String, log: DailyLogBO): Boolean =
        suspendCoroutine { cont ->
            addLog(userId, log) { cont.resume(it) }
        }

    private fun addLog(userId: String, log: DailyLogBO, onLogsAdded: (Boolean) -> Unit) {
        val logMap = hashMapOf(
            LABEL_DATE to log.date,
            LABEL_IRRITATION to log.irritation?.overallValue,
            LABEL_IRRITATED_ZONES to log.irritation?.zoneValues?.joinToString(separator = ","),
            LABEL_FOODS to log.foodList.joinToString(separator = ","),
            LABEL_BEERS to log.additionalData?.beerTypes?.joinToString(separator = ","),
            LABEL_ALCOHOL to log.additionalData?.alcoholLevel?.name,
            LABEL_STRESS to log.additionalData?.stressLevel,
            LABEL_CITY to log.additionalData?.travel?.city,
            LABEL_TRAVELED to log.additionalData?.travel?.traveled,
            LABEL_WEATHER_TEMPERATURE to log.additionalData?.weather?.temperature,
            LABEL_WEATHER_HUMIDITY to log.additionalData?.weather?.humidity
        )
        val firebaseLog = hashMapOf(userId to logMap)
        firebaseDb.collection(LABEL_DATABASE_NAME).add(firebaseLog)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG_FIREBASE, "Logs added to FB: ${documentReference.id}")
                onLogsAdded.invoke(true)
            }
            .addOnFailureListener { exception ->
                Log.e(
                    TAG_FIREBASE,
                    "Exception occurred while adding logs in FB: ${exception.message}"
                )
                onLogsAdded.invoke(false)
            }
    }

    override suspend fun getSyncFirebaseLogs(user: String): List<DailyLogBO> =
        suspendCoroutine { cont ->
            getLogs(user) { cont.resume(it) }
        }


    private fun getLogs(userId: String, onLogsObtained: (List<DailyLogBO>) -> Unit) {

        firebaseDb.collection(LABEL_DATABASE_NAME)
            .get()
            .addOnSuccessListener { result ->
                val firebaseLogsList = mutableListOf<DailyLogBO>()
                for (document in result) {
                    if (document.data.keys.first() == userId) {
                        parseDocumentData(userId, document.data)?.let {
                            firebaseLogsList.add(it)
                        }
                    }
                }
                onLogsObtained.invoke(firebaseLogsList)
            }
            .addOnFailureListener { exception ->
                Log.e(
                    TAG_FIREBASE,
                    "Exception occurred while retrieving logs from FB: ${exception.message}"
                )
                onLogsObtained.invoke(emptyList())
            }
    }
}