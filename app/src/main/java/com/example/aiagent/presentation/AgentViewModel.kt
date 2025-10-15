package com.example.aiagent.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiagent.domain.models.AgentUiState
import com.example.aiagent.domain.models.ChatMessageUi
import com.example.aiagent.domain.models.ChatRole
import com.example.aiagent.domain.use_case.AgentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AgentViewModel @Inject constructor(
    private val agentUseCase: AgentUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AgentUiState())
    val state = _state.asStateFlow()

    fun onInputChange(value: String) {
        _state.update { it.copy(input = value) }
    }

    fun clearError() = _state.update { it.copy(error = null) }

    fun run(useTypewriter: Boolean = false) {
        val prompt = state.value.input.trim()
        if (prompt.isEmpty()) return

        // 1) добавляем сообщение пользователя
        val userMsg = ChatMessageUi(role = ChatRole.User, text = prompt)
        // 2) сразу добавляем “печатает…” от агента
        val agentMsg = ChatMessageUi(role = ChatRole.Agent, text = "", isTyping = true)

        _state.update {
            it.copy(
                input = "", // очистим поле ввода
                loading = true,
                error = null,
                messages = it.messages + listOf(userMsg, agentMsg)
            )
        }

        viewModelScope.launch {
            try {
                val pretty: String = agentUseCase.getAnswer(prompt)
                if (useTypewriter) {
                    typewriterReplace(agentMsg.id, pretty)
                } else {
                    replaceAgentMessage(agentMsg.id, pretty)
                }
            } catch (t: Throwable) {
                replaceAgentMessage(
                    agentMsg.id,
                    "Ошибка: ${t.message ?: "неизвестная ошибка"}"
                )
                _state.update { it.copy(error = t.message) }
            } finally {
                _state.update { it.copy(loading = false) }
            }
        }
    }

    /** Плавная подмена текста у уже добавленного agent-сообщения. */
    private suspend fun typewriterReplace(agentMsgId: String, fullText: String, stepDelayMs: Long = 10L) {
        val sb = StringBuilder()
        for (ch in fullText) {
            sb.append(ch)
            _state.update { st ->
                st.copy(messages = st.messages.map {
                    if (it.id == agentMsgId) it.copy(text = sb.toString(), isTyping = true) else it
                })
            }
            kotlinx.coroutines.delay(stepDelayMs)
        }
        // финальный “сняли печатает…”
        _state.update { st ->
            st.copy(messages = st.messages.map {
                if (it.id == agentMsgId) it.copy(isTyping = false) else it
            })
        }
    }

    /** Мгновенная подмена текста у agent-сообщения. */
    private fun replaceAgentMessage(agentMsgId: String, text: String) {
        _state.update { st ->
            st.copy(
                messages = st.messages.map {
                    if (it.id == agentMsgId) it.copy(text = text, isTyping = false) else it
                }
            )
        }
    }

}