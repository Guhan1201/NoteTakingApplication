package com.example.roompracticeactivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotesListAdapter internal constructor(
    context: Context?
) : RecyclerView.Adapter<NotesListAdapter.WordViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notes = emptyList<Notes>() // Cached copy of words

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notesTitle = itemView.findViewById<TextView>(R.id.title)
        val notesDescription = itemView.findViewById<TextView>(R.id.description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.note_display, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = notes[position]
        holder.notesTitle.text = current.noteName
        holder.notesDescription.text = current.description
    }

    internal fun setWords(words: List<Notes>) {
        this.notes = words
        notifyDataSetChanged()
    }

    override fun getItemCount() = notes.size
}