package com.example.semillerosapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semillerosapp.network.DocenteCargaDto
import com.example.semillerosapp.ui.components.AppHeader
import com.example.semillerosapp.ui.components.OutlinedActionButton
import com.example.semillerosapp.ui.theme.*
import com.example.semillerosapp.ui.viewmodel.MainViewModel

@Composable
fun CargaDocentesScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onRegistrarUsuarioClick: () -> Unit
) {
    val cargaState by viewModel.cargaDocentes.collectAsState()

    val totalProyectos = cargaState?.total_proyectos ?: 12
    val totalDocentes = cargaState?.total_docentes ?: 3
    val docentesList = cargaState?.docentes ?: listOf(
        DocenteCargaDto(id = 101, nombre = "Dra. Laura Martínez", facultad = "Facultad de Ingeniería", proyectos_count = 6),
        DocenteCargaDto(id = 102, nombre = "Dr. Carlos Rojas", facultad = "Facultad de Ciencias Sociales", proyectos_count = 4),
        DocenteCargaDto(id = 103, nombre = "Dra. Juliana Torres", facultad = "Facultad de Ciencias Ambientales", proyectos_count = 2)
    )

    val maxProyectos = (docentesList.maxOfOrNull { it.proyectos_count } ?: 6).coerceAtLeast(1)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        AppHeader(
            title = "Carga de docentes",
            subtitle = "Administrador · Gestión institucional",
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Carga de asesorías",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Proyectos por docente asesor, de mayor a menor carga.",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }

            // Hero Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(LightBlueBg)
                        .border(1.dp, LightBlueBorder, RoundedCornerShape(16.dp))
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = "$totalProyectos proyectos",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$totalDocentes docentes asesores · Total acumulado",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Table headers
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Docente asesor",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                    Text(
                        text = "Proyectos",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                }
            }

            // List of docentes with progress bar
            items(docentesList) { docente ->
                DocenteCargaCard(
                    docente = docente,
                    maxProyectos = maxProyectos
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedActionButton(
                    text = "Registrar usuario",
                    onClick = onRegistrarUsuarioClick
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun DocenteCargaCard(
    docente: DocenteCargaDto,
    maxProyectos: Int
) {
    val progress = (docente.proyectos_count.toFloat() / maxProyectos.toFloat()).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(1.dp, BorderColor, RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = docente.nombre,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = docente.facultad,
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }

                Text(
                    text = "${docente.proyectos_count}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = PrimaryBlue,
                trackColor = LightBlueBg
            )
        }
    }
}
