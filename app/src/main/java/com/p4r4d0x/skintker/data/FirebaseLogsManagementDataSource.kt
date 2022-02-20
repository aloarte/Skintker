package com.p4r4d0x.skintker.data

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.parsers.DataParser.parseDocumentData

class FirebaseLogsManagementDataSource {

    private val firebaseDb = Firebase.firestore

    fun addLog(userId: String, log: DailyLogBO) {
        val logMap = hashMapOf(
            "date" to log.date,
            "irritation" to log.irritation?.overallValue,
            "irritatedZones" to log.irritation?.zoneValues?.joinToString(separator = ","),
            "foods" to log.foodList.joinToString(separator = ","),
            "beers" to log.additionalData?.beerTypes?.joinToString(separator = ","),
            "alcohol" to log.additionalData?.alcoholLevel?.name,
            "stress" to log.additionalData?.stressLevel,
            "city" to log.additionalData?.travel?.city,
            "traveled" to log.additionalData?.travel?.traveled,
            "weatherTemperature" to log.additionalData?.weather?.temperature,
            "humidityTemperature" to log.additionalData?.weather?.humidity
        )
        val firebaseLog = hashMapOf(userId to logMap)


        firebaseDb.collection("DailyLogs").add(firebaseLog)
            .addOnSuccessListener { documentReference ->
                Log.d("ALRALR", "14 DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("ALRALR", "14 Error adding document", e)
            }
    }

    fun getLogs(userId: String, onLogsObtained: (List<DailyLogBO>) -> Unit) {

        firebaseDb.collection("DailyLogs")
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
                Log.w("24ALRALR", "Error getting documents.", exception)
            }
    }


}