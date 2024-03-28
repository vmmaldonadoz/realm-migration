package com.vmmaldonadoz.realmexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vmmaldonadoz.realmexample.db.DatabaseDemo
import com.vmmaldonadoz.realmexample.db.realm.RealmDemo

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val databaseDemo: DatabaseDemo = RealmDemo()
        databaseDemo.execute()
    }

}

