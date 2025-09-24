package com.example.aiagent.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiagent.domain.lm_repository.LlmClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class AgentUiState(
    val input: String = "",
    val output: String = "",
    val loading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AgentViewModel @Inject constructor(
    private val llm: LlmClientRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AgentUiState())
    val state = _state.asStateFlow()

    fun onInputChange(text: String) {
        _state.value = _state.value.copy(input = text)
    }

    fun run() {
        val term = _state.value.input.trim()
        if (term.isEmpty() || _state.value.loading) return

        _state.value = _state.value.copy(loading = true, error = null, output = "")

        viewModelScope.launch {
            try {
                val system = "Ты — лаконичный помощник. Объясняй понятным языком."
                val user   = "Объясни термин/понятие: $term"

                // СТРИМ: приходят дельты, просто дописываем в output
                llm.stream(system, user).collect { delta ->
                    _state.value = _state.value.copy(output = _state.value.output + delta)
                }
                _state.value = _state.value.copy(loading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(loading = false, error = e.message ?: "Ошибка")
            }
        }
    }
}