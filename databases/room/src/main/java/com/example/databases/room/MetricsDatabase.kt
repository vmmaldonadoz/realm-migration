package com.example.databases.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.databases.room.dao.MetricsDao
import com.example.databases.room.model.MetricEntity

@Database(
    entities = [
        MetricEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class MetricsDatabase : RoomDatabase() {
    abstract fun metricsDao(): MetricsDao
}
