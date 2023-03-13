package com.example.notes.dbModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val noteId: Int = 0,
    var noteHeader: String,
    var noteContent: String,
    val noteTimeCreation: String
)