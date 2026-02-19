package com.example.image_zip_downloader.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val user: User,
    val tokens: Tokens,
    val subscription: Map<String, String> = emptyMap()
)

@Serializable
data class User(
    val email: String,
    val dateCreated: String,
    val emailStatus: String
)

@Serializable
data class Tokens(
    val accessToken: String,
    val refreshToken: String
)

@Serializable
data class JobStatusResponse(
    val userId: String = "",
    val jobId: String,
    val totalImageCount: Int = 0,
    val translatedImageCount: Int = 0,
    val contextPageCount: Int = 0,
    val status: String = "",
    val subscriptionTier: String = "",
    val targetLangCode: String = "",
    val translationModel: String = "",
    val advancedOcrDisabled: Boolean = false,
    val fileName: String = "",
    val contentType: String = "",
    val internal: String? = null,
    val fileLocation: String? = null,
    val imageLimit: Int = 0,
    val createdAt: String = ""
)

@Serializable
data class Job(
    val jobId: String,
    val folderName: String,
    val status: String,
    val outputPath: String,
    val createdAt: Long
)
