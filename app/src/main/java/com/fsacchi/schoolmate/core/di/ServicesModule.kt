package com.fsacchi.schoolmate.core.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

val servicesModule = module {

    single {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.setLanguageCode("pt-BR")
        firebaseAuth
    }

    single { FirebaseFirestore.getInstance() }
}
