package com.chongieball.salttest.di
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import org.koin.dsl.module

private const val BASE_URL = "reqres.in/api"

val networkModule = module {

    fun provideClient() = HttpClient(Android.create()) {
        expectSuccess = true

        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = BASE_URL

                headers {
                    append(HttpHeaders.Accept, "application/vnd.api+json")
                    append(HttpHeaders.ContentType, "application/vnd.api+json")
                }
            }
        }

        install(HttpTimeout) {
            this.requestTimeoutMillis = 60000
            this.connectTimeoutMillis = 60000
            this.socketTimeoutMillis = 60000
        }

        install(ContentNegotiation) {
            gson()
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY
        }
    }

    single { provideClient() }

}