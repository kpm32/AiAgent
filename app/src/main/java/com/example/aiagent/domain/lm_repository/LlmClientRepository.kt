package com.example.aiagent.domain.lm_repository

import kotlinx.coroutines.flow.Flow

interface LlmClientRepository {

    suspend fun generate(system: String, user: String): String

    fun stream(system: String, user: String): Flow<String>
}