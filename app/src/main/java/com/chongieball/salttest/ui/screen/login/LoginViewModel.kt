package com.chongieball.salttest.ui.screen.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chongieball.salttest.data.ProcessState
import com.chongieball.salttest.data.repository.UserRepository
import com.chongieball.salttest.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _loginState = SingleLiveEvent<ProcessState<Int>>()
    val loginState: LiveData<ProcessState<Int>> get() = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loginState.postValue(ProcessState.loading())
                val login = userRepository.login(email, password)
                val userId = if (login.token.isNullOrEmpty()) login.id!!.toInt() else 2 //handle inconsistent response

                _loginState.postValue(ProcessState.success(userId))
            } catch (e: Exception) {
                val error =
                    ProcessState.error<Int>(e.message ?: e.localizedMessage ?: "fail to login") {
                        login(email, password)
                    }
                _loginState.postValue(error)
            }
        }
    }
}