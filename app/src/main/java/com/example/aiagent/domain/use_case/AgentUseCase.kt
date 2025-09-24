package com.example.aiagent.domain.use_case

import com.example.aiagent.domain.lm_repository.LlmClientRepository
import kotlinx.coroutines.flow.Flow

class AgentUseCase (private val repository: LlmClientRepository) {

    /** вернёт готовую строку (оставим для совместимости). */
    suspend fun generate(system: String, user: String): String {
        return repository.generate(system, user)
    }

    /** поток частичных кусков ответа (добавка к уже полученному). */
    fun stream(system: String, user: String): Flow<String> {
        return repository.stream(system, user)
    }
}