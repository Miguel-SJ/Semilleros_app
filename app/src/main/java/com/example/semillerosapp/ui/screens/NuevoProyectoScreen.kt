package com.example.semillerosapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semillerosapp.network.IntegranteDto
import com.example.semillerosapp.ui.components.*
import com.example.semillerosapp.ui.theme.*
import com.example.semillerosapp.ui.viewmodel.MainViewModel

@Composable
fun NuevoProyectoScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onSuccess: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val semilleros by viewModel.semilleros.collectAsState()
    val docentes by viewModel.docentes.collectAsState()
    val estudiantes by viewModel.estudiantes.collectAsState()

    var titulo by remember { mutableStateOf("") }
    var semilleroSeleccionado by remember(semilleros) { mutableStateOf(semilleros.firstOrNull()?.nombre ?: "") }
    var docenteAsesorSeleccionado by remember(docentes) { mutableStateOf(docentes.firstOrNull()?.nombre ?: "") }
    var descripcion by remember { mutableStateOf("") }
    var articulacion by remember { mutableStateOf("") }

    var integrantes by remember {
        mutableStateOf(
            if (currentUser != null) {
                mutableStateListOf(
                    IntegranteDto(
                        nombre = currentUser!!.nombre,
                        codigo = currentUser!!.codigo_estudiantil ?: "ID",
                        rol = "Investigador principal"
                    )
                )
            } else {
                mutableStateListOf<IntegranteDto>()
            }
        )
    }

    var showEstudianteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        AppHeader(
            title = "Nuevo proyecto",
            subtitle = "${currentUser?.rol?.replaceFirstChar { it.uppercase() } ?: "Estudiante"} · ${currentUser?.nombre ?: "Ana María López"}",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Tu investigación empieza aquí",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = "Registra el proyecto y su equipo. * Obligatorio",
                fontSize = 14.sp,
                color = TextSecondary
            )

            // Título
            Text("Título del proyecto *", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = BorderColor,
                    unfocusedBorderColor = BorderColor
                ),
                singleLine = true
            )

            // Semillero Dropdown
            val semilleroOptions = if (semilleros.isNotEmpty()) semilleros.map { it.nombre } else listOf("Inteligencia Artificial", "Territorio y Sociedad", "Biodiversidad y Ambiente")
            DropdownSelector(
                label = "Semillero *",
                value = semilleroSeleccionado,
                options = semilleroOptions,
                onOptionSelected = { semilleroSeleccionado = it }
            )

            // Docente Asesor Dropdown
            val docenteOptions = if (docentes.isNotEmpty()) docentes.map { it.nombre } else listOf("Dra. Laura Martínez", "Dr. Carlos Rojas", "Dra. Juliana Torres")
            DropdownSelector(
                label = "Docente asesor *",
                value = docenteAsesorSeleccionado,
                options = docenteOptions,
                onOptionSelected = { docenteAsesorSeleccionado = it }
            )

            // Descripción
            Text("Descripción *", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = BorderColor,
                    unfocusedBorderColor = BorderColor
                ),
                maxLines = 4
            )

            // Integrantes Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = ButtonDefaults.outlinedShape.let { androidx.compose.foundation.BorderStroke(1.dp, BorderColor) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Integrantes *",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    integrantes.forEach { integrante ->
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text(
                                text = "${integrante.nombre} · ${integrante.codigo}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(
                                text = integrante.rol,
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    OutlinedButton(
                        onClick = { showEstudianteDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryBlue)
                    ) {
                        Text("+ Vincular estudiante", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Solo estudiantes registrados en la plataforma.",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }

            // Articulación interdisciplinar
            Text("Articulación interdisciplinar *", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
            OutlinedTextField(
                value = articulacion,
                onValueChange = { articulacion = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = BorderColor,
                    unfocusedBorderColor = BorderColor
                ),
                maxLines = 3
            )

            PrimaryActionButton(
                text = "Registrar proyecto",
                onClick = {
                    val targetSemillero = semilleros.find { it.nombre == semilleroSeleccionado }
                    val targetDocente = docentes.find { it.nombre == docenteAsesorSeleccionado }

                    viewModel.crearProyecto(
                        titulo = titulo,
                        semilleroId = targetSemillero?.id ?: 1,
                        docenteAsesorId = targetDocente?.id ?: 101,
                        descripcion = descripcion,
                        integrantes = integrantes.toList(),
                        articulacion = articulacion,
                        onSuccess = onSuccess
                    )
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    if (showEstudianteDialog) {
        AlertDialog(
            onDismissRequest = { showEstudianteDialog = false },
            title = { Text("Vincular estudiante") },
            text = {
                Column {
                    estudiantes.forEach { est ->
                        TextButton(
                            onClick = {
                                integrantes.add(
                                    IntegranteDto(
                                        nombre = est.nombre,
                                        codigo = est.codigo_estudiantil ?: "104623000",
                                        rol = "Co-investigador"
                                    )
                                )
                                showEstudianteDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("${est.nombre} (${est.codigo_estudiantil ?: "ID"})")
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showEstudianteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
