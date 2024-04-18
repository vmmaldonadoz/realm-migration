package com.vmmaldonadoz.realmexample

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.vmmaldonadoz.realmexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MainPresenter.View {

    private lateinit var binding: ActivityMainBinding

    private val presenter by lazy { MainPresenter(applicationContext, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initListeners()
    }

    private fun initListeners() {
        binding.numberOfRecords.doOnTextChanged { text, _, _, _ ->
            presenter.setNumberOfRegistries(text.toString().toInt())
        }
        binding.tryRealm.setOnClickListener { presenter.tryRealm() }
        binding.tryRoom.setOnClickListener { presenter.tryRoom() }
        binding.tryRealmToRoom.setOnClickListener { presenter.tryRealmMigrationToRoom() }
    }

    override fun showResults(results: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setMessage(results)
            .setTitle("Results")
            .setPositiveButton("Ok") { _, _ -> }

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

}

