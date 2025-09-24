package com.example.aiagent.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel


@Composable
fun AgentScreen1() {

    val viewModel: AgentViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()


    val scrollState = rememberScrollState()

    // Автопрокрутка вниз при каждом обновлении вывода
    LaunchedEffect(state.output) {
        // маленькая задержка, чтобы layout успел посчитать высоту
        snapshotFlow { state.output }.collect {
            // анимировано или мгновенно — на вкус
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Describe Agent (LM Studio)", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = state.input,
            onValueChange = { viewModel.onInputChange(it) },
            label = { Text("Термин/понятие") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = { viewModel.run() },
            enabled = !state.loading,
            modifier = Modifier
                .wrapContentWidth()
        ) { Text(if (state.loading) "Печатаю…" else "Объяснить") }

        if (state.error != null) {
            Text(state.error!!, color = MaterialTheme.colorScheme.error)
        }

        // Область ответа с прокруткой
        Box(
            modifier = Modifier
                .weight(1f) // занимает оставшееся место
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(top = 8.dp)
        ) {
            Text(
                text = state.output.ifBlank { if (state.loading) "…" else "" },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun AgentScreen() {

    val viewModel: AgentViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()


    val outputScroll = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    // автопрокрутка вниз, когда приходит новая порция текста
    LaunchedEffect(state.output) {
        outputScroll.animateScrollTo(outputScroll.maxValue)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Describe Agent (LM Studio)", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = state.input,
            onValueChange = { viewModel.onInputChange(it) },
            label = { Text("Термин/понятие") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboard?.hide()
                    focusManager.clearFocus(force = true)
                    viewModel.run()
                },
                onDone = {
                    keyboard?.hide()
                    focusManager.clearFocus(force = true)
                    viewModel.run()
                }
            )
        )

        Button(
            onClick = {
                keyboard?.hide()
                focusManager.clearFocus(force = true)
                viewModel.run()
            },
            enabled = !state.loading,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text(if (state.loading) "Печатаю…" else "Объяснить")
        }

        if (state.error != null) {
            Text(state.error!!, color = MaterialTheme.colorScheme.error)
        }

        // ПРОКРУТКА ДЛИННОГО ТЕКСТА
        Box(
            modifier = Modifier
                .weight(1f)                   // занимает всё оставшееся место
                .fillMaxWidth()
                .verticalScroll(outputScroll) // даёт скролл, если текст длинный
                .padding(top = 8.dp)
        ) {
            SelectionContainer {              // можно выделять/копировать текст
                Text(
                    text = state.output.ifBlank { if (state.loading) "…" else "" },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}