package com.example.image_zip_downloader

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

object FileOperations {

    fun zipFolder(context: Context, folderUri: Uri): File {
        val zipFile = File(context.cacheDir, "upload_${System.currentTimeMillis()}.zip")
        val contentResolver = context.contentResolver

        ZipOutputStream(FileOutputStream(zipFile)).use { zos ->
            val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(
                folderUri,
                DocumentsContract.getTreeDocumentId(folderUri)
            )
            val cursor = contentResolver.query(
                childrenUri,
                arrayOf(
                    DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                    DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                    DocumentsContract.Document.COLUMN_MIME_TYPE
                ),
                null, null, null
            )
            cursor?.use {
                while (it.moveToNext()) {
                    val docId = it.getString(0)
                    val name = it.getString(1)
                    val mimeType = it.getString(2) ?: ""

                    if (mimeType.startsWith("image/")) {
                        val docUri = DocumentsContract.buildDocumentUriUsingTree(folderUri, docId)
                        contentResolver.openInputStream(docUri)?.use { inputStream ->
                            zos.putNextEntry(ZipEntry(name))
                            inputStream.copyTo(zos)
                            zos.closeEntry()
                        }
                    }
                }
            }
        }
        return zipFile
    }

    fun extractZip(zipFile: File, outputDir: File) {
        outputDir.mkdirs()
        ZipInputStream(zipFile.inputStream()).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                if (!entry.isDirectory) {
                    val outFile = File(outputDir, entry.name)
                    outFile.parentFile?.mkdirs()
                    outFile.outputStream().use { fos ->
                        zis.copyTo(fos)
                    }
                }
                zis.closeEntry()
                entry = zis.nextEntry
            }
        }
    }

    fun extractZipToUri(context: Context, zipFile: File, outputDirUri: Uri): String {
        val contentResolver = context.contentResolver

        // Create a subdirectory named after the zip file (without extension)
        val baseName = zipFile.nameWithoutExtension
        val outputDocUri = DocumentsContract.buildDocumentUriUsingTree(
            outputDirUri,
            DocumentsContract.getTreeDocumentId(outputDirUri)
        )
        val subDirUri = DocumentsContract.createDocument(
            contentResolver, outputDocUri, DocumentsContract.Document.MIME_TYPE_DIR, baseName
        ) ?: throw IllegalStateException("Failed to create output subdirectory")

        ZipInputStream(zipFile.inputStream()).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                if (!entry.isDirectory) {
                    val mimeType = when {
                        entry.name.endsWith(".png", true) -> "image/png"
                        entry.name.endsWith(".jpg", true) || entry.name.endsWith(".jpeg", true) -> "image/jpeg"
                        entry.name.endsWith(".webp", true) -> "image/webp"
                        else -> "application/octet-stream"
                    }
                    val fileUri = DocumentsContract.createDocument(
                        contentResolver, subDirUri, mimeType, entry.name
                    )
                    if (fileUri != null) {
                        contentResolver.openOutputStream(fileUri)?.use { os ->
                            zis.copyTo(os)
                        }
                    }
                }
                zis.closeEntry()
                entry = zis.nextEntry
            }
        }
        return subDirUri.toString()
    }

    fun sha1Fingerprint(file: File): String {
        val digest = MessageDigest.getInstance("SHA-1")
        file.inputStream().use { fis ->
            val buffer = ByteArray(8192)
            var bytesRead: Int
            while (fis.read(buffer).also { bytesRead = it } != -1) {
                digest.update(buffer, 0, bytesRead)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

    fun deleteFile(file: File) {
        file.delete()
    }
}
