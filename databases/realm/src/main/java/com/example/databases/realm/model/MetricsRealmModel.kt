package com.example.databases.realm.model

import io.realm.RealmObject

open class MetricsRealmModel : RealmObject() {
    var createdAt: Long? = null
    var body: String? = null
}