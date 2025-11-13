package com.example.appretrofit.ui

import android.app.Activity
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.appretrofit.viewmodel.PlantAnalysisViewModel
import java.io.File

@Composable
fun PlantAnalysisScreen(viewModel: PlantAnalysisViewModel = viewModel()) {

    val context = LocalContext.current
    val activity = context as? Activity
    val resolver = context.contentResolver

    // Inicializar el deviceId dentro del ViewModel
    LaunchedEffect(Unit) {
        viewModel.initDevice(context)
    }

    // Ocultar barra de notificaciones y navegación (modo inmersivo)
    val view = LocalView.current
    LaunchedEffect(Unit) {
        activity?.window?.let { window ->
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
        view.systemUiVisibility = (
                android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
                        or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }

    // Estados de UI
    var displayUri by remember { mutableStateOf<Uri?>(null) }
    val result by viewModel.result.collectAsState()
    val history by viewModel.history.collectAsState()
    val loading by viewModel.loading.collectAsState()

    // Archivo temporal donde se guarda la foto tomada
    val photoFile = remember { File(context.cacheDir, "photo.jpg") }
    val photoUri = remember {
        FileProvider.getUriForFile(context, "${context.packageName}.provider", photoFile)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { ok ->
        if (ok) {
            displayUri = photoUri.buildUpon()
                .appendQueryParameter("time", System.currentTimeMillis().toString())
                .build()

            viewModel.analyze(photoUri, resolver, context)
        }
    }

    // CONTENEDOR PRINCIPAL
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) Color(0xFF1C1C1E) else Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {

            // BOTÓN: TOMAR FOTO
            item {
                Button(
                    onClick = { launcher.launch(photoUri) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text("Tomar foto y analizar", fontWeight = FontWeight.Bold)
                }
            }

            // FOTO ACTUAL
            displayUri?.let {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(it),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // RESULTADO ACTUAL
            result?.let { r ->
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Estado actual:", fontWeight = FontWeight.Bold)
                            Text(
                                r.estado,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(Modifier.height(12.dp))

                            r.etiquetas.forEach { e ->
                                Text("• ${e.descripcion} (${(e.confianza * 100).toInt()}%)")
                            }
                        }
                    }
                }
            }

            // ENCABEZADO HISTORIAL + REFRESH
            item {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Historial de análisis",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(onClick = { viewModel.loadHistory() }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Refrescar historial",
                            tint = Color.White
                        )
                    }
                }
            }

            // LISTA DEL HISTORIAL CON MINIATURA
            items(history) { item ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .background(Color(0xFF2C2C2E))
                            .padding(16.dp)
                    ) {

                        // MINIATURA
                        item.imageUrl?.let { path ->
                            Image(
                                painter = rememberAsyncImagePainter(File(path)),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(Color.DarkGray, RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                        }

                        // INFO
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.name, color = Color.White, fontWeight = FontWeight.Bold)
                            Text(item.description, color = Color.LightGray)
                            Text(
                                "Confianza: ${(item.confidence * 100).toInt()}%",
                                color = Color(0xFFA5A5A5)
                            )
                        }
                    }
                }
            }

            // BOTÓN: CERRAR APP
            item {
                OutlinedButton(
                    onClick = { activity?.finish() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    border = ButtonDefaults.outlinedButtonBorder
                ) {
                    Text("Cerrar aplicación", fontWeight = FontWeight.Bold)
                }
            }
        }

        // LOADING (overlay)
        if (loading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color(0x88000000)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Color.White)
                    Spacer(Modifier.height(12.dp))
                    Text("Analizando imagen…", color = Color.White)
                }
            }
        }
    }
}