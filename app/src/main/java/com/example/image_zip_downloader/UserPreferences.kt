package com.example.image_zip_downloader

import android.content.Context

class UserPreferences(context: Context) {

    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveCredentials(email: String, password: String) {
        prefs.edit()
            .putString(KEY_EMAIL, email)
            .putString(KEY_PASSWORD, password)
            .apply()
    }

    fun getEmail(): String = prefs.getString(KEY_EMAIL, "") ?: ""

    fun getPassword(): String = prefs.getString(KEY_PASSWORD, "") ?: ""

    fun saveSourceFolder(uri: String, name: String) {
        prefs.edit()
            .putString(KEY_SOURCE_FOLDER_URI, uri)
            .putString(KEY_SOURCE_FOLDER_NAME, name)
            .apply()
    }

    fun getSourceFolderUri(): String? = prefs.getString(KEY_SOURCE_FOLDER_URI, null)

    fun getSourceFolderName(): String = prefs.getString(KEY_SOURCE_FOLDER_NAME, "") ?: ""

    fun saveOutputDir(uri: String, name: String) {
        prefs.edit()
            .putString(KEY_OUTPUT_DIR_URI, uri)
            .putString(KEY_OUTPUT_DIR_NAME, name)
            .apply()
    }

    fun getOutputDirUri(): String? = prefs.getString(KEY_OUTPUT_DIR_URI, null)

    fun getOutputDirName(): String = prefs.getString(KEY_OUTPUT_DIR_NAME, "") ?: ""

    companion object {
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"
        private const val KEY_SOURCE_FOLDER_URI = "source_folder_uri"
        private const val KEY_SOURCE_FOLDER_NAME = "source_folder_name"
        private const val KEY_OUTPUT_DIR_URI = "output_dir_uri"
        private const val KEY_OUTPUT_DIR_NAME = "output_dir_name"
    }
}
