package com.example.databases.realm.model

import com.example.databases.realm.model.MetricsRealmModel
import io.realm.annotations.RealmModule

@RealmModule(library = true, classes = [MetricsRealmModel::class])
class MetricsRealmModule