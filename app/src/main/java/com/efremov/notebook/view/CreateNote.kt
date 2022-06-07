package com.efremov.notebook.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import com.efremov.notebook.R
import com.efremov.notebook.databinding.ActivityCreateNoteBinding
import com.efremov.notebook.db.dbManager
import com.efremov.notebook.model.DataNote

class CreateNote : AppCompatActivity() {
    lateinit var binding: ActivityCreateNoteBinding
    private var launcher: ActivityResultLauncher<Intent>? = null
    private val databaseManager = dbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val colorMain = applicationContext.let { ContextCompat.getColor(it, R.color.main) }
        supportActionBar!!.title = "Создать заметку"
        supportActionBar!!.setBackgroundDrawable(colorMain.toDrawable())
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        databaseManager.openDatabase()
        binding.apply {
            val index = databaseManager.getIndex()
            val noteName = "Заметка #$index"
            binding.noteNameEdittext.setText(noteName)
            personNoteButton.setOnClickListener {
                val name = noteNameEdittext.text.toString()
                val content = noteContent.text.toString()
                if(name.isNotEmpty() && content.isNotEmpty()) {
                    val intent = Intent(applicationContext, PersonalizationNote::class.java)
                    intent.putExtra("note_name", name)
                    intent.putExtra("note_content", content)
                    launcher?.launch(intent)
                }
                else {
                    if(name.isEmpty())
                        Toast.makeText(applicationContext, "Название заметки не может быть пустым", Toast.LENGTH_SHORT).show()
                    else if(content.isEmpty())
                        Toast.makeText(applicationContext, "Текст заметки не может быть пустым", Toast.LENGTH_SHORT).show()
                    else if(name.isEmpty() && content.isEmpty())
                        Toast.makeText(applicationContext, "Название и текст заметки не могут быть пустыми", Toast.LENGTH_SHORT).show()
                }
            }

            createNoteButton.setOnClickListener {
                val n = binding.noteNameEdittext.text.toString()
                val c = binding.noteContent.text.toString()
                if(n.isNotEmpty() && c.isNotEmpty()) {
                    databaseManager.insertToDatabase(DataNote(0, n, c, "2131099697", "active"))
                    Toast.makeText(
                        applicationContext,
                        "Заметка успешно создана",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
                else Toast.makeText(applicationContext, "Текст заметки не может быть пустым", Toast.LENGTH_SHORT).show()
            }
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if(result.resultCode == RESULT_OK) {
                val color = result.data?.getIntExtra("note_color", 0)
                val n = binding.noteNameEdittext.text.toString()
                val c = binding.noteContent.text.toString()
                databaseManager.insertToDatabase(DataNote(0, n, c, color.toString(), "active"))
                Toast.makeText(applicationContext, "Заметка успешно создана", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseManager.closeDatabase()
    }
}