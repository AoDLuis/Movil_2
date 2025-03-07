package com.example.broadcastreceiver_telefona.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.broadcastreceiver_telefona.Data.AutoReplyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AutoReplyViewModel @Inject constructor(private val repository: AutoReplyRepository) : ViewModel() {
    private val _number = MutableStateFlow(repository.getSavedNumber())
    val number = _number.asStateFlow()

    private val _message = MutableStateFlow(repository.getSavedMessage())
    val message = _message.asStateFlow()

    fun saveConfig(number: String, message: String) {
        viewModelScope.launch {
            repository.saveConfig(number, message)
            _number.value = number
            _message.value = message
        }
    }
}
