package com.example.aiagent.data.mapper

import com.example.aiagent.data.models.ApiAnswer
import com.example.aiagent.data.models.ChatRequestDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LLMMapper @Inject constructor(){


    fun mapEntityToDto(text: String): ChatRequestDto =
        ChatRequestDto(message = text)

    /** Форматирует строгий JSON-ответ сервера в читабельный текст для UI. */
    fun prettyText(apiAnswer: ApiAnswer): String = buildString {
        appendLine("Запрос: ${apiAnswer.query}")

        when (apiAnswer.intent) {
            "search_metadata" -> {
                appendLine()

                val items = apiAnswer.items
                if (items.isEmpty()) {
                    appendLine("— ничего не найдено")
                } else {
                    items.forEachIndexed { i, it ->
                        val title = it.title ?: it.name
                        appendLine("${i + 1}. $title ")
                    }
                }
            }

            "get_structure" -> {
                appendLine()
                val s = apiAnswer.structure
                if (s == null) {
                    appendLine("Структура не получена.")
                } else {
                    appendLine("Объект: ${s.objectName} (${s.metaType})")

                    if (s.attributes.isNotEmpty()) {
                        appendLine()
                        appendLine("Реквизиты:")
                        s.attributes.forEach { f ->
                            val d = f.description?.let { " — $it" } ?: ""
                            appendLine("• ${f.name}: ${f.type}$d")
                        }
                    }

                    if (s.tables.isNotEmpty()) {
                        appendLine()
                        appendLine("Табличные части:")
                        s.tables.forEach { t ->
                            appendLine("• ${t.name}")
                            t.columns.forEach { c ->
                                val d = c.description?.let { " — $it" } ?: ""
                                appendLine("   · ${c.name}: ${c.type}$d")
                            }
                        }
                    }
                }
            }

            else -> {
                appendLine()
                appendLine(apiAnswer.message ?: "Нет данных.")
            }
        }
    }


}