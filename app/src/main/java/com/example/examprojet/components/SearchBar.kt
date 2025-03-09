package com.example.examprojet.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    var searchJob by remember { mutableStateOf<Job?>(null) }
    val coroutineScope = rememberCoroutineScope()

    TextField(
        value = query,
        onValueChange = {
            query = it
            searchJob?.cancel()
            searchJob = coroutineScope.launch {
                //delay(2000) //potentiel debouncer (pour permettre des optimisations de performance)
                onSearch(query)
            }
        },
        label = { Text("Search") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    SearchBar { query -> println("Searching for: $query") }
}