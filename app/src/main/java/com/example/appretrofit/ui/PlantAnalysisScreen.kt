package com.example.appretrofit.ui

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.appretrofit.viewmodel.PlantAnalysisViewModel
import java.io.File
// Nuevos componentes
import com.example.appretrofit.ui.components.PrimaryButton
import com.example.appretrofit.ui.components.SecondaryButton
import com.example.appretrofit.ui.components.CardPlantResult
import com.example.appretrofit.ui.components.HistoryItemCard
import com.example.appretrofit.ui.components.LoadingOverlay
import com.example.appretrofit.ui.components.PhotoPlaceholder

@Composable
fun PlantAnalysisScreen(viewModel: PlantAnalysisViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = context as? Activity
    val resolver = context.contentResolver

    LaunchedEffect(Unit) { viewModel.initDevice(context) }

    LaunchedEffect(Unit) {
        activity?.window?.let { window ->
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val controller = WindowInsetsControllerCompat(window, window.decorView)
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            controller.hide(WindowInsetsCompat.Type.systemBars())
        }
    }

    var displayUri by remember { mutableStateOf<Uri?>(null) }
    val result by viewModel.result.collectAsState()
    val history by viewModel.history.collectAsState()
    val loading by viewModel.loading.collectAsState()

    val photoFile = remember { File(context.cacheDir, "photo.jpg") }
    val photoUri = remember { FileProvider.getUriForFile(context, "${context.packageName}.provider", photoFile) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
        if (ok) {
            displayUri = photoUri.buildUpon().appendQueryParameter("time", System.currentTimeMillis().toString()).build()
            viewModel.analyze(photoUri, resolver, context)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                PrimaryButton(text = "Tomar foto y analizar", onClick = { launcher.launch(photoUri) })
            }

            if (displayUri == null) {
                item { PhotoPlaceholder() }
            }

            displayUri?.let { uri ->
                item {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(6.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Foto reciente",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            result?.let { r ->
                item {
                    CardPlantResult(
                        estado = r.estado,
                        etiquetas = r.etiquetas.map { e -> "${e.descripcion} (${(e.confianza * 100).toInt()}%)" }
                    )
                }
            }

            item {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Historial de análisis", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { viewModel.loadHistory() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refrescar historial", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            items(history) { item ->
                HistoryItemCard(
                    name = item.name,
                    description = item.description,
                    confidence = (item.confidence * 100).toInt(),
                    imagePath = item.imageUrl
                )
            }

            item {
                SecondaryButton(text = "Cerrar aplicación", onClick = { activity?.finish() })
            }
        }

        if (loading) {
            LoadingOverlay(text = "Analizando imagen…")
        }
    }
}