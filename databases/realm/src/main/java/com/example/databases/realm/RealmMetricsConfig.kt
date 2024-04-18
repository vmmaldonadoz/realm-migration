package com.example.databases.realm

import com.example.databases.realm.model.MetricsRealmModule
import io.realm.Realm
import io.realm.RealmConfiguration

class RealmMetricsConfig(databaseName: String = "RealmMetricsDatabase") {

    private val config: RealmConfiguration = RealmConfiguration.Builder()
        .name(databaseName)
        .modules(MetricsRealmModule())
        .deleteRealmIfMigrationNeeded()
        .allowQueriesOnUiThread(false)
        .allowWritesOnUiThread(false)
        .build()

    fun getRealm(): Realm = Realm.getInstance(config)
}

