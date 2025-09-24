package com.example.aiagent.data.network

import com.example.aiagent.data.models.ChatRequestDto
import com.example.aiagent.data.models.ChatResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Streaming

interface LmStudioApi {
    @POST("v1/chat/completions")
    suspend fun chat(@Body body: ChatRequestDto): ChatResponse

    @Streaming
    @POST("v1/chat/completions")
    suspend fun chatStream(@Body body: ChatRequestDto): ResponseBody

}