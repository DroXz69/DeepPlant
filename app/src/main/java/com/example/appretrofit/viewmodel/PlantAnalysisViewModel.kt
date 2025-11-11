package com.example.appretrofit.viewmodel

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.appretrofit.viewmodel.PlantAnalysisScreen
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import com.example.appretrofit.data.models.PlantAnalysisResult
import com.example.appretrofit.ui.PlantAnalysisViewModel
import com.example.appretrofit.utils.UriUtils

@Composable
fun PlantAnalysisScreen(viewModel: PlantAnalysisViewModel) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Launcher para seleccionar imagen
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            coroutineScope.launch {
                val bytes = UriUtils.readBytesFromUri(context, it)
                if (bytes != null) {
                    viewModel.analyzeImage(bytes)
                } else {
                    // manejar error
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Detector de Plantas Inacode", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(12.dp))

        if (state.isUploading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
            Text(state.uploadMessage)
        } else {
            Button(onClick = { launcher.launch("image/*") }) {
                Text("Seleccionar y Analizar Foto")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(state.uploadMessage)
        }

        state.error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text("ERROR: $it", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        state.analysisResult?.let { result ->
            Text("Resultado Reciente:", style = MaterialTheme.typography.titleMedium)
            Text("${result.name} (${result.scientificName})")
            Text("Confianza: ${"%.2f".format(result.confidence * 100)}%")
            Text("Descripción: ${result.description}")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Historial de Análisis (Firebase)", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (state.firebaseData.isEmpty()) {
            Text("No hay datos. Analiza una imagen para iniciar.")
        } else {
            LazyColumn {
                items(state.firebaseData) { item ->
                    AnalysisHistoryItem(item)
                }
            }
        }
    }
}

@Composable
fun AnalysisHistoryItem(item: PlantAnalysisResult) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(item.name, style = MaterialTheme.typography.titleMedium)
            Text(item.scientificName, style = MaterialTheme.typography.bodySmall)
            Text("Confianza: ${"%.0f".format(item.confidence * 100)}%")
        }
    }
}
