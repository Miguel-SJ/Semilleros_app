package com.example.semillerosapp.ui.screens

import androidx.compose.foundation.background
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
import com.example.semillerosapp.ui.components.*
import com.example.semillerosapp.ui.theme.*
import com.example.semillerosapp.ui.viewmodel.MainViewModel

@Composable
fun NuevaCapacitacionScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onSuccess: () -> Unit
) {
    val semilleros by viewModel.semilleros.collectAsState()

    var titulo by remember { mutableStateOf("") }
    var semilleroSeleccionado by remember(semilleros) { mutableStateOf(semilleros.firstOrNull()?.nombre ?: "") }
    var descripcion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var cupoMaximo by remember { mutableStateOf("") }
    var horaInicio by remember { mutableStateOf("") }
    var horaFin by remember { mutableStateOf("") }
    var modalidad by remember { mutableStateOf("Presencial") }
    var lugarOEnlace by remember { mutableStateOf("") }
    var infoAdicional by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        AppHeader(
            title = "Nueva capacitación",
            subtitle = "Organizador · Publicaciones",
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
                text = "Comparte conocimiento",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = "Publicación de tipo capacitación. * Obligatorio",
                fontSize = 14.sp,
                color = TextSecondary
            )

            // Título
            Text("Título de la capacitación *", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
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

            // Descripción
            Text("Descripción *", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
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

            // Fecha & Cupo máximo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Fecha *", fontSize = 13.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = fecha,
                        onValueChange = { fecha = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("Cupo máximo *", fontSize = 13.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = cupoMaximo,
                        onValueChange = { cupoMaximo = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }
            }

            // Horas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Hora de inicio *", fontSize = 13.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = horaInicio,
                        onValueChange = { horaInicio = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("Hora de fin *", fontSize = 13.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = horaFin,
                        onValueChange = { horaFin = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }
            }

            // Modalidad Dropdown
            DropdownSelector(
                label = "Modalidad *",
                value = modalidad,
                options = listOf("Presencial", "Virtual"),
                onOptionSelected = { modalidad = it }
            )

            // Lugar o enlace
            Text("Lugar o enlace *", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
            OutlinedTextField(
                value = lugarOEnlace,
                onValueChange = { lugarOEnlace = it },
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

            // Información adicional
            Text("Información adicional (opcional)", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
            OutlinedTextField(
                value = infoAdicional,
                onValueChange = { infoAdicional = it },
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

            InfoBannerCard(
                title = "Lista de inscritos vacía",
                text = "La capacitación se publicará con 0 inscritos. Los estudiantes podrán reservar su cupo."
            )

            PrimaryActionButton(
                text = "Publicar capacitación",
                onClick = {
                    val targetSemillero = semilleros.find { it.nombre == semilleroSeleccionado }
                    viewModel.crearCapacitacion(
                        titulo = titulo,
                        semilleroId = targetSemillero?.id ?: 1,
                        descripcion = descripcion,
                        fecha = fecha,
                        horaInicio = horaInicio,
                        horaFin = horaFin,
                        modalidad = modalidad,
                        lugarOEnlace = lugarOEnlace,
                        infoAdicional = infoAdicional,
                        cupoMaximo = cupoMaximo.toIntOrNull() ?: 25,
                        onSuccess = onSuccess
                    )
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
