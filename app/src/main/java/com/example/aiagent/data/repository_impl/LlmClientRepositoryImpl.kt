package com.example.aiagent.data.repository_impl


import com.example.aiagent.data.mapper.LLMMapper
import com.example.aiagent.data.network.LmStudioApi
import com.example.aiagent.domain.lm_repository.LlmClientRepository

class LlmClientRepositoryImpl(
    private val lmStudioApi: LmStudioApi,
    private val mapper: LLMMapper
) : LlmClientRepository {


    override suspend fun getAnswer(request: String): String {
        val request = mapper.mapEntityToDto(request)
        val response = lmStudioApi.chat(request)
        val answerText = mapper.prettyText(response.reply)
        return answerText
    }


}