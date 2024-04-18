package com.example.databases.realm

import android.content.Context
import com.example.databases.api.Database
import com.example.databases.api.model.Metric
import com.example.databases.realm.dao.RealmMetricsDao
import io.realm.Realm

class RealmDatabase : Database {

    private var database: Realm? = null
    private var dao: RealmMetricsDao? = null
    override suspend fun createDatabase(context: Context, databaseName: String) {
        Realm.init(context)
        database = RealmMetricsConfig(databaseName).getRealm()
        dao = RealmMetricsDao(database!!)
    }

    override suspend fun storeData(data: List<Metric>): Boolean {
        return try {
            dao!!.insert(data)
            true
        } catch (exception: Exception) {
            false
        }
    }

    override suspend fun retrieveData(limit: Int): List<Metric> {
        return dao!!.getEvents(limit)
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
        database!!.close()
        database = null
    }
}