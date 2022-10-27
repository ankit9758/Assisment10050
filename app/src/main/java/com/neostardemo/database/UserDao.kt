package com.neostardemo.database

import androidx.room.*
import com.neostardemo.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: User)

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>
}