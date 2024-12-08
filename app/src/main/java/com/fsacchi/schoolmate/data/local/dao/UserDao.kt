package com.fsacchi.schoolmate.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fsacchi.schoolmate.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(user: UserEntity): Long

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("SELECT * FROM user LIMIT 1")
    fun findTop(): Flow<UserEntity?>

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun findTopAsync(): UserEntity?

    @Query("DELETE FROM user")
    suspend fun clear()
}
