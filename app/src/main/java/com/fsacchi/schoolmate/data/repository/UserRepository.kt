package com.fsacchi.schoolmate.data.repository

import com.fsacchi.schoolmate.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUser(): Flow<UserEntity?>
    suspend fun save(userEntity: UserEntity)
    suspend fun clear()
}
