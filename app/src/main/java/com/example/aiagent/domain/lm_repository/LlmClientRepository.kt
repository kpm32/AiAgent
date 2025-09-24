package com.example.aiagent.domain.lm_repository

import kotlinx.coroutines.flow.Flow

interface LlmClientRepository {
    /** вернёт готовую строку (оставим для совместимости). */
    suspend fun generate(system: String, user: String): String

    /** поток частичных кусков ответа (добавка к уже полученному). */
    fun stream(system: String, user: String): Flow<String>
}