AiAgent

Локальный AI-агент на Jetpack Compose + Hilt с провайдером LM Studio (OpenAI-совместимый сервер).
Поддерживает стриминг ответа (текст «печатается» в реальном времени), автопрокрутку и минимальную чистую архитектуру.

Возможности

📟 Работа с локальной LLM через LM Studio (/v1/chat/completions)

⏱️ Реальный стриминг (SSE) без таймаутов и «подвисаний»

🧱 Слои: domain / data / di / presentation

🧩 DI на Hilt, сеть — Retrofit + OkHttp + Moshi

🧭 Простой экран: ввод термина → объяснение понятным языком

Стек

Kotlin, Jetpack Compose, Hilt, Retrofit2, OkHttp3, Moshi, Coroutines/Flow.

Структура проекта
app/
 ├─ domain/
 │   └─ repository/LlmClientRepository.kt         // интерфейс клиента LLM
 ├─ ai/data/lmstudio/
 │   ├─ LmStudioApi.kt                            // Retrofit API (/v1/chat/completions)
 │   ├─ LmStudioLlmClient.kt                      // реализация клиента + стриминг SSE
 │   └─ (DTO для запросов/стрима)
 ├─ di/
 │   └─ AiModule.kt                               // Moshi/OkHttp/Retrofit/Hilt биндинги
 ├─ presentation/
 │   ├─ AgentViewModel.kt                         // VM, сборка потока в текст
 │   └─ AgentScreen.kt                            // Compose-экран, автоскролл, IME
 └─ app/src/debug/res/xml/network_security_config.xml

Быстрый старт
1) Установи и запусти LM Studio

Скачай LM Studio: https://lmstudio.ai/
curl http://localhost:8800/v1/models
Возьми значение id активной модели
Для быстрой работы лучше взять компактную instruct-модель, например:
qwen2.5:3b-instruct или llama-3.2-3b-instruct

2) Настрой local.properties

В корне проекта (рядом с settings.gradle):
LM_STUDIO_BASE_URL=http://10.0.2.2:8800/
LM_STUDIO_MODEL=<ID_модели_из_/v1/models>

Для эмулятора Android всегда используй 10.0.2.2.

Для реального устройства укажи IP твоего ПК в локальной сети, например:
LM_STUDIO_BASE_URL=http://192.168.1.10:8800/

Важные детали конфигурации
BuildConfig-поля

В app/build.gradle.kts включены и заполняются из local.properties:
buildFeatures { compose = true; buildConfig = true }
defaultConfig {
    buildConfigField("String","LM_STUDIO_BASE_URL","\"${…}\"")
    buildConfigField("String","LM_STUDIO_MODEL","\"${…}\"")
}

Network (debug)

Для HTTP на локалхосте в debug-сборке включён cleartext:
<!-- app/src/debug/AndroidManifest.xml -->
<application
    android:usesCleartextTraffic="true"
    android:networkSecurityConfig="@xml/network_security_config"/>

<!-- app/src/debug/res/xml/network_security_config.xml -->
<network-security-config>
  <base-config cleartextTrafficPermitted="true" />
</network-security-config>

Таймауты и стриминг

OkHttp настроен без общего callTimeout и с большими read/write-таймаутами.
Используем stream=true и читаем SSE-чанки — ответ приходит сразу, без долгого ожидания.

# список моделей
curl http://localhost:8800/v1/models

# пример запроса как у приложения (stream=true)
curl http://localhost:8800/v1/chat/completions \
  -H "Content-Type: application/json" \
  -d '{
    "model": "<ID_модели>",
    "messages": [
      { "role": "system", "content": "Ты — лаконичный помощник. Объясняй понятным языком." },
      { "role": "user",   "content": "Объясни термин/понятие: Умма" }
    ],
    "temperature": 0.2,
    "max_tokens": 300,
    "stream": true
  }'
