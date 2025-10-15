package com.example.aiagent.data.network

import com.example.aiagent.data.models.ChatRequestDto
import com.example.aiagent.data.models.ChatResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LmStudioApi {
    @POST("chat")
    suspend fun chat(@Body body: ChatRequestDto): ChatResponse

}

