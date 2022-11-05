package com.chongieball.salttest.data.repository

import com.chongieball.salttest.data.Endpoint
import com.chongieball.salttest.data.model.request.LoginRequest
import com.chongieball.salttest.data.model.response.LoginResponse
import com.chongieball.salttest.data.model.response.UserResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class UserRepository(private val httpClient: HttpClient) {

    suspend fun login(email: String, password: String): LoginResponse =
        httpClient.post(Endpoint.LOGIN) {
            setBody( LoginRequest(email, password))
        }.body()

    suspend fun getUser(id: Int): UserResponse =
        httpClient.get("${Endpoint.GET_USER}/$id").body()
}