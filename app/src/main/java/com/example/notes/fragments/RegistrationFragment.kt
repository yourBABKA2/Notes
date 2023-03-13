package com.example.notes.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.notes.R
import com.example.notes.databinding.FragmentRegistrationBinding
import com.example.notes.dbModel.NoteDatabase
import com.example.notes.dbModel.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private val dataList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Добавление слушателя нажатий на кнопку
        binding.btnEnter.setOnClickListener{
            //Запись пользователя
            insertUserToDatabase()
            //Переключение на другой фрагмент
            navigateToNoteEditFragment()
        }

        binding.editTextName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Вызывается после изменения текста
                s?.toString()?.let {
                    dataList.add(it) // Добавляем новый элемент в динамический массив
                    updateTextName()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    //Добавляем первый символ в textView
    fun updateTextName() {
        if (dataList.isNotEmpty()) {
            // Обновляем текст в TextView, отображая первый элемент в массиве
            binding.textName.text = dataList.first()
        }
        if (binding.editTextName.text.isEmpty()) {
            binding.textName.text = ""
            dataList.clear()
        }
    }

    //Запись пользователя
    private fun insertUserToDatabase() {
        val userFullName = binding.editTextName.text.toString()
        val user = User(
            userFullName = userFullName
        )
        val userDao = NoteDatabase.getDatabase(requireContext()).userDao()
        GlobalScope.launch(Dispatchers.IO) {
            userDao.insert(user)
        }
        binding.editTextName.setText("")
        Toast.makeText(requireContext(), "Пользователь $userFullName успешно создан", Toast.LENGTH_SHORT).show()
    }

    //Переключение на другой фрагмент
    private fun navigateToNoteEditFragment(){

        activity
            ?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.fragmentHolder, NoteEditFragment())
            ?.commit()
//        val noteEditFragment = NoteEditFragment()
//        val transaction = requireFragmentManager().beginTransaction()
//        transaction.replace(R.id.fragmentHolder, noteEditFragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() = RegistrationFragment()
    }
}