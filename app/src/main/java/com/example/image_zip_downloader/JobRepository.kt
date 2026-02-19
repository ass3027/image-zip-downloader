package com.example.image_zip_downloader

import android.content.Context
import com.example.image_zip_downloader.model.Job
import kotlinx.serialization.json.Json
import kotlinx.serialization.builtins.ListSerializer

class JobRepository(context: Context) {

    private val prefs = context.getSharedPreferences("jobs", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }
    private val listSerializer = ListSerializer(Job.serializer())

    fun saveJob(job: Job) {
        val jobs = getJobs().toMutableList()
        jobs.add(0, job)
        prefs.edit().putString("job_list", json.encodeToString(listSerializer, jobs)).apply()
    }

    fun getJobs(): List<Job> {
        val raw = prefs.getString("job_list", null) ?: return emptyList()
        return json.decodeFromString(listSerializer, raw)
    }

    fun updateJobStatus(jobId: String, status: String) {
        val jobs = getJobs().map {
            if (it.jobId == jobId) it.copy(status = status) else it
        }
        prefs.edit().putString("job_list", json.encodeToString(listSerializer, jobs)).apply()
    }
}
