package com.example.aiagent.domain.models

data class AgentUiState(
    val input: String = "",
    val output: String = "",
    val loading: Boolean = false,
    val error: String? = null
)
