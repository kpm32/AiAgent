package com.example.aiagent.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// ---------- request / response ----------
@JsonClass(generateAdapter = true)
data class ChatRequestDto(
    @Json(name = "model") val model: String,
    @Json(name = "messages") val messages: List<Message>,
    @Json(name = "temperature") val temperature: Double? = 0.2,
    @Json(name = "max_tokens") val maxTokens: Int? = 300,
    @Json(name = "stream") val stream: Boolean? = true
)

@JsonClass(generateAdapter = true)
data class Message(
    @Json(name = "role") val role: String,
    @Json(name = "content") val content: String
)

@JsonClass(generateAdapter = true)
data class ChatResponse(
    @Json(name = "choices") val choices: List<Choice>
)

@JsonClass(generateAdapter = true)
data class Choice(
    @Json(name = "message") val message: Message
)

// ---------- streaming chunks ----------
@JsonClass(generateAdapter = true)
data class StreamChunk(
    @Json(name = "choices") val choices: List<StreamChoice>?
)

@JsonClass(generateAdapter = true)
data class StreamChoice(
    @Json(name = "delta") val delta: Delta? = null,
    @Json(name = "message") val message: StreamMessage? = null
)

@JsonClass(generateAdapter = true)
data class Delta(
    @Json(name = "content") val content: String? = null
)

@JsonClass(generateAdapter = true)
data class StreamMessage(
    @Json(name = "role") val role: String? = null,
    @Json(name = "content") val content: String? = null
)

