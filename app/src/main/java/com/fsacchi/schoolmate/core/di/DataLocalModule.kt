package com.fsacchi.schoolmate.core.di

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKey.KeyScheme.AES256_GCM
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataLocalModule = module {

    single {
        val context = androidApplication()
        val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "default",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        sharedPreferences
    }
}
