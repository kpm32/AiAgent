package com.example.aiagent.domain.lm_repository

interface LlmClientRepository {

    suspend fun getAnswer(request: String):  String
}