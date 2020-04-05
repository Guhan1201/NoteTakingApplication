package com.example.roompracticeactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.roompracticeactivity.R
import com.example.roompracticeactivity.database.entities.Notes

class NotesListAdapter internal constructor(
    context: Context?
) : RecyclerView.Adapter<NotesListAdapter.WordViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notes = emptyList<Notes>() // Cached copy of words

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notesTitle: TextView = itemView.findViewById(R.id.notes_title) as TextView
        val notesDescription = itemView.findViewById(R.id.notes_description) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.note_display, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = notes[position]
        holder.notesTitle.text = current.notesTitle
        holder.notesDescription.text = current.description
    }

    internal fun setWords(words: List<Notes>) {
        this.notes = words
        notifyDataSetChanged()
    }

    internal fun callNotifyDataSetChanged() {
        notifyDataSetChanged()
    }

    override fun getItemCount() = notes.size
}