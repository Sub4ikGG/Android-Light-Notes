package com.efremov.notebook

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.widget.addTextChangedListener
import com.efremov.notebook.data.DataNote
import com.efremov.notebook.databinding.ActivityMyNoteBinding
import com.efremov.notebook.db.dbManager

class MyNote : AppCompatActivity() {
    private var launcher: ActivityResultLauncher<Intent>? = null
    private val databaseManager = dbManager(this)
    lateinit var binding: ActivityMyNoteBinding
    private var lock = true
    private var name = ""
    private var content = ""
    private var id = 0
    private var color = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        name = intent.getStringExtra("note_name").toString()
        content = intent.getStringExtra("note_content").toString()
        id = intent.getIntExtra("note_id", 0)
        color = intent.getIntExtra("note_color", 0)

        val colorMain = applicationContext.let { ContextCompat.getColor(it, R.color.main) }
        supportActionBar!!.title = name
        supportActionBar!!.setBackgroundDrawable(colorMain.toDrawable())
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        binding.mynoteName.isEnabled = false
        binding.mynoteContent.isEnabled = false

        binding.mynoteName.setText(name)
        binding.mynoteContent.setText(content)

        databaseManager.openDatabase()

        binding.mynoteName.addTextChangedListener {
            supportActionBar!!.title = binding.mynoteName.text
        }

        binding.myNoteLockButton.setOnClickListener {
            val color_open = ContextCompat.getColor(applicationContext, R.color.color_note_3)
            val color_close = ContextCompat.getColor(applicationContext, R.color.color_note_6)

            if(lock) {
                binding.myNoteLockButton.setImageResource(R.drawable.ic_baseline_lock_open_24)
                binding.myNoteLockButton.backgroundTintList = ColorStateList.valueOf(color_open)

                binding.mynoteName.isEnabled = true
                binding.mynoteContent.isEnabled = true
            }
            else {
                binding.myNoteLockButton.setImageResource(R.drawable.ic_baseline_lock_24)
                binding.myNoteLockButton.backgroundTintList = ColorStateList.valueOf(color_close)

                binding.mynoteName.isEnabled = false
                binding.mynoteContent.isEnabled = false
            }

            lock = !lock
        }

        binding.saveNoteButton.setOnClickListener {
            if(name != binding.mynoteName.text.toString() || content != binding.mynoteContent.text.toString()) {
                if(binding.mynoteName.text.toString().isNotEmpty() && binding.mynoteContent.text.toString().isNotEmpty()) {
                    databaseManager.updateNote(
                        DataNote(
                            id,
                            binding.mynoteName.text.toString(),
                            binding.mynoteContent.text.toString(),
                            color.toString(),
                            "active"
                        )
                    )
                    Toast.makeText(this, "Заметка успешно изменена", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Удаление заметки")
                    builder.setMessage("Вы точно хотите удалить '$name?'")
                    builder.setPositiveButton("Удалить") { _: DialogInterface, _: Int ->
                        databaseManager.openDatabase()
                        databaseManager.clearNote(DataNote(id, name, content, color.toString(), "non-active"))
                        Toast.makeText(this, "Заметка успешно удалена", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    builder.setNegativeButton("Оставить") { _: DialogInterface, _: Int -> }
                    builder.show()
                }
            }
        }

        binding.personNoteButtonMynote.setOnClickListener {
            val n = binding.mynoteName.text.toString()
            val c = binding.mynoteContent.text.toString()
            if(n.isNotEmpty() && c.isNotEmpty()) {
                val intent = Intent(this, PersonalizationNote::class.java)
                intent.putExtra("note_name", n)
                intent.putExtra("note_content", c)
                intent.putExtra("note_id", id)
                intent.putExtra("note_color", color)
                launcher?.launch(intent)
            }
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if(result.resultCode == RESULT_OK) {
                val color = result.data?.getIntExtra("note_color", 0)
                val n = binding.mynoteName.text.toString()
                val c = binding.mynoteContent.text.toString()
                databaseManager.updateNote(DataNote(id, n, c, color.toString(), "active"))
                Toast.makeText(applicationContext, "Заметка успешно изменена", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_nav_menu_mynote, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.clear_note) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Удаление заметки")
            builder.setMessage("Вы точно хотите удалить '$name?'")
            builder.setPositiveButton("Удалить") { _: DialogInterface, _: Int ->
                databaseManager.openDatabase()
                databaseManager.updateNote(
                    DataNote(
                        id,
                        name,
                        content,
                        color.toString(),
                        "non-active"
                    )
                )
                Toast.makeText(this, "Заметка успешно удалена", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
            builder.setNegativeButton("Оставить") { _: DialogInterface, _: Int -> }
            builder.show()
        }
        return super.onOptionsItemSelected(item)
    }
}