package com.example.notes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.dbModel.Note

class NoteAdapter(var notes: List<Note>) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var onClickListener: OnClickListener? = null

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textViewHeader: TextView = itemView.findViewById(R.id.textViewHeader)
        val textViewContent: TextView = itemView.findViewById(R.id.textViewContent)
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.note_tem, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = notes[position]
        holder.textViewHeader.text = currentNote.noteHeader
        holder.textViewContent.text = currentNote.noteContent
        holder.textViewDate.text = currentNote.noteTimeCreation

        holder.itemView.setOnClickListener {
            onClickListener?.onClick(currentNote)
        }
    }

    override fun getItemCount() = notes.size

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(note: Note)
    }
}