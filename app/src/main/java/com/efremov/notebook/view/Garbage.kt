package com.efremov.notebook.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import com.efremov.notebook.R
import com.efremov.notebook.databinding.ActivityGarbageBinding
import com.efremov.notebook.db.dbManager
import com.efremov.notebook.recyclerview.GarbageAdapter
import com.efremov.notebook.recyclerview.Note

class Garbage : AppCompatActivity() {
    lateinit var binding: ActivityGarbageBinding
    private var adapter = GarbageAdapter(this)
    private val databaseManager = dbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGarbageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvGarbage.layoutManager = LinearLayoutManager(this@Garbage)
        binding.rvGarbage.adapter = adapter

        initRecyclerView()

        val colorMain = applicationContext.let { ContextCompat.getColor(it, R.color.main) }
        if (adapter.itemCount > 0) supportActionBar!!.title = "Корзина"
        else supportActionBar!!.title = "Корзина пустая"
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setBackgroundDrawable(colorMain.toDrawable())
    }

    private fun initRecyclerView() {
        databaseManager.openDatabase()

        for (note in databaseManager.readDatabase()) {
            if (note.status == "non-active") {
                val n = Note(note.id, note.name, note.content, note.color.toInt(), "non-active")
                adapter.addNote(n)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}