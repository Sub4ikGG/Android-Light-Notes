package com.efremov.notebook

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import com.efremov.notebook.databinding.ActivityMainBinding
import com.efremov.notebook.db.dbManager
import com.efremov.notebook.recyclerview.Note
import com.efremov.notebook.recyclerview.NoteAdapter
import com.efremov.notebook.view.CreateNote
import com.efremov.notebook.view.Garbage
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var adapter = NoteAdapter(this, supportFragmentManager)
    private val databaseManager = dbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val colorMain = applicationContext.let { ContextCompat.getColor(it, R.color.main) }
        supportActionBar!!.title = "Заметки"
        supportActionBar!!.setBackgroundDrawable(colorMain.toDrawable())

        binding.noteRecyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.noteRecyclerview.adapter = adapter

        databaseManager.openDatabase()
        try { for(i in databaseManager.readDatabase()) println("Заметка: ${i.id}, ${i.name}, ${i.color}, status: ${i.status}") }
        catch(e: Exception) {
            databaseManager.createDatabase()
            val text = "Для создания первой заметки тебе нужно:" +
                    "\n\n - дать название заметке" +
                    "\n - что-нибудь написать в саму заметку" +
                    "\n - нажать на кнопочку снизу"
            val builder = AlertDialog.Builder(this)
            builder.setIcon(R.drawable.arrow)
            builder.setTitle("Создание первой заметки")
            builder.setMessage(text)
            builder.setPositiveButton("Начать") { _: DialogInterface, _: Int ->
                startActivity(Intent(this, CreateNote::class.java))
            }
            builder.show()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            initRecyclerView()
        }
        catch(e: Exception) {
            println("ЗАЛУПА ЗАЛУПА ЗАЛУПА")
            println(e.message)
            databaseManager.destroyDatabase()
            databaseManager.createDatabase()
            initRecyclerView()
        }
    }

    private fun initRecyclerView() {
        adapter.clearNoteList()

        for(note in databaseManager.readDatabase()) {
            if(note.status != "non-active") {
                val rvNote = Note(note.id, note.name, note.content, note.color.toInt())
                adapter.addNote(rvNote)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.create -> startActivity(Intent(this, CreateNote::class.java))
            R.id.deleted_notes -> startActivity(Intent(this, Garbage::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}