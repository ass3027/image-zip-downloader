package com.example.image_zip_downloader

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.image_zip_downloader.model.Job
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = TranslatorApiService()
    private val jobRepository = JobRepository(application)

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoggedIn by mutableStateOf(false)
    var accessToken by mutableStateOf("")
    var refreshToken by mutableStateOf("")

    var selectedFolderUri by mutableStateOf<Uri?>(null)
    var selectedFolderName by mutableStateOf("")
    var outputDirUri by mutableStateOf<Uri?>(null)
    var outputDirName by mutableStateOf("")

    var isProcessing by mutableStateOf(false)
    var currentStatus by mutableStateOf("")
    var jobs by mutableStateOf(listOf<Job>())
    var errorMessage by mutableStateOf<String?>(null)

    init {
        jobs = jobRepository.getJobs()
    }

    fun login() {
        viewModelScope.launch {
            isProcessing = true
            errorMessage = null
            currentStatus = "Logging in..."
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.login(email, password)
                }
                accessToken = response.tokens.accessToken
                refreshToken = response.tokens.refreshToken
                isLoggedIn = true
                currentStatus = ""
            } catch (e: Exception) {
                errorMessage = "Login failed: ${e.message}"
                currentStatus = ""
            } finally {
                isProcessing = false
            }
        }
    }

    fun startTranslation() {
        val folderUri = selectedFolderUri ?: return
        val outUri = outputDirUri ?: return

        viewModelScope.launch {
            isProcessing = true
            errorMessage = null
            try {
                // Step 1: Zip
                currentStatus = "Zipping images..."
                val zipFile = withContext(Dispatchers.IO) {
                    FileOperations.zipFolder(getApplication(), folderUri)
                }

                // Step 2: SHA-1
                currentStatus = "Computing fingerprint..."
                val fingerprint = withContext(Dispatchers.IO) {
                    FileOperations.sha1Fingerprint(zipFile)
                }

                // Step 3: Upload
                currentStatus = "Uploading..."
                val uploadResponse = withContext(Dispatchers.IO) {
                    apiService.upload(accessToken, refreshToken, zipFile, fingerprint)
                }
                val jobId = uploadResponse.jobId

                // Step 4: Poll
                currentStatus = "Translating (0/${uploadResponse.totalImageCount})..."
                var statusResponse = uploadResponse
                while (statusResponse.translatedImageCount < statusResponse.totalImageCount) {
                    delay(3000)
                    statusResponse = withContext(Dispatchers.IO) {
                        apiService.getStatus(accessToken, refreshToken, jobId)
                    }
                    currentStatus = "Translating (${statusResponse.translatedImageCount}/${statusResponse.totalImageCount})..."
                }

                // Step 5: Download
                currentStatus = "Downloading result..."
                val downloadedZip = withContext(Dispatchers.IO) {
                    val responseBody = apiService.download(accessToken, refreshToken, jobId)
                    val file = File(getApplication<Application>().cacheDir, "result_$jobId.zip")
                    file.outputStream().use { fos ->
                        responseBody.byteStream().copyTo(fos)
                    }
                    file
                }

                // Step 6: Extract
                currentStatus = "Extracting..."
                val outputPath = withContext(Dispatchers.IO) {
                    FileOperations.extractZipToUri(getApplication(), downloadedZip, outUri)
                }

                // Step 7: Cleanup
                withContext(Dispatchers.IO) {
                    FileOperations.deleteFile(downloadedZip)
                    FileOperations.deleteFile(zipFile)
                }

                // Save job
                val job = Job(
                    jobId = jobId,
                    folderName = selectedFolderName,
                    status = "completed",
                    outputPath = outputPath,
                    createdAt = System.currentTimeMillis()
                )
                jobRepository.saveJob(job)
                jobs = jobRepository.getJobs()

                currentStatus = "Done!"
            } catch (e: Exception) {
                errorMessage = "Translation failed: ${e.message}"
                currentStatus = ""
            } finally {
                isProcessing = false
            }
        }
    }
}
