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

            "odata_query" -> {
                appendLine()
                val d = apiAnswer.data
                if (d == null || d.value.isEmpty()) {
                    appendLine("Нет данных.")
                } else {
                    appendLine("Источник: ${d.entitySet}")
                    val rows = d.value
                    // маленькое превью — Code / Description / Ref_Key, если есть
                    rows.take(10).forEachIndexed { i, row ->
                        val code = row["Code"] ?: row["Код"]
                        val desc = row["Description"] ?: row["Наименование"]
                        val ref = row["Ref_Key"] ?: row["Ссылка_Key"]
                        val head = listOfNotNull(code, desc).joinToString(" — ").ifBlank { ref?.toString() ?: "(строка ${i + 1})" }
                        appendLine("${i + 1}. $head")
                    }
                    d.count?.let { appendLine("Всего (по серверу): $it") }
                    d.nextLink?.let { appendLine("Есть следующая страница…") }
                }
            }

            else -> {
                appendLine()
                appendLine(apiAnswer.message ?: "Нет данных.")
            }
        }
    }


}