package com.example.notes.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.adapter.NoteAdapter
import com.example.notes.databinding.FragmentListOfNotesBinding
import com.example.notes.databinding.FragmentNoteEditBinding
import com.example.notes.dbModel.Note
import com.example.notes.dbModel.NoteDao
import com.example.notes.dbModel.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ListOfNotes : Fragment() {

    lateinit var binding: FragmentListOfNotesBinding

    private lateinit var noteDatabase: NoteDatabase
    private lateinit var noteDao: NoteDao


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListOfNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteDatabase = NoteDatabase.getDatabase(requireContext())
        noteDao = noteDatabase.noteDao()

        binding.rcNotes.layoutManager = GridLayoutManager(requireContext(),2)

        val adapter = NoteAdapter(listOf())
        binding.rcNotes.adapter = adapter

        lifecycleScope.launch {
            val notes = noteDao.getAllNotes()
            Log.d("notes", "$notes")
            adapter.notes = notes
            adapter.notifyDataSetChanged()
        }

        adapter.setOnClickListener(object : NoteAdapter.OnClickListener {
            override fun onClick(note: Note) {
                val id = note.noteId
                val bundle = Bundle().apply {
                    putInt("note_id", id)
                }
                    val noteEditFragment = NoteEditFragment()
                    noteEditFragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentHolder, noteEditFragment)
                    .commit()
            }
        })

        with(binding){
            btnAddNote.setOnClickListener{
                activity
                    ?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.fragmentHolder, NoteEditFragment())
                    ?.commit()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ListOfNotes()
    }
}