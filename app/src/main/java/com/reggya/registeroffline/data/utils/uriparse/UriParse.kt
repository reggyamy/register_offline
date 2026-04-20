package com.reggya.registeroffline.data.utils.uriparse

import android.content.Context
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import java.io.File

class UriParse(private val context: Context) {
    fun uriToFile(uriString: String): File? {
        return try {
            val uri = uriString.toUri()
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
            val extension = MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(mimeType) ?: "jpg"

            val tempFile = File(context.cacheDir, "upload_${System.currentTimeMillis()}.$extension")
            tempFile.outputStream().use { output ->
                inputStream.copyTo(output)
            }
            inputStream.close()
            tempFile
        } catch (e: Exception) {
            null
        }
    }

    fun getMimeType(file: File): String {
        return when (file.extension.lowercase()) {
            "png" -> "image/png"
            "webp" -> "image/webp"
            "jpg", "jpeg" -> "image/jpeg"
            else -> "image/*"
        }
    }

}