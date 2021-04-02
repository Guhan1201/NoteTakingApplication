package com.example.roompracticeactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil.Callback
import androidx.recyclerview.widget.DiffUtil.calculateDiff
import androidx.recyclerview.widget.RecyclerView
import com.example.roompracticeactivity.R
import com.example.roompracticeactivity.database.entities.Notes

class NotesListAdapter constructor(
    context: Context?
) : RecyclerView.Adapter<NotesListAdapter.NotesViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notes = ArrayList<Notes>()

    class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notesTitle: TextView = itemView.findViewById(R.id.notes_title) as TextView
        val notesDescription = itemView.findViewById(R.id.notes_description) as TextView
        val parent = itemView.findViewById(R.id.parent) as ConstraintLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val itemView = inflater.inflate(R.layout.note_display, parent, false)
        return NotesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val current = notes[position]
        holder.notesTitle.text = current.notesTitle
        holder.notesDescription.text = current.description
        holder.parent.setBackgroundColor(current.backgroundColor)
    }

    fun setNotes(words: List<Notes>) {
        val diffCallback = NotesListDiffCallback(notes, words)
        val diffResult = calculateDiff(diffCallback)
        notes.clear()
        notes.addAll(words)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = notes.size
}

class NotesListDiffCallback(private val oldList: List<Notes>, private val newList: List<Notes>) :
    Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].notesUid === newList[newItemPosition].notesUid
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (oldValue) = oldList[oldItemPosition]
        val (newValue) = newList[newItemPosition]
        return oldValue == newValue
    }

}