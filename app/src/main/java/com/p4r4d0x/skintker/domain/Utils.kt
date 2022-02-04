package com.p4r4d0x.skintker.domain

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.File
import java.net.URL

fun generateFile(context: Context, fileName: String): File? {
    val csvFile = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//        context.getExternalFilesDir(null),
        fileName
    )
    csvFile.createNewFile()

    return if (csvFile.exists()) {
        csvFile
    } else {
        null
    }
}

fun openFile(uri: Uri): File? {
    return uri.path?.let { path ->
        val csvFile = File(path)
        if (csvFile.exists()) {
            csvFile
        } else {
            null
        }
    } ?: run {
        null
    }

}


@RequiresApi(Build.VERSION_CODES.Q)
private fun saveFileUsingMediaStore(context: Context, url: String, fileName: String) {
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }
    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
    if (uri != null) {
        URL(url).openStream().use { input ->
            resolver.openOutputStream(uri).use { output ->
                input.copyTo(output!!, DEFAULT_BUFFER_SIZE)
            }
        }
    }
}