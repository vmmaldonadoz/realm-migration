package com.vmmaldonadoz.realmexample

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.databases.api.Database
import com.example.databases.api.model.Metric
import com.example.databases.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainPresenter(
    private val application: Context
) : ViewModel() {


    private var numberOfRegistries = 1000

    private lateinit var realmDatabase: Database
    private lateinit var roomDatabase: Database

    fun setNumberOfRegistries(numberOfRegistries: Int) {
        this.numberOfRegistries = numberOfRegistries
    }

    fun tryRoom() {
        viewModelScope.launch(Dispatchers.IO) {
            val elapseTime = chronometer {
                roomDatabase = RoomDatabase()
                roomDatabase.createDatabase(application)

                val listToInsert = List(numberOfRegistries) { createRandomRegistry() }
                val stored = roomDatabase.storeData(listToInsert)
                println("Room stored: $stored")

                val data = roomDatabase.retrieveData(500)
                println("Room retrieved: ${data.size}")

                val tableSize = roomDatabase.getSize()
                println("Room size: $tableSize")

                roomDatabase.removeDatabaseFromMemory()
            }
            println("Room took: ${String.format("%.3f", elapseTime.nanosecondsToMillis())}ms")
        }
    }

    fun tryRealm() {

    }

    fun tryRealmMigrationToRoom() {

    }

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
            body = generateRandomString(257)
        )
    }

    private fun generateRandomString(length: Int): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') // Define the character pool
        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    fun Long.nanosecondsToMillis(): Double {
        return this / 1_000_000.0 // 1 millisecond = 1,000,000 nanoseconds
    }

}