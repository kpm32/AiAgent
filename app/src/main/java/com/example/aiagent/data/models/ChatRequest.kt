package com.example.aiagent.data.models

import com.squareup.moshi.Json

data class ChatRequestDto(
    @Json(name = "message") val message: String
)

data class ChatResponse(
    @Json(name = "reply") val reply: ApiAnswer,
    @Json(name = "tookMs") val tookMs: Long
)

// --- Ниже структуры ответа (точно как на сервере) ---

data class ApiAnswer(
    val status: String,
    val intent: String,
    val query: String,
    val message: String? = null,
    val items: List<MetaItem> = emptyList(),
    val structure: MetaStructure? = null,
    val toolCalls: List<ToolCallLog> = emptyList()
)

data class MetaItem(
    val type: String,
    val name: String,
    val title: String? = null
)

data class MetaStructure(
    val metaType: String,
    val objectName: String,
    val attributes: List<Field> = emptyList(),
    val tables: List<Table> = emptyList()
)

data class Field(
    val name: String,
    val type: String,
    val description: String? = null
)

data class Table(
    val name: String,
    val columns: List<Field>
)

data class ToolCallLog(
    val name: String,
    val args: Map<String, String>,
    val success: Boolean,
    val note: String? = null
)


