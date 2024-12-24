package com.fsacchi.schoolmate.core.di

import com.fsacchi.schoolmate.domain.discipline.DeleteDisciplineUseCase
import com.fsacchi.schoolmate.domain.discipline.GetDisciplinesUseCase
import com.fsacchi.schoolmate.domain.discipline.SaveDisciplineUseCase
import com.fsacchi.schoolmate.domain.discipline.UpdateDisciplineUseCase
import com.fsacchi.schoolmate.domain.file.DeleteFileUseCase
import com.fsacchi.schoolmate.domain.file.GetFileUseCase
import com.fsacchi.schoolmate.domain.file.SaveFileUseCase
import com.fsacchi.schoolmate.domain.file.UpdateFileUseCase
import com.fsacchi.schoolmate.domain.home.GetUserUseCase
import com.fsacchi.schoolmate.domain.home.LogoffUseCase
import com.fsacchi.schoolmate.domain.job.DeleteJobUseCase
import com.fsacchi.schoolmate.domain.job.GetJobHomeUseCase
import com.fsacchi.schoolmate.domain.job.GetJobUseCase
import com.fsacchi.schoolmate.domain.job.GetJobsCalendarUseCase
import com.fsacchi.schoolmate.domain.job.SaveJobUseCase
import com.fsacchi.schoolmate.domain.job.UpdateJobUseCase
import com.fsacchi.schoolmate.domain.login.ForgotPasswordUseCase
import com.fsacchi.schoolmate.domain.login.LoginUseCase
import com.fsacchi.schoolmate.domain.login.RegisterUserUseCase
import org.koin.dsl.module

private val loginUseCases = module {
    factory { RegisterUserUseCase(get()) }
    factory { ForgotPasswordUseCase(get()) }
    factory { LoginUseCase(get(), get(), get()) }
    factory { LogoffUseCase(get()) }
    factory { GetUserUseCase(get()) }
}

private val disciplineUseCases = module {
    factory { SaveDisciplineUseCase(get()) }
    factory { DeleteDisciplineUseCase(get()) }
    factory { UpdateDisciplineUseCase(get()) }
    factory { GetDisciplinesUseCase(get()) }
}

private val jobUseCases = module {
    factory { SaveJobUseCase(get()) }
    factory { GetJobUseCase(get(), get()) }
    factory { GetJobHomeUseCase(get(), get()) }
    factory { GetJobsCalendarUseCase(get()) }
    factory { DeleteJobUseCase(get()) }
    factory { UpdateJobUseCase(get()) }
}

private val fileUseCases = module {
    factory { SaveFileUseCase(get()) }
    factory { GetFileUseCase(get(), get()) }
    factory { DeleteFileUseCase(get()) }
    factory { UpdateFileUseCase(get()) }
}

internal val useCaseModules = listOf(
    loginUseCases,
    disciplineUseCases,
    jobUseCases,
    fileUseCases
)
