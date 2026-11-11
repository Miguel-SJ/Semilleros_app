package com.example.semillerosapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semillerosapp.ui.components.AppHeader
import com.example.semillerosapp.ui.components.InfoBannerCard
import com.example.semillerosapp.ui.components.OutlinedActionButton
import com.example.semillerosapp.ui.theme.*
import com.example.semillerosapp.ui.viewmodel.MainViewModel

@Composable
fun SemilleroDetailScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    onCreateClick: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val semillero by viewModel.selectedSemillero.collectAsState()
    val docentes by viewModel.docentes.collectAsState()

    val userRol = currentUser?.rol?.lowercase() ?: "estudiante"
    val canManageSemillero = userRol == "coordinador" || userRol == "administrador" || userRol == "admin"

    var showLiderDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        AppHeader(
            title = "Detalle del semillero",
            subtitle = "${currentUser?.rol?.replaceFirstChar { it.uppercase() } ?: "Usuario"} · ${currentUser?.nombre ?: ""}",
            onBackClick = onBackClick
        )

        val item = semillero ?: return@Column

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Active badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(ActiveBadgeBg)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Semillero ${item.estado}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = ActiveBadgeText
                )
            }

            // Title
            Text(
                text = item.nombre,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            // Subtitle
            Column {
                Text(
                    text = item.facultad,
                    fontSize = 15.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Área estudiada: ${item.area_estudiada}",
                    fontSize = 15.sp,
                    color = TextSecondary
                )
            }

            // Card 1: Líneas de investigación
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = ButtonDefaults.outlinedShape.let { androidx.compose.foundation.BorderStroke(1.dp, BorderColor) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Líneas de investigación",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    if (item.lineas_investigacion.isEmpty()) {
                        Text(text = "No hay líneas registradas", fontSize = 14.sp, color = TextSecondary)
                    } else {
                        item.lineas_investigacion.forEach { linea ->
                            Text(
                                text = linea,
                                fontSize = 14.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // Card 2: Docente líder
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = ButtonDefaults.outlinedShape.let { androidx.compose.foundation.BorderStroke(1.dp, BorderColor) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Docente líder",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.docente_lider_nombre ?: "Sin asignar",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.facultad,
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = item.docente_lider_correo ?: "",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )

                    if (canManageSemillero) {
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedButton(
                            onClick = { showLiderDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryBlue)
                        ) {
                            Text("Cambiar docente líder", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            if (canManageSemillero) {
                // Button: Editar información
                OutlinedActionButton(
                    text = "Editar información",
                    onClick = { onEditClick(item.id) }
                )

                // Info banner card
                InfoBannerCard(
                    title = "Gestión del semillero",
                    text = "Edita el grupo o asigna otro docente líder desde el mismo formulario."
                )

                // Button: + Crear semillero
                OutlinedActionButton(
                    text = "+ Crear semillero",
                    onClick = onCreateClick
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    if (showLiderDialog && canManageSemillero) {
        val item = semillero ?: return
        AlertDialog(
            onDismissRequest = { showLiderDialog = false },
            title = { Text("Seleccionar Docente Líder") },
            text = {
                Column {
                    docentes.forEach { doc ->
                        TextButton(
                            onClick = {
                                viewModel.cambiarLiderDocente(item.id, doc.id ?: "101")
                                showLiderDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${doc.nombre} (${doc.facultad ?: ""})",
                                fontSize = 14.sp,
                                color = TextPrimary
                            )
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showLiderDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
