package com.example.databases.api

import android.content.Context
import com.example.databases.api.model.Metric

interface Database {

    suspend fun createDatabase(context: Context, databaseName: String)

    suspend fun storeData(data: List<Metric>): Boolean

    suspend fun retrieveData(limit: Int): List<Metric>

    suspend fun removeData(limit: Int): Boolean
    suspend fun getSize(): Long

    suspend fun removeDatabaseFromMemory()
}