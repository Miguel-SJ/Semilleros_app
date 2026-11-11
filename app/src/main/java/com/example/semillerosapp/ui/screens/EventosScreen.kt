package com.example.semillerosapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semillerosapp.network.CapacitacionDto
import com.example.semillerosapp.ui.components.*
import com.example.semillerosapp.ui.theme.*
import com.example.semillerosapp.ui.viewmodel.MainViewModel

@Composable
fun EventosScreen(
    viewModel: MainViewModel,
    onSemillerosTabClick: () -> Unit,
    onNuevaCapacitacionClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val capacitaciones by viewModel.capacitaciones.collectAsState()
    val semilleros by viewModel.semilleros.collectAsState()

    val userRol = currentUser?.rol?.lowercase() ?: "estudiante"
    val canCreateCapacitacion = userRol == "docente" || userRol == "coordinador" || userRol == "administrador" || userRol == "admin"

    var selectedSemilleroFilter by remember { mutableStateOf("Todos los semilleros") }

    val filterOptions = listOf("Todos los semilleros") + semilleros.map { it.nombre }

    val filteredCapacitaciones = remember(capacitaciones, selectedSemilleroFilter) {
        if (selectedSemilleroFilter == "Todos los semilleros") {
            capacitaciones
        } else {
            capacitaciones.filter { it.semillero_nombre == selectedSemilleroFilter }
        }
    }

    Scaffold(
        bottomBar = {
            AppBottomNavBar(
                selectedTab = 1,
                onTabSelected = { tab ->
                    if (tab == 0) onSemillerosTabClick()
                }
            )
        },
        floatingActionButton = {
            if (canCreateCapacitacion) {
                FloatingActionButton(
                    onClick = onNuevaCapacitacionClick,
                    containerColor = PrimaryBlue,
                    contentColor = Color.White
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Nueva capacitación")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(ScreenBackground)
        ) {
            AppHeader(
                title = "Eventos",
                subtitle = "${currentUser?.rol?.replaceFirstChar { it.uppercase() } ?: "Estudiante"} · ${currentUser?.nombre ?: ""}",
                onLogoutClick = {
                    viewModel.logout(onLogoutClick)
                }
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
                        text = "Aprende con tu semillero",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                item {
                    DropdownSelector(
                        label = "Semillero",
                        value = selectedSemilleroFilter,
                        options = filterOptions,
                        onOptionSelected = { selectedSemilleroFilter = it }
                    )
                }

                items(filteredCapacitaciones) { capacitacion ->
                    EventCardItem(
                        capacitacion = capacitacion,
                        onInscribirse = {
                            viewModel.inscribirCapacitacion(capacitacion.id)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    InfoBannerCard(
                        title = "Inscripción con cupo validado",
                        text = "No se permiten inscripciones duplicadas. Si se completa el cupo, la inscripción se deshabilita."
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun EventCardItem(
    capacitacion: CapacitacionDto,
    onInscribirse: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(1.dp, BorderColor, RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Column {
            // State badge
            Text(
                text = capacitacion.estado_inscripcion,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (capacitacion.esta_inscrito) Color(0xFF1E5BB0) else PrimaryBlue
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Title
            Text(
                text = capacitacion.titulo,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Semillero
            Text(
                text = capacitacion.semillero_nombre ?: "Inteligencia Artificial",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = PrimaryBlue
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Date time & location
            Text(
                text = "${capacitacion.fecha} · ${capacitacion.hora_inicio}–${capacitacion.hora_fin}",
                fontSize = 13.sp,
                color = TextSecondary
            )
            Text(
                text = "${capacitacion.lugar_o_enlace} · ${capacitacion.modalidad}",
                fontSize = 13.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Description
            Text(
                text = capacitacion.descripcion,
                fontSize = 14.sp,
                color = TextPrimary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Occupancy
            Text(
                text = "${capacitacion.cupos_ocupados} de ${capacitacion.cupo_maximo} cupos ocupados",
                fontSize = 13.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (capacitacion.esta_inscrito) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Ya estás inscrita",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                }
            } else {
                PrimaryActionButton(
                    text = "Inscribirme",
                    onClick = onInscribirse
                )
            }
        }
    }
}
