package com.example.aiagent.data

import com.example.aiagent.domain.lm_repository.LlmClientRepository
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.Buffer

class LmStudioLlmClient(
    private val api: LmStudioApi,
    private val model: String,
    private val moshi: Moshi
) : LlmClientRepository {

    private val chunkAdapter = moshi.adapter(StreamChunk::class.java)

    override suspend fun generate(system: String, user: String): String {
        var all = ""
        stream(system, user).collect { all += it }
        return all.trim()
    }

    override fun stream(system: String, user: String): Flow<String> =
        flow {
            val mdl = model.trim()
            require(mdl.isNotEmpty()) { "LM_STUDIO_MODEL пуст. Укажи id модели из /v1/models" }

            val req = ChatRequest(
                model = mdl,
                messages = listOf(
                    Message(role = "system", content = system.trim()),
                    Message(role = "user",   content = user.trim())
                ),
                temperature = 0.2,
                max_tokens = 600,    // можно поднять потолок
                stream = true
            )

            val rb = api.chatStream(req)
            rb.use { body ->
                val source = body.source()
                val carry = StringBuilder()
                val buf = Buffer()

                while (true) {
                    val read = source.read(buf, 8_192)
                    if (read == -1L) break
                    val chunk = buf.readUtf8()
                    carry.append(chunk)

                    // поддержим и \r\n, и \n
                    var idx = carry.indexOf("\n")
                    while (idx != -1) {
                        val line = carry.substring(0, idx).trim()
                        carry.delete(0, idx + 1)

                        if (line.startsWith("data:")) {
                            val payload = line.removePrefix("data:").trim()
                            if (payload == "[DONE]") return@use
                            // --- НАДЁЖНЫЙ ПАРСИНГ JSON ---
                            chunkAdapter.fromJson(payload)?.let { sc ->
                                val delta = sc.choices
                                    ?.firstOrNull()
                                    ?.let { it.delta?.content ?: it.message?.content }
                                if (!delta.isNullOrEmpty()) emit(delta)
                            }
                        }
                        idx = carry.indexOf("\n")
                    }
                }
            }
        }.flowOn(Dispatchers.IO)
}