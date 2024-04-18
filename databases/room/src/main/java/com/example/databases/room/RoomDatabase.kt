package com.example.databases.room

import android.content.Context
import androidx.room.Room
import com.example.databases.api.Database
import com.example.databases.api.model.Metric
import com.example.databases.room.dao.MetricsDao
import com.example.databases.room.model.MetricEntity

class RoomDatabase : Database {

    private var database: MetricsDatabase? = null
    private var dao: MetricsDao? = null
    override suspend fun createDatabase(context: Context, databaseName: String) {
        database = Room.databaseBuilder(
            context,
            MetricsDatabase::class.java,
            databaseName,
        ).build()
        dao = database!!.metricsDao()
    }

    override suspend fun storeData(data: List<Metric>): Boolean {
        return try {
            dao!!.insert(
                data.map {
                    MetricEntity(
                        createdAt = it.createdAt,
                        body = it.body,
                    )
                }
            )
            true
        } catch (exception: Exception) {
            false
        }
    }

    override suspend fun retrieveData(limit: Int): List<Metric> {
        return dao!!.getEvents(limit)
            .map {
                Metric(
                    id = it.id,
                    createdAt = it.createdAt,
                    body = it.body,
                )
            }
    }

    override suspend fun removeData(limit: Int): Boolean {
        return try {
            dao!!.deleteEvents(limit)
            true
        } catch (exception: Exception) {
            false
        }
    }

    override suspend fun getSize(): Long {
        return dao!!.getSize()
    }

    override suspend fun removeDatabaseFromMemory() {
        dao = null
        database?.close()
        database = null
    }
}