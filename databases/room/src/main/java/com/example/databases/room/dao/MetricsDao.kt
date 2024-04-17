package com.example.databases.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.databases.room.model.MetricEntity

@Dao
interface MetricsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(metric: MetricEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(metric: List<MetricEntity>)

    @Query("SELECT * FROM metrics ORDER BY createdAt ASC LIMIT :limit")
    suspend fun getEvents(limit: Int): List<MetricEntity>

    @Query("DELETE FROM metrics WHERE createdAt IN (SELECT createdAt FROM metrics ORDER BY createdAt ASC LIMIT :limit)")
    suspend fun deleteEvents(limit: Int)

    @Query("SELECT COUNT(*) FROM metrics")
    suspend fun getSize(): Long
}
