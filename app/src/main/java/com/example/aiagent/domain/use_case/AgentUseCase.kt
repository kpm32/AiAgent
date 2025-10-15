package com.example.aiagent.domain.use_case

import com.example.aiagent.domain.lm_repository.LlmClientRepository

class AgentUseCase (private val repository: LlmClientRepository) {

    suspend fun getAnswer(request: String):  String {
        return repository.getAnswer(request)
    }

}