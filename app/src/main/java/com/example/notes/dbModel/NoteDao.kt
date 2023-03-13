package com.example.notes.dbModel

import androidx.room.*

@Dao
interface NoteDao {

    //Внесение данных
    @Insert
    suspend fun insert(note: Note)

    //Обновление данных
    @Update
    suspend fun update(note: Note)

    //Удаление данных
    @Query("DELETE FROM Notes WHERE noteId = :noteId")
    suspend fun delete(noteId: Int) : Int

    //Выборка всех данных
    @Query("SELECT * FROM Notes")
    suspend fun getAllNotes(): List<Note>

    //Выборка конкретной заметки
    @Query("SELECT * FROM Notes WHERE noteId = :noteId")
    suspend fun getNoteById(noteId: Int): Note
}
