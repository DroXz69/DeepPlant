package com.example.appretrofit.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.appretrofit.viewmodel.PlantAnalysisViewModel
import java.io.File

@Composable
fun PlantAnalysisScreen(viewModel: PlantAnalysisViewModel = viewModel()) {

    val context = LocalContext.current
    val resolver = context.contentResolver
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val result by viewModel.result.collectAsState()

    val photoFile = File(context.cacheDir, "photo.jpg")
    val photoUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        photoFile
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { ok ->
        if (ok) {
            imageUri = photoUri
            viewModel.analyze(photoUri, resolver)
        }
    }

    Column(Modifier.padding(16.dp)) {

        Button(onClick = { launcher.launch(photoUri) }) {
            Text("Tomar foto y analizar")
        }

        Spacer(Modifier.height(20.dp))

        imageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )
        }

        result?.let {
            Spacer(Modifier.height(20.dp))
            Text("Estado: ${it.estado}", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            it.etiquetas.forEach { e ->
                Text("• ${e.descripcion} (${(e.confianza * 100).toInt()}%)")
            }
        }
    }
}
