package com.vmmaldonadoz.realmexample.db.realm

import android.util.Log
import com.vmmaldonadoz.realmexample.db.DatabaseDemo
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import java.util.Date

class RealmDemo : DatabaseDemo {

    override fun execute() {
        val realmName = "My Project"
        val config = RealmConfiguration.Builder(schema = setOf(Task::class)).name(realmName).build()

        val backgroundThreadRealm: Realm = Realm.open(config)

        val task: Task = Task()
        task.name = "New Task ${Date().time}"
        backgroundThreadRealm.writeBlocking {
            copyToRealm(task)
        }

        // all tasks in the realm
        val tasks: RealmResults<Task> = backgroundThreadRealm.query<Task>().find()

        // you can also filter a collection
        val tasksThatBeginWithN: List<Task> = tasks.query("name BEGINSWITH $0", "N").find()
        Log.v(
            "QUICKSTART",
            "VMMZ: tasksThatBeginWithN: ${tasksThatBeginWithN.map { it.name }} ",
        )
        val openTasks: List<Task> =
            tasks.query("status == ${TaskStatus.Open.name}").find()
        Log.v(
            "QUICKSTART",
            "VMMZ: openTasks: ${openTasks.map { it.name }} ",
        )

        val otherTask: Task = tasks[0]
        Log.v(
            "QUICKSTART",
            "VMMZ: otherTask: $otherTask ",
        )

        // because this background thread uses synchronous realm transactions, at this point all
        // transactions have completed and we can safely close the realm
        backgroundThreadRealm.close()
    }

}
