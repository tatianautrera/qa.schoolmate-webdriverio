package com.fsacchi.schoolmate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val uid: String,
    val email: String,
    val name: String,
    val password: String
)
