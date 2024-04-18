package com.example.databases.realm.dao

import io.realm.Realm
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

interface RealmDataSource {

    val realm: Realm

    private fun <T> executeQueryWithRealm(query: (realm: Realm) -> T): T {
        return run {
//            realm.refresh()
            val result = query(realm)
//            realm.close()
            result
        }
    }

    fun <T> executeTransactionalQuery(query: (realm: Realm) -> T): T {
        return runBlocking {
            suspendCancellableCoroutine<T> { continuation ->
                executeQueryWithRealm { realm ->
                    realm.executeTransaction {
                        val result = query(realm)
                        continuation.resume(result)
                    }
                }
            }
        }
    }

    fun <T> useRealmToQuery(query: (realm: Realm) -> T): T {
        return run {
            realm.refresh()
            realm.use { realm ->
                query(realm)
            }
        }
    }

    fun useRealmTransaction(op: (realm: Realm) -> Unit) {
        realm.refresh()
        realm.use { realm ->
            realm.executeTransaction {
                op(realm)
            }
        }
    }

}
