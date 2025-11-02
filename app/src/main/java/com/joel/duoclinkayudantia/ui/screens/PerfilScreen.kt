package com.joel.duoclinkayudantia.ui.screens

import android.Manifest
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.joel.duoclinkayudantia.ui.PerfilViewModel
import com.joel.duoclinkayudantia.ui.theme.DuocBlue
import com.joel.duoclinkayudantia.ui.theme.DuocYellow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(vm: PerfilViewModel = viewModel()) {
    val nombre by vm.nombre.collectAsStateWithLifecycle()
    val apellido by vm.apellido.collectAsStateWithLifecycle()
    val fotoUri by vm.fotoUri.collectAsStateWithLifecycle()

    var capturedBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var cameraUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    /* ---------- Launchers ---------- */

    // Cámara: guarda la foto en MediaStore y devuelve éxito/fracaso
    val takePicture = rememberLauncherForActivityResult(TakePicture()) { success ->
        if (success && cameraUri != null) {
            capturedBitmap = null
            vm.setFotoUri(cameraUri)  // persistente
        } else {
            // si se canceló o falló: elimina el registro vacío
            cameraUri?.let { context.contentResolver.delete(it, null, null) }
            cameraUri = null
        }
    }

    // Galería
    val pickFromGallery = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            capturedBitmap = null
            vm.setFotoUri(uri)
        }
    }

    /* ---------- Permisos ---------- */
    val permissionsToRequest = remember {
        buildList {
            add(Manifest.permission.CAMERA)
            if (Build.VERSION.SDK_INT >= 33)
                add(Manifest.permission.READ_MEDIA_IMAGES)
            else
                add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }.toTypedArray()
    }
    val requestPermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { /* no-op */ }

    /* ---------- Helpers ---------- */
    fun createImageUri(context: android.content.Context): Uri? {
        val values = ContentValues().apply {
            put(
                MediaStore.Images.Media.DISPLAY_NAME,
                "perfil_${System.currentTimeMillis()}.jpg"
            )
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            // API 29+ (scoped storage)
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/DuocLinkAyudantia")
        }
        return context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
    }

    fun onTakePhoto() {
        requestPermissions.launch(permissionsToRequest)
        cameraUri = createImageUri(context)
        cameraUri?.let { takePicture.launch(it) }
    }

    fun onPickFromGallery() {
        requestPermissions.launch(permissionsToRequest)
        pickFromGallery.launch("image/*")
    }

    /* ---------- UI ---------- */
    val scroll = rememberScrollState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Perfil") },
                colors = centerAlignedTopAppBarColors(
                    containerColor = DuocBlue,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(scroll),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nombre + Apellido arriba del avatar
            val displayName = listOf(nombre, apellido)
                .filter { it.isNotBlank() }
                .joinToString(" ")
                .ifBlank { " " }

            Text(text = displayName, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(12.dp))

            // Avatar
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                when {
                    capturedBitmap != null -> Image(
                        bitmap = capturedBitmap!!.asImageBitmap(),
                        contentDescription = "Foto de perfil (cámara)",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    fotoUri != null -> AsyncImage(
                        model = fotoUri,
                        contentDescription = "Foto de perfil (galería/cámara)",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    else -> Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Sin foto",
                        modifier = Modifier.size(56.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = ::onTakePhoto,
                    modifier = Modifier.weight(1f)
                ) { Text("Tomar foto") }

                OutlinedButton(
                    onClick = ::onPickFromGallery,
                    modifier = Modifier.weight(1f)
                ) { Text("Elegir de galería") }
            }

            if (fotoUri != null) {
                Spacer(Modifier.height(8.dp))
                TextButton(
                    onClick = { vm.setFotoUri(null) },
                    modifier = Modifier.align(Alignment.End)
                ) { Text("Eliminar foto", color = MaterialTheme.colorScheme.error) }
            }

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = vm::setNombre,
                singleLine = true,
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = apellido,
                onValueChange = vm::setApellido,
                singleLine = true,
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    vm.saveProfile()
                    scope.launch { snackbarHostState.showSnackbar("Perfil guardado") }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = buttonColors(containerColor = DuocYellow)
            ) { Text("Guardar cambios") }
        }
    }
}