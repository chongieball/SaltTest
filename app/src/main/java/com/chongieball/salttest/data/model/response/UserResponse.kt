package com.chongieball.salttest.data.model.response

data class UserResponse(
    val data: UserDetailResponse
)

data class UserDetailResponse(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val avatar: String
) {
    val fullName = "$firstName $lastName"
}
