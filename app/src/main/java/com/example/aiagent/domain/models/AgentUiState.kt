package com.example.aiagent.domain.models

import java.util.UUID

enum class ChatRole { User, Agent }

data class ChatMessageUi(
    val id: String = UUID.randomUUID().toString(),
    val role: ChatRole,
    val text: String,
    val isTyping: Boolean = false
)

data class AgentUiState(
    val input: String = "",
    val messages: List<ChatMessageUi> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)
