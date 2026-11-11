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
import com.example.semillerosapp.network.HorarioAsesoriaDto
import com.example.semillerosapp.ui.components.*
import com.example.semillerosapp.ui.theme.*
import com.example.semillerosapp.ui.viewmodel.MainViewModel

@Composable
fun RegistrarUsuarioScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onSuccess: () -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(0) } // 0 = Estudiante, 1 = Docente

    // Form states - Estudiante
    var nombreEstudiante by remember { mutableStateOf("") }
    var correoEstudiante by remember { mutableStateOf("") }
    var passEstudiante by remember { mutableStateOf("") }
    var codigoEstudiantil by remember { mutableStateOf("") }
    var programaAcademico by remember { mutableStateOf("") }
    var semestreText by remember { mutableStateOf("") }

    // Form states - Docente
    var nombreDocente by remember { mutableStateOf("") }
    var correoDocente by remember { mutableStateOf("") }
    var passDocente by remember { mutableStateOf("") }
    var facultadDocente by remember { mutableStateOf("Facultad de Ingeniería") }
    var diaAsesoria by remember { mutableStateOf("Lunes") }
    var horaInicio by remember { mutableStateOf("") }
    var horaFin by remember { mutableStateOf("") }

    val facultadesList = listOf("Facultad de Ingeniería", "Facultad de Ciencias Sociales", "Facultad de Ciencias Ambientales")
    val diasList = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        AppHeader(
            title = "Registrar usuario",
            subtitle = "Administrador · Gestión institucional",
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
                options = listOf("Estudiante", "Docente"),
                selectedIndex = selectedTabIndex,
                onOptionSelected = { selectedTabIndex = it }
            )

            if (selectedTabIndex == 0) {
                Text(
                    text = "Nuevo estudiante",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Text(
                    text = "Datos de acceso y perfil académico. * Obligatorio",
                    fontSize = 14.sp,
                    color = TextSecondary
                )

                Text("Nombre completo *", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
                OutlinedTextField(
                    value = nombreEstudiante,
                    onValueChange = { nombreEstudiante = it },
                    placeholder = { Text("Ej: Ana Lopez") },
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

                // Correo
                Text("Correo institucional *", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
                OutlinedTextField(
                    value = correoEstudiante,
                    onValueChange = { correoEstudiante = it },
                    placeholder = { Text("ana.lopez@uniautonoma.edu.co") },
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

                // Contraseña
                Text("Contraseña *", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
                OutlinedTextField(
                    value = passEstudiante,
                    onValueChange = { passEstudiante = it },
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

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = ButtonDefaults.outlinedShape.let { androidx.compose.foundation.BorderStroke(1.dp, BorderColor) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Perfil académico *",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Código estudiantil *", fontSize = 13.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = codigoEstudiantil,
                            onValueChange = { codigoEstudiantil = it },
                            placeholder = { Text("Ej: 104623017") },
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

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Programa académico *", fontSize = 13.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = programaAcademico,
                            onValueChange = { programaAcademico = it },
                            placeholder = { Text("Ej: Ingeniería de Sistemas") },
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

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Semestre *", fontSize = 13.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = semestreText,
                            onValueChange = { semestreText = it },
                            placeholder = { Text("Ej: 6") },
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
                    }
                }

                InfoBannerCard(
                    title = "Rol asignado: estudiante",
                    text = "El perfil académico se guardará junto con la cuenta para habilitar su acceso."
                )

                PrimaryActionButton(
                    text = "Registrar estudiante",
                    onClick = {
                        viewModel.registrarEstudiante(
                            nombre = nombreEstudiante,
                            correo = correoEstudiante,
                            pass = passEstudiante,
                            codigo = codigoEstudiantil,
                            programa = programaAcademico,
                            semestre = semestreText.toIntOrNull() ?: 6,
                            onSuccess = onSuccess
                        )
                    }
                )

            } else {
                Text(
                    text = "Nuevo docente",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Text(
                    text = "Registra al docente y sus asesorías. * Obligatorio",
                    fontSize = 14.sp,
                    color = TextSecondary
                )

                // Nombre completo
                Text("Nombre completo *", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
                OutlinedTextField(
                    value = nombreDocente,
                    onValueChange = { nombreDocente = it },
                    placeholder = { Text("Ej: Laura Martínez") },
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

                Text("Correo institucional *", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
                OutlinedTextField(
                    value = correoDocente,
                    onValueChange = { correoDocente = it },
                    placeholder = { Text("laura.martinez@uniautonoma.edu.co") },
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

                Text("Contraseña *", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
                OutlinedTextField(
                    value = passDocente,
                    onValueChange = { passDocente = it },
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

                DropdownSelector(
                    label = "Facultad *",
                    value = facultadDocente,
                    options = facultadesList,
                    onOptionSelected = { facultadDocente = it }
                )

                // Horarios de asesoría
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = ButtonDefaults.outlinedShape.let { androidx.compose.foundation.BorderStroke(1.dp, BorderColor) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Horarios de asesoría *",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        DropdownSelector(
                            value = diaAsesoria,
                            options = diasList,
                            onOptionSelected = { diaAsesoria = it }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Hora de inicio", fontSize = 12.sp, color = TextSecondary)
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
                                Text("Hora de fin", fontSize = 12.sp, color = TextSecondary)
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

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryBlue)
                        ) {
                            Text("+ Añadir horario", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                InfoBannerCard(
                    title = "Rol asignado: docente",
                    text = "Disponible para liderar semilleros o asesorar proyectos una vez registrado."
                )

                PrimaryActionButton(
                    text = "Registrar docente",
                    onClick = {
                        viewModel.registrarDocente(
                            nombre = nombreDocente,
                            correo = correoDocente,
                            pass = passDocente,
                            facultad = facultadDocente,
                            horarios = listOf(HorarioAsesoriaDto(diaAsesoria, horaInicio, horaFin)),
                            onSuccess = onSuccess
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
