package com.fsacchi.schoolmate.data.local.repository

import androidx.room.Transaction
import com.fsacchi.schoolmate.data.local.database.AppDatabase
import com.fsacchi.schoolmate.data.local.entity.UserEntity
import com.fsacchi.schoolmate.data.local.extensions.exec
import com.fsacchi.schoolmate.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class UserLocalRepository(db: AppDatabase) : UserRepository {

    private val userDao = db.userDao()

    override suspend fun getUser(): Flow<UserEntity?> = exec {
        userDao.findTopAsync()
    }

    @Transaction
    override suspend fun save(userEntity: UserEntity) {
        userDao.save(userEntity)
    }

    override suspend fun clear() {
        userDao.clear()
    }
}
