package com.example.aiagent.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiagent.domain.models.AgentUiState
import com.example.aiagent.domain.use_case.AgentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AgentViewModel @Inject constructor(
    private val llmUseCase: AgentUseCase
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
                llmUseCase.stream(system, user).collect { delta ->
                    _state.update { st -> st.copy(output = st.output + delta) }
                }
                _state.value = _state.value.copy(loading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(loading = false, error = e.message ?: "Ошибка")
            }
        }
    }
}