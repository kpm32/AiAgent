package com.example.aiagent.data

import com.squareup.moshi.JsonClass
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Streaming

interface LmStudioApi {
    @POST("v1/chat/completions")
    suspend fun chat(@Body body: ChatRequest): ChatResponse

    @Streaming
    @POST("v1/chat/completions")
    suspend fun chatStream(@Body body: ChatRequest): ResponseBody

}


@JsonClass(generateAdapter = true)
data class ChatRequest(
    val model: String,
    val messages: List<Message>,
    val temperature: Double? = 0.2,
    val max_tokens: Int? = 300,
    val stream: Boolean? = true
)


@JsonClass(generateAdapter = true)
data class Message(
    val role: String,
    val content: String
)

@JsonClass(generateAdapter = true)
data class ChatResponse(
    val choices: List<Choice>
)

@JsonClass(generateAdapter = true)
data class Choice(
    val message: Message
)

@JsonClass(generateAdapter = true)
data class StreamChunk(
    val choices: List<StreamChoice>?
)
@JsonClass(generateAdapter = true)
data class StreamChoice(
    val delta: Delta? = null,
    val message: Message? = null
)
@JsonClass(generateAdapter = true)
data class Delta(val content: String? = null)
@JsonClass(generateAdapter = true)
data class StreamMessage(val role: String? = null, val content: String? = null)