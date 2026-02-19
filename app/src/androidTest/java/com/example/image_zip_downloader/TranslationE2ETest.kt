package com.example.image_zip_downloader

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@RunWith(AndroidJUnit4::class)
class TranslationE2ETest {

    private val arguments = InstrumentationRegistry.getArguments()
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val tempDir = File(context.cacheDir, "e2e_test_${System.currentTimeMillis()}")
    private val imageDir = File(tempDir, "images")
    private val outputDir = File(tempDir, "output")

    @After
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun fullTranslationWorkflow() = runTest {
        // 1. Generate test PNGs
        imageDir.mkdirs()
        createTestPng(File(imageDir, "test_page_01.png"), Color.RED, "Page 1")
        createTestPng(File(imageDir, "test_page_02.png"), Color.BLUE, "Page 2")

        // 2. Zip the images manually (not using SAF-based zipFolder)
        val zipFile = File(tempDir, "test_upload.zip")
        ZipOutputStream(FileOutputStream(zipFile)).use { zos ->
            imageDir.listFiles()?.sorted()?.forEach { file ->
                zos.putNextEntry(ZipEntry(file.name))
                file.inputStream().use { it.copyTo(zos) }
                zos.closeEntry()
            }
        }
        assertTrue("Zip file should exist", zipFile.exists())
        assertTrue("Zip file should not be empty", zipFile.length() > 0)

        // 3. Compute SHA-1 fingerprint
        val fingerprint = FileOperations.sha1Fingerprint(zipFile)
        assertTrue("Fingerprint should be 40-char hex", fingerprint.length == 40)

        // 4. Login
        val email = arguments.getString("email")
            ?: throw IllegalArgumentException("Missing instrumentation arg: email")
        val password = arguments.getString("password")
            ?: throw IllegalArgumentException("Missing instrumentation arg: password")
        val apiService = TranslatorApiService()
        val loginResponse = apiService.login(email = email, password = password)
        val accessToken = loginResponse.tokens.accessToken
        val refreshToken = loginResponse.tokens.refreshToken
        assertTrue("Access token should not be empty", accessToken.isNotEmpty())

        // 5. Upload
        val uploadResponse = apiService.upload(accessToken, refreshToken, zipFile, fingerprint)
        val jobId = uploadResponse.jobId
        assertTrue("Job ID should not be empty", jobId.isNotEmpty())
        assertTrue("Total image count should be 2", uploadResponse.totalImageCount == 2)

        // 6. Poll for completion (timeout: 120 seconds)
        val pollIntervalMs = 3_000L
        val timeoutMs = 120_000L
        val startTime = System.currentTimeMillis()
        var status: String

        do {
            Thread.sleep(pollIntervalMs)
            val statusResponse = apiService.getStatus(accessToken, refreshToken, jobId)
            status = statusResponse.status

            if (statusResponse.translatedImageCount == statusResponse.totalImageCount
                && statusResponse.totalImageCount > 0
            ) {
                break
            }

            if (System.currentTimeMillis() - startTime > timeoutMs) {
                fail("Translation timed out after ${timeoutMs / 1000}s. Last status: $status")
            }
        } while (true)

        // 7. Download the result zip
        val downloadedZip = File(tempDir, "result.zip")
        val responseBody = apiService.download(accessToken, refreshToken, jobId)
        responseBody.byteStream().use { input ->
            FileOutputStream(downloadedZip).use { output ->
                input.copyTo(output)
            }
        }
        assertTrue("Downloaded zip should exist", downloadedZip.exists())
        assertTrue("Downloaded zip should not be empty", downloadedZip.length() > 0)

        // 8. Extract to output directory
        outputDir.mkdirs()
        FileOperations.extractZip(downloadedZip, outputDir)

        // 9. Assert output files exist and are non-empty
        val outputFiles = outputDir.walkTopDown().filter { it.isFile }.toList()
        assertTrue("Extracted output should contain files", outputFiles.isNotEmpty())
        outputFiles.forEach { file ->
            assertTrue("${file.name} should not be empty", file.length() > 0)
        }
    }

    private fun createTestPng(file: File, bgColor: Int, label: String) {
        val bitmap = Bitmap.createBitmap(200, 300, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(bgColor)

        val paint = Paint().apply {
            color = Color.WHITE
            textSize = 32f
            isAntiAlias = true
        }
        canvas.drawText(label, 30f, 160f, paint)

        FileOutputStream(file).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        }
        bitmap.recycle()
    }
}
