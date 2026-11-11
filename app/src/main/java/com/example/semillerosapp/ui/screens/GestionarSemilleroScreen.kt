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
import com.example.semillerosapp.ui.components.*
import com.example.semillerosapp.ui.theme.*
import com.example.semillerosapp.ui.viewmodel.MainViewModel

@Composable
fun GestionarSemilleroScreen(
    viewModel: MainViewModel,
    initialSemilleroId: Int? = null,
    onBackClick: () -> Unit,
    onSuccess: () -> Unit
) {
    val selectedSemillero by viewModel.selectedSemillero.collectAsState()
    val docentes by viewModel.docentes.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(if (initialSemilleroId != null) 1 else 0) } // 0 = Crear, 1 = Editar

    var nombre by remember(selectedSemillero, selectedTabIndex) {
        mutableStateOf(if (selectedTabIndex == 1) selectedSemillero?.nombre ?: "" else "")
    }
    var facultad by remember(selectedSemillero, selectedTabIndex) {
        mutableStateOf(if (selectedTabIndex == 1) selectedSemillero?.facultad ?: "Facultad de Ingeniería" else "Facultad de Ingeniería")
    }
    var areaEstudiada by remember(selectedSemillero, selectedTabIndex) {
        mutableStateOf(if (selectedTabIndex == 1) selectedSemillero?.area_estudiada ?: "" else "")
    }
    var docenteLiderNombre by remember(selectedSemillero, selectedTabIndex, docentes) {
        mutableStateOf(if (selectedTabIndex == 1) selectedSemillero?.docente_lider_nombre ?: docentes.firstOrNull()?.nombre ?: "Dra. Laura Martínez" else docentes.firstOrNull()?.nombre ?: "Dra. Laura Martínez")
    }
    var lineas by remember(selectedSemillero, selectedTabIndex) {
        mutableStateOf(
            if (selectedTabIndex == 1 && selectedSemillero?.lineas_investigacion?.isNotEmpty() == true) {
                selectedSemillero!!.lineas_investigacion.toMutableList()
            } else {
                mutableStateListOf("", "")
            }
        )
    }

    val facultadesList = listOf("Facultad de Ingeniería", "Facultad de Ciencias Sociales", "Facultad de Ciencias Ambientales")
    val docentesMap = docentes.associateBy { it.nombre }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        AppHeader(
            title = "Gestionar semillero",
            subtitle = "Coordinador · Gestión académica",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SegmentedTabs(
                options = listOf("Crear", "Editar"),
                selectedIndex = selectedTabIndex,
                onOptionSelected = { selectedTabIndex = it }
            )

            Text(
                text = "Información del semillero",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = if (selectedTabIndex == 0) "Ingresa los datos del nuevo grupo. * Obligatorio" else "Actualiza los datos del grupo. * Obligatorio",
                fontSize = 14.sp,
                color = TextSecondary
            )

            // Nombre
            Text(
                text = "Nombre del semillero *",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary
            )
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
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

            // Facultad
            DropdownSelector(
                label = "Facultad *",
                value = facultad,
                options = facultadesList,
                onOptionSelected = { facultad = it }
            )

            // Área estudiada
            Text(
                text = "Área estudiada *",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary
            )
            OutlinedTextField(
                value = areaEstudiada,
                onValueChange = { areaEstudiada = it },
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

            // Docente líder
            val docenteOptions = if (docentes.isNotEmpty()) docentes.map { it.nombre } else listOf("Dra. Laura Martínez", "Dr. Carlos Rojas", "Dra. Juliana Torres")
            DropdownSelector(
                label = "Docente líder *",
                value = docenteLiderNombre,
                options = docenteOptions,
                onOptionSelected = { docenteLiderNombre = it }
            )

            // Líneas de investigación Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = ButtonDefaults.outlinedShape.let { androidx.compose.foundation.BorderStroke(1.dp, BorderColor) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Líneas de investigación *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    lineas.forEachIndexed { index, lineaText ->
                        Text(
                            text = "Línea ${index + 1}${if (index == 0) " *" else ""}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = lineaText,
                            onValueChange = { newTxt ->
                                lineas[index] = newTxt
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = BorderColor,
                                unfocusedBorderColor = BorderColor
                            ),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    OutlinedButton(
                        onClick = { lineas.add("") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryBlue)
                    ) {
                        Text("+ Añadir línea", fontWeight = FontWeight.Bold)
                    }
                }
            }

            InfoBannerCard(
                title = "Asignación de liderazgo",
                text = "Selecciona un docente registrado. Crear guarda un nuevo grupo; Editar actualiza el existente."
            )

            PrimaryActionButton(
                text = if (selectedTabIndex == 0) "Guardar cambios" else "Guardar cambios",
                onClick = {
                    val docenteId = docentesMap[docenteLiderNombre]?.id ?: 101
                    val cleanLineas = lineas.filter { it.isNotBlank() }

                    if (selectedTabIndex == 0) {
                        viewModel.crearSemillero(
                            nombre = nombre,
                            facultad = facultad,
                            area = areaEstudiada,
                            docenteLiderId = docenteId,
                            lineas = cleanLineas,
                            onSuccess = onSuccess
                        )
                    } else {
                        val currentId = selectedSemillero?.id ?: initialSemilleroId ?: 1
                        viewModel.actualizarSemillero(
                            id = currentId,
                            nombre = nombre,
                            facultad = facultad,
                            area = areaEstudiada,
                            docenteLiderId = docenteId,
                            lineas = cleanLineas,
                            onSuccess = onSuccess
                        )
                    }
                }
            )

            OutlinedActionButton(
                text = "Cancelar",
                onClick = onBackClick
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
