package com.androidai.module.geminiapi.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.result.ActivityResult
import java.io.File
import kotlin.math.log10
import kotlin.math.pow


data class FileUriInfo( val name: String, val fileSize: Long, val mimeType: String,
                        val data:ByteArray?,
                    val path: String)

object AttachmentFileUtils {

    const val MAX_ATTACHMENT_COUNT = 10
    const val MAX_ATTACHMENT_SIZE = 1024 * 1024 * 10 // 10 MB

    const val OPTION_VIEW = 1
    const val OPTION_SHARE = 2

    private const val INVALID_FILE_NAME_CHAR = "[\\\\/:*?\"<>|]"

    fun isFileExist(filePath: String) = getFile(filePath).exists()

    private fun getFile(filePath: String) = File(filePath)

    fun getCacheFile(cachePath: String, createIfNotExist: Boolean = false): File {
        val file = getFile(cachePath)
        if (createIfNotExist) {
            file.parentFile?.mkdirs()
        }
        return file
    }

    fun isAttachmentFileExistInUri(context: Context, localPath: String): Boolean {
        val contentResolver = context.contentResolver
        var cursor: Cursor? = null
        return try {
            cursor = contentResolver.query(Uri.parse(localPath), null, null, null, null)
            cursor != null && cursor.count > 0
        } catch (e: Exception) {
            false
        } finally {
            cursor?.close()
        }
    }

    fun getAttachmentEntityByUri(
        context: Context,
        uri: Uri
    ): FileUriInfo? {

        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(uri, null, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)

                val name = it.getString(nameIndex)
                val size = it.getLong(sizeIndex)

                // Get the MIME type of the file
                val type = contentResolver.getType(uri) ?: "*/*"


                return FileUriInfo(
                    name = name,
                    fileSize = size,
                    mimeType = type,
                    data = readBytes(context, uri),
                    path = uri.toString()
                )
            }
        }

        return null

    }

    private fun readBytes(context: Context, uri: Uri): ByteArray? =
        context.contentResolver.openInputStream(uri)?.readBytes()

    fun Long.toFormattedSize(): String {
        if (this <= 0) return "0 B"

        val units = arrayOf("B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")
        val digitGroups = (log10(this.toDouble()) / log10(1024.0)).toInt()

        return String.format(
                "%.2f %s",
                this / 1024.0.pow(digitGroups.toDouble()),
                units[digitGroups]
        )
    }


    fun deleteFilesIfExists(path: String) {
        val folder = getFile(path)
        if (folder.exists()) {
            folder.deleteRecursively()
        }
    }

    fun getAttachmentContentTypeByMime(type: String): AttachmentContentType {
        return when (type) {
            "image/jpeg", "image/png", "image/webp", " image/svg+xml" -> AttachmentContentType.IMAGE
            "audio/wav", "audio/mpeg" -> AttachmentContentType.MUSIC
            "video/mp4" -> AttachmentContentType.VIDEO
            "text/plain" -> AttachmentContentType.TEXT
            "application/zip" -> AttachmentContentType.ZIP
            "application/pdf" -> AttachmentContentType.PDF
            "application/xlsx,application/xls" -> AttachmentContentType.EXCEL
            "application/doc,application/docx" -> AttachmentContentType.WORD
            else -> AttachmentContentType.UNKNOWN
        }
    }


    /*fun saveFileToCache(contentResolver: ContentResolver, localPath: String, cachePath: String) {
        contentResolver.openInputStream(Uri.parse(localPath))?.use { inputStream ->
            val file = getCacheFile(cachePath, createIfNotExist = true)
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
                outputStream.close()
            }
            inputStream.close()
        }
    }*/

    fun getDownloadFile(downloadFolderPath: String, fileName: String): File {
        val renamedFileName = fileName.replace(Regex(INVALID_FILE_NAME_CHAR), "")
        File(downloadFolderPath, renamedFileName).apply {
            parentFile?.mkdirs()
            return this
        }
    }

    fun getAttachmentIntent() : Intent {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        return intent
    }

    fun getFileUriInfoListFromActivityResult(context : Context, result : ActivityResult) : List<FileUriInfo> {
       return if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val listOfFiles = mutableListOf<FileUriInfo>()
            if (result.data?.clipData != null) {
                result.data!!.clipData!!.let {
                    for (i in 0 until it.itemCount) {
                        val fileURIInfo = getAttachmentEntityByUri(context, it.getItemAt(i).uri)
                        if(fileURIInfo != null) {
                            listOfFiles.add(fileURIInfo)
                        }
                    }
                }
            } else if (result.data?.data != null) {
                val fileURIInfo = getAttachmentEntityByUri(context, result.data!!.data!!)
                if(fileURIInfo != null) {
                    listOfFiles.add(fileURIInfo)
                }
            }
           listOfFiles
        }else{
            listOf()
        }
    }

}