package com.example.notes.dbModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val userFullName: String
)