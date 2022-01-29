package com.efremov.notebook.recyclerview

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.efremov.notebook.MyNote
import com.efremov.notebook.R
import com.efremov.notebook.classes.DataNote
import com.efremov.notebook.databinding.NoteItemBinding
import com.efremov.notebook.db.dbManager

@SuppressLint("NotifyDataSetChanged")
class NoteAdapter(val activity: Activity, val fragmentManager: FragmentManager) : RecyclerView.Adapter<NoteAdapter.NoteHolder>() {
    private val noteList = ArrayList<Note>()
    private val databaseManager = dbManager(activity)

    inner class NoteHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = NoteItemBinding.bind(item)

        @SuppressLint("ResourceAsColor")
        fun bind(note: Note) {
            binding.noteItemName.text = note.name
            binding.noteItemContent.text = note.content
            binding.noteBackground.setBackgroundColor(ContextCompat.getColor(activity, note.color))

            if (note.status == "active") {
                binding.noteItemName.setOnClickListener {
                    val intent = Intent(activity, MyNote::class.java)
                    intent.putExtra("note_name", note.name)
                    intent.putExtra("note_content", note.content)
                    intent.putExtra("note_id", note.id)
                    intent.putExtra("note_color", note.color)
                    activity.startActivity(intent)
                }

                binding.noteItemContent.setOnClickListener {
                    val intent = Intent(activity, MyNote::class.java)
                    intent.putExtra("note_name", note.name)
                    intent.putExtra("note_content", note.content)
                    intent.putExtra("note_id", note.id)
                    intent.putExtra("note_color", note.color)
                    activity.startActivity(intent)
                }
            }

            binding.notePin.setOnClickListener {
                val builder = AlertDialog.Builder(activity)
                builder.setIcon(R.drawable.ic_baseline_delete_24)
                builder.setTitle("Удаление заметки")
                builder.setMessage("Вы точно хотите удалить '${note.name}'?")
                builder.setPositiveButton("Удалить") { _: DialogInterface, _: Int ->
                    databaseManager.openDatabase()
                    databaseManager.updateNote(
                        DataNote(
                            note.id,
                            note.name,
                            note.content,
                            note.color.toString(),
                            "non-active"
                        )
                    )
                    Toast.makeText(activity, "Заметка успешно удалена", Toast.LENGTH_SHORT)
                        .show()
                    noteList.remove(note)
                    notifyDataSetChanged()
                }
                builder.setNegativeButton("Оставить") { _: DialogInterface, _: Int -> }
                builder.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteHolder(view)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.bind(noteList[position])
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    fun clearNoteList() {
        noteList.clear()
        notifyDataSetChanged()
    }

    fun addNote(note: Note) {
        noteList.add(note)
        notifyDataSetChanged()
    }
}