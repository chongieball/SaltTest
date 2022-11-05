package com.chongieball.salttest.data

sealed class ProcessState<T> {
    companion object {
        fun <T> initial() = Initial<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> loading() = Loading<T>()
        fun <T> error(message: String, retryFunction: (() -> Unit)? = null) =
            ErrorState<T>(message, retryFunction)

        fun <T> empty(message: String) = EmptyData<T>(message)
    }

    class Initial<T>() : ProcessState<T>()
    class Success<T>(val data: T) : ProcessState<T>()
    class Loading<T> : ProcessState<T>()
    class EmptyData<T>(val message: String) : ProcessState<T>()
    class ErrorState<T>(
        val message: String,
        val retryFunction: (() -> Unit)? = null
    ) : ProcessState<T>()

    fun isSuccess() = this is Success
    fun isLoading() = this is Loading
    fun isError() = this is ErrorState
    fun isEmpty() = this is EmptyData
}