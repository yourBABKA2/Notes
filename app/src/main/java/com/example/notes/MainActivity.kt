package com.example.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.notes.databinding.ActivityMainBinding
import com.example.notes.databinding.FragmentRegistrationBinding
import com.example.notes.dbModel.NoteDatabase
import com.example.notes.fragments.ListOfNotes
import com.example.notes.fragments.NoteEditFragment
import com.example.notes.fragments.RegistrationFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.fragmentHolder, ListOfNotes.newInstance())
//            .commit()

        checkIfUserAndNotesIsExist()
    }

     private fun checkIfUserAndNotesIsExist() {
         val noteDatabase = NoteDatabase.getDatabase(this)
         val userDao = noteDatabase.userDao()
         val noteDao = noteDatabase.noteDao()

         lifecycleScope.launch {
             val userList = userDao.getAllUsers()
             val noteList = noteDao.getAllNotes()
             //Если пользователь существует и таблица заметок не пуста -> переход в список заметок
             if (userList.isNotEmpty() && noteList.isNotEmpty()) {
                 withContext(Dispatchers.Main){
                     supportFragmentManager
                         .beginTransaction()
                         .replace(R.id.fragmentHolder, ListOfNotes.newInstance())
                         .commit()
                 }
             }
             //Если пользователь существует и таблица заметок пуста -> переход в создание заметок
             if(userList.isNotEmpty() && noteList.isEmpty()) {
                 withContext(Dispatchers.Main) {
                     supportFragmentManager
                         .beginTransaction()
                         .replace(R.id.fragmentHolder, NoteEditFragment.newInstance())
                         .commit()
                 }
             }
             //Если пользователь не существует и таблица заметок пуста -> переход в создание пользователя
             if (userList.isEmpty() && noteList.isEmpty()){
                 withContext(Dispatchers.Main) {
                     supportFragmentManager
                         .beginTransaction()
                         .replace(R.id.fragmentHolder, RegistrationFragment.newInstance())
                         .commit()
                 }
             }
         }

    }
}