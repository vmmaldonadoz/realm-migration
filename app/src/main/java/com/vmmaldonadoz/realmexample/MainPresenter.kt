package com.vmmaldonadoz.realmexample

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.databases.api.Database
import com.example.databases.api.model.Metric
import com.example.databases.realm.RealmDatabase
import com.example.databases.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainPresenter(
    private val application: Context,
    private val view: View
) : ViewModel() {


    private var numberOfRegistries = 1000

    fun setNumberOfRegistries(numberOfRegistries: Int) {
        this.numberOfRegistries = numberOfRegistries
    }

    fun tryRoom() {
        tryDatabase(
            databaseName = "RoomMetrics",
            database = RoomDatabase()
        )
    }


    fun tryRealm() {
        tryDatabase(
            databaseName = "RealmMetrics",
            database = RealmDatabase()
        )
    }

    fun tryRealmMigrationToRoom() {
        val realmDatabaseName = "RealmMigration"
        val realmDatabase = RealmDatabase()

        val roomDatabaseName = "RoomMigration"
        val roomDatabase = RoomDatabase()

        viewModelScope.launch(Dispatchers.IO) {
            val results = StringBuilder()
            realmDatabase.createDatabase(application, realmDatabaseName)
            roomDatabase.createDatabase(application, roomDatabaseName)

            val listToInsert = List(numberOfRegistries) { createRandomRegistry() }
            val stored = realmDatabase.storeData(listToInsert)
            logResult("Realm DB stored $numberOfRegistries registries: $stored", results)

            var dataToMigrate: List<Metric> = listOf()
            val queryElapsedTime = chronometer {
                dataToMigrate = realmDatabase.retrieveData(numberOfRegistries)
                logResult("Realm DB retrieved: ${dataToMigrate.size} registries", results)
            }
            logResult("Realm DB query took: ${queryElapsedTime.formatNanos()}ms", results)

            val migrationElapsedTime = chronometer {
                val storeData = roomDatabase.storeData(dataToMigrate)
                check(storeData)

                val removeFromRealm = realmDatabase.removeData(numberOfRegistries)
                check(removeFromRealm)

                val roomData = roomDatabase.retrieveData(numberOfRegistries)
                logResult("Room retrieved: ${roomData.size} registries", results)
            }
            logResult(
                "Migration (store, remove and retrieve ) took: ${migrationElapsedTime.formatNanos()}ms",
                results
            )
            logResult(
                "Complete migration took: ${(migrationElapsedTime + queryElapsedTime).formatNanos()}ms",
                results
            )

            val databaseSize = realmDatabase.getSize()
            logResult(
                message = "Size: $databaseSize",
                results = results
            )

            showResults(results)
        }
    }


    private fun tryDatabase(database: Database, databaseName: String) {
        val registriesToUse = numberOfRegistries
        viewModelScope.launch(Dispatchers.IO) {
            val results = StringBuilder()
            val creationTime = chronometer {
                database.createDatabase(application, databaseName)
            }
            logResult(
                message = "$databaseName creation took: ${creationTime.formatNanos()}ms",
                results = results
            )

            val listToInsert = List(registriesToUse) { createRandomRegistry() }
            val insertOperations = chronometer {
                val stored = database.storeData(listToInsert)
                check(stored)
            }
            logResult(
                message = "$databaseName insertion of $registriesToUse registries took: ${insertOperations.formatNanos()}ms",
                results = results
            )

            val retrieveOperations = chronometer {
                val data = database.retrieveData(registriesToUse)
                check(data.size == registriesToUse)
            }
            logResult(
                message = "$databaseName retrieval of $registriesToUse registries took: ${retrieveOperations.formatNanos()}ms",
                results = results
            )

            val databaseSize = database.getSize()
            logResult(
                message = "$databaseName size: $databaseSize",
                results = results
            )

            database.removeDatabaseFromMemory()

            val elapsedOperationsTime = insertOperations + retrieveOperations
            logResult(
                message = "$databaseName storing and retrieving data took: ${elapsedOperationsTime.formatNanos()}ms",
                results = results
            )

            showResults(results)
        }
    }

    private fun showResults(results: StringBuilder) {
        viewModelScope.launch(Dispatchers.Main) {
            view.showResults(results.toString())
        }
    }

    private fun logResult(
        message: String,
        results: StringBuilder
    ) {
        with(message) {
            println(this)
            results.appendLine(this)
        }
    }

    private fun Long.formatNanos() = String.format("%.2f", this.nanosecondsToMillis())

    private suspend fun chronometer(function: suspend () -> Unit): Long {
        val startTime = System.nanoTime()
        function()
        val endTime = System.nanoTime()
        return endTime - startTime
    }


    private fun createRandomRegistry(): Metric {
        return Metric(
            id = 0,
            createdAt = Random.nextLong(),
            body = generateRandomString()
        )
    }

    private fun generateRandomString(length: Int = 300): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') // Define the character pool
        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    private fun Long.nanosecondsToMillis(): Double {
        return this / 1_000_000.0 // 1 millisecond = 1,000,000 nanoseconds
    }


    interface View {
        fun showResults(results: String)
    }

}