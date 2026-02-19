package com.example.image_zip_downloader

import com.example.image_zip_downloader.model.JobStatusResponse
import com.example.image_zip_downloader.model.LoginRequest
import com.example.image_zip_downloader.model.LoginResponse
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import java.io.File
import java.util.concurrent.TimeUnit

class TranslatorApiService {

    private val baseUrl = "https://ichigoreader.com"

    private val json = Json { ignoreUnknownKeys = true }

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .build()

    fun login(email: String, password: String): LoginResponse {
        val body = json.encodeToString(LoginRequest.serializer(), LoginRequest(email, password))
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$baseUrl/auth/login")
            .post(body)
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string() ?: throw Exception("Empty response body")
        if (!response.isSuccessful) {
            throw Exception("Login failed (${response.code}): $responseBody")
        }
        return json.decodeFromString(LoginResponse.serializer(), responseBody)
    }

    fun upload(
        accessToken: String,
        refreshToken: String,
        zipFile: File,
        fingerprint: String
    ): JobStatusResponse {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file", zipFile.name,
                zipFile.asRequestBody("application/x-zip-compressed".toMediaType())
            )
            .addFormDataPart("fingerprint", fingerprint)
            .addFormDataPart("targetLangCode", "ko")
            .addFormDataPart("translationModel", "undefined")
            .build()

        val request = Request.Builder()
            .url("$baseUrl/translate/as-reader-format")
            .header("Cookie", buildCookie(accessToken, refreshToken))
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string() ?: throw Exception("Empty response body")
        if (!response.isSuccessful) {
            throw Exception("Upload failed (${response.code}): $responseBody")
        }
        return json.decodeFromString(JobStatusResponse.serializer(), responseBody)
    }

    fun getStatus(accessToken: String, refreshToken: String, jobId: String): JobStatusResponse {
        val request = Request.Builder()
            .url("$baseUrl/translate/as-reader-format/get?jobId=$jobId")
            .header("Cookie", buildCookie(accessToken, refreshToken))
            .get()
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string() ?: throw Exception("Empty response body")
        if (!response.isSuccessful) {
            throw Exception("Status check failed (${response.code}): $responseBody")
        }
        return json.decodeFromString(JobStatusResponse.serializer(), responseBody)
    }

    fun download(accessToken: String, refreshToken: String, jobId: String): ResponseBody {
        val request = Request.Builder()
            .url("$baseUrl/translate/as-reader-format/download?jobId=$jobId")
            .header("Cookie", buildCookie(accessToken, refreshToken))
            .get()
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw Exception("Download failed (${response.code})")
        }
        return response.body ?: throw Exception("Empty download response")
    }

    private fun buildCookie(accessToken: String, refreshToken: String): String {
        return "access_cookie=$accessToken; refresh_token_cookie=$refreshToken"
    }
}
