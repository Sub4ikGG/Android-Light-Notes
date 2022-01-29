package com.efremov.notebook

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import com.efremov.notebook.databinding.ActivityPersonalizationNoteBinding
import com.efremov.notebook.db.dbManager

class PersonalizationNote : AppCompatActivity() {
    private val databaseManager = dbManager(this)
    lateinit var binding: ActivityPersonalizationNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalizationNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val colorMain = applicationContext.let { ContextCompat.getColor(it, R.color.main) }
        supportActionBar!!.title = "Персонализация заметки"
        supportActionBar!!.setBackgroundDrawable(colorMain.toDrawable())
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        binding.apply {
            createNoteButton.setOnClickListener {
                if(selectedColor() != 0) {
                    databaseManager.openDatabase()

                    val color_ = selectedColor()
                    val intent = Intent()
                    intent.putExtra("note_color", color_)
                    setResult(RESULT_OK, intent)
                    finish()
                }
                else {
                    Toast.makeText(applicationContext, "Необходимо выбрать цвет заметки", Toast.LENGTH_SHORT).show()
                }
            }

            rbColor1.isChecked = true
            rbColor1.setOnCheckedChangeListener { _, checked ->
                if(checked) {
                    clearChecks(1)
                    colorView.setBackgroundColor(ContextCompat.getColor(application, R.color.color_note_1))
                }
            }
            rbColor2.setOnCheckedChangeListener { _, checked ->
                if(checked) {
                    clearChecks(2)
                    colorView.setBackgroundColor(ContextCompat.getColor(application, R.color.color_note_2))
                }
            }
            rbColor3.setOnCheckedChangeListener { _, checked ->
                if(checked) {
                    clearChecks(3)
                    colorView.setBackgroundColor(ContextCompat.getColor(application, R.color.color_note_3))
                }
            }
            rbColor4.setOnCheckedChangeListener { _, checked ->
                if(checked) {
                    clearChecks(4)
                    colorView.setBackgroundColor(ContextCompat.getColor(application, R.color.color_note_4))
                }
            }
            rbColor5.setOnCheckedChangeListener { _, checked ->
                if(checked) {
                    clearChecks(5)
                    colorView.setBackgroundColor(ContextCompat.getColor(application, R.color.color_note_5))
                }
            }
            rbColor6.setOnCheckedChangeListener { _, checked ->
                if(checked) {
                    clearChecks(6)
                    colorView.setBackgroundColor(ContextCompat.getColor(application, R.color.color_note_6))
                }
            }
            rbColor7.setOnCheckedChangeListener { _, checked ->
                if(checked) {
                    clearChecks(7)
                    colorView.setBackgroundColor(ContextCompat.getColor(application, R.color.color_note_7))
                }
            }
            rbColor8.setOnCheckedChangeListener { _, checked ->
                if(checked) {
                    clearChecks(8)
                    colorView.setBackgroundColor(ContextCompat.getColor(application, R.color.color_note_8))
                }
            }
            rbColor9.setOnCheckedChangeListener { _, checked ->
                if(checked) {
                    clearChecks(9)
                    colorView.setBackgroundColor(ContextCompat.getColor(application, R.color.color_note_9))
                }
            }

            buttonFont1.setOnClickListener {
                buttonFont1.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.color_note_3))
                buttonFont2.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.color_note_2))
                buttonFont3.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.color_note_2))
            }
            buttonFont2.setOnClickListener {
                buttonFont2.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.color_note_3))
                buttonFont1.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.color_note_2))
                buttonFont3.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.color_note_2))
            }
            buttonFont3.setOnClickListener {
                buttonFont3.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.color_note_3))
                buttonFont1.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.color_note_2))
                buttonFont2.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.color_note_2))
            }
        }
    }

    private fun selectedColor(): Int {
        binding.apply {
            return when {
                rbColor1.isChecked -> R.color.color_note_1
                rbColor2.isChecked -> R.color.color_note_2
                rbColor3.isChecked -> R.color.color_note_3
                rbColor4.isChecked -> R.color.color_note_4
                rbColor5.isChecked -> R.color.color_note_5
                rbColor6.isChecked -> R.color.color_note_6
                rbColor7.isChecked -> R.color.color_note_7
                rbColor8.isChecked -> R.color.color_note_8
                rbColor9.isChecked -> R.color.color_note_9
                else -> 0
            }
        }
    }

    private fun clearChecks(id: Int) {
        binding.apply {
            if (id != 1 && rbColor1.isChecked) rbColor1.isChecked = false
            if (id != 2 && rbColor2.isChecked) rbColor2.isChecked = false
            if (id != 3 && rbColor3.isChecked) rbColor3.isChecked = false
            if (id != 4 && rbColor4.isChecked) rbColor4.isChecked = false
            if (id != 5 && rbColor5.isChecked) rbColor5.isChecked = false
            if (id != 6 && rbColor6.isChecked) rbColor6.isChecked = false
            if (id != 7 && rbColor7.isChecked) rbColor7.isChecked = false
            if (id != 8 && rbColor8.isChecked) rbColor8.isChecked = false
            if (id != 9 && rbColor9.isChecked) rbColor9.isChecked = false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}