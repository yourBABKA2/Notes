package com.example.notes.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.notes.R
import com.example.notes.databinding.FragmentNoteEditBinding
import com.example.notes.databinding.FragmentRegistrationBinding
import com.example.notes.dbModel.Note
import com.example.notes.dbModel.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class NoteEditFragment : Fragment() {
    private lateinit var binding: FragmentNoteEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNoteEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Получение ссылки на экземпляр базы данных
        val noteDatabase = NoteDatabase.getDatabase(requireContext())
        //Получение ссылки на userDao
        val userDao = noteDatabase.userDao()
        //Получение ссылки на noteDao
        val noteDao = noteDatabase.noteDao()

        val currentDate = Date()
        val dateFormat = SimpleDateFormat("HH:mm/dd/MM")
        val formattedDate = dateFormat.format(currentDate)

        val dateString: String = formattedDate
        binding.textViewCurrentDate.text = dateString

        //Вывод данных из бд в textView
        lifecycleScope.launch {
            val userList = userDao.getAllUsers()
            val wellcome = "Здравствуйте " + userList[0].userFullName
            if (userList.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    binding.textViewName.text = wellcome
                }
            }
        }
        //Обработчик нажатия на кнопки назад
        with(binding) {
            btnBack.setOnClickListener {
                activity
                    ?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.fragmentHolder, ListOfNotes())
                    ?.commit()
            }
        }
        val id = arguments?.getInt("note_id")
        Log.d("note_id", "$id")

        //Если id != null, то происходит выборка из базы данных заметки по её id
        if (id != null) {

            lifecycleScope.launch {

                val note = noteDao.getNoteById(id)
                withContext(Dispatchers.Main) {
                    binding.editTextHeader.setText(note.noteHeader)
                    binding.editTextNote.setText(note.noteContent)
                }
            }
        }

        with(binding) {
            btnDel.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Удаление заметки")
                builder.setMessage("Вы точно хотите удалить данную заметку?")
                builder.setPositiveButton("Да") { dialog, which ->
                    lifecycleScope.launch {
                        id?.let { it1 -> noteDao.delete(it1) }
                        activity
                            ?.supportFragmentManager
                            ?.beginTransaction()
                            ?.replace(R.id.fragmentHolder, ListOfNotes())
                            ?.commit()
                    }
                }
                builder.setNegativeButton("Нет", null)
                val dialog = builder.create()
                dialog.show()
            }
        }

        //Кнопка сохранения заметки
        with(binding) {
            btnSave.setOnClickListener {
                val _noteHeader = editTextHeader.text.toString()
                val _noteContent = editTextNote.text.toString()
                val _noteTimeCreation = System.currentTimeMillis()

                //Проверка полей на заполнение
                if (_noteHeader == "" || _noteContent == "") {
                    Toast.makeText(requireContext(), "Не все данные указаны", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                } else {
                    //Создание AlertDialog
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Сохранение заметки")
                    builder.setMessage("Вы точно хотите сохранить данную заметку?")
                    builder.setPositiveButton("Да") { dialog, which ->
                        val note = Note(
                            noteHeader = _noteHeader,
                            noteContent = _noteContent,
                            noteTimeCreation = dateString
                        )
                        //Если id = null, то тогда происходит создание новой заметки
                        if (id == null) {
                            lifecycleScope.launch {
                                noteDao.insert(note)
                                activity
                                    ?.supportFragmentManager
                                    ?.beginTransaction()
                                    ?.replace(R.id.fragmentHolder, ListOfNotes())
                                    ?.commit()
                            }
                        }
                        //Если id != null, то тогда происхоит редактирование уже существующей заметки
                        else {
                            lifecycleScope.launch {
                                val currentNote = noteDao.getNoteById(id)
                                currentNote.noteHeader = _noteHeader
                                currentNote.noteContent = _noteContent
                                //currentNote.noteHeader = _noteHeader
                                noteDao.update(currentNote)
                                activity
                                    ?.supportFragmentManager
                                    ?.beginTransaction()
                                    ?.replace(R.id.fragmentHolder, ListOfNotes())
                                    ?.commit()
                            }
                        }
                    }
                    builder.setNegativeButton("Нет", null)
                    val dialog = builder.create()
                    dialog.show()
                }
            }
        }
    }

    companion object {
        fun newInstance() = NoteEditFragment()
    }
}