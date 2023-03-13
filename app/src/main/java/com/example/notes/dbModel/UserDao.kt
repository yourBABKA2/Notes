package com.example.notes.dbModel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<User>
}
