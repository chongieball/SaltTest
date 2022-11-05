package com.chongieball.salttest.di

import com.chongieball.salttest.data.repository.UserRepository
import io.ktor.client.*
import org.koin.dsl.module

val repositoryModule = module {

    fun provideUserRepository(httpClient: HttpClient): UserRepository = UserRepository(httpClient)

    single { provideUserRepository(get()) }
}