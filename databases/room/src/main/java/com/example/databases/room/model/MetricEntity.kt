package com.example.databases.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "metrics")
data class MetricEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("createdAt")
    val createdAt: Long = 0,
    val body: String? = null,
)
