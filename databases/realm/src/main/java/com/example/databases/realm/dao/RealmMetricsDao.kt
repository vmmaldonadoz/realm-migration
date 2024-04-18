package com.example.databases.realm.dao

import com.example.databases.api.model.Metric
import com.example.databases.realm.model.MetricsRealmModel
import io.realm.Realm
import io.realm.Sort

class RealmMetricsDao(override val realm: Realm) : RealmDataSource {

    suspend fun insert(metric: Metric) {
        executeTransactionalQuery {
            mapToMetricsRealmModel(realm, metric)?.let {
                realm.insert(it)
            }
        }
    }

    suspend fun insert(metric: List<Metric>) {
        executeTransactionalQuery {
            val values = metric.mapNotNull { mapToMetricsRealmModel(realm, it) }
            it.insert(values)
        }
    }

    suspend fun getEvents(limit: Int): List<Metric> {
        return executeTransactionalQuery { realm ->
            realm.where(QUERY_CLASS)
                .sort(FIELD_CREATED_AT, Sort.ASCENDING)
                .limit(limit.toLong())
                .findAll()
                ?: arrayListOf()
        }.map { it.mapToMetric() }
    }

    suspend fun deleteEvents(limit: Int) {
        executeTransactionalQuery { realm ->
            realm.where(QUERY_CLASS)
                .sort(FIELD_CREATED_AT, Sort.ASCENDING)
                .limit(limit.toLong())
                .findAll()
                .deleteAllFromRealm()
        }
    }

    suspend fun getSize(): Long {
        return executeTransactionalQuery { realm ->
            realm.where(QUERY_CLASS).count()
        }
    }

    private fun MetricsRealmModel.mapToMetric(): Metric {
        return Metric(
            createdAt = createdAt ?: 0L,
            body = this.body,
        )
    }

    private fun mapToMetricsRealmModel(
        realm: Realm,
        metric: Metric
    ): MetricsRealmModel? {
        val metricsRealmModel = realm.createObject(QUERY_CLASS).apply {
            createdAt = metric.createdAt
            body = metric.body
        }
        return metricsRealmModel
    }

    private companion object {
        val QUERY_CLASS = MetricsRealmModel::class.java
        const val FIELD_CREATED_AT = "createdAt"
    }
}
