package com.example.aiagent.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatRequestDto(
    @Json(name = "message") val message: String
)

@JsonClass(generateAdapter = true)
data class ChatResponse(
    @Json(name = "reply") val reply: ApiAnswer,
    @Json(name = "tookMs") val tookMs: Long
)

@JsonClass(generateAdapter = true)
data class ApiAnswer(
    @Json(name = "status") val status: String,
    @Json(name = "intent") val intent: String, // search_metadata | get_structure | odata_query | other
    @Json(name = "query") val query: String,
    @Json(name = "message") val message: String? = null,
    @Json(name = "items") val items: List<MetaItem> = emptyList(),
    @Json(name = "structure") val structure: MetaStructure? = null,
    @Json(name = "data") val data: ODataResult? = null,
    @Json(name = "toolCalls") val toolCalls: List<ToolCallLog> = emptyList(),
)

@JsonClass(generateAdapter = true)
data class MetaItem(
    @Json(name = "type") val type: String,
    @Json(name = "name") val name: String,
    @Json(name = "title") val title: String? = null
)

@JsonClass(generateAdapter = true)
data class MetaStructure(
    @Json(name = "metaType") val metaType: String,
    @Json(name = "objectName") val objectName: String,
    @Json(name = "attributes") val attributes: List<Field> = emptyList(),
    @Json(name = "tables") val tables: List<Table> = emptyList()
)

@JsonClass(generateAdapter = true)
data class Field(
    @Json(name = "name") val name: String,
    @Json(name = "type") val type: String,
    @Json(name = "description") val description: String? = null
)

@JsonClass(generateAdapter = true)
data class Table(
    @Json(name = "name") val name: String,
    @Json(name = "columns") val columns: List<Field>
)

@JsonClass(generateAdapter = true)
data class ToolCallLog(
    @Json(name = "name") val name: String,
    @Json(name = "args") val args: Map<String, String>,
    @Json(name = "success") val success: Boolean,
    @Json(name = "note") val note: String? = null
)

@JsonClass(generateAdapter = true)
data class ODataResult(
    @Json(name = "entitySet") val entitySet: String,
    // ключ: имя поля 1С (Ref_Key, Code, Description, ...), значение: любой тип
    @Json(name = "value") val value: List<Map<String, Any?>> = emptyList(),
    @Json(name = "count") val count: Int? = null,
    @Json(name = "nextLink") val nextLink: String? = null
)

