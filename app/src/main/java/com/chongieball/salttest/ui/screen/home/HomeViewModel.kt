package com.chongieball.salttest.ui.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chongieball.salttest.data.ProcessState
import com.chongieball.salttest.data.model.response.UserDetailResponse
import com.chongieball.salttest.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _userState = MutableLiveData<ProcessState<UserDetailResponse>>()
    val userState: LiveData<ProcessState<UserDetailResponse>> get() = _userState

    fun getUser(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _userState.postValue(ProcessState.loading())
                val getUser = userRepository.getUser(userId)

                _userState.postValue(ProcessState.success(getUser.data))
            } catch (e: Exception) {
                val error =
                    ProcessState.error<UserDetailResponse>(e.message ?: e.localizedMessage ?: "fail to get user") {
                        getUser(userId)
                    }
                _userState.postValue(error)
            }
        }
    }
}