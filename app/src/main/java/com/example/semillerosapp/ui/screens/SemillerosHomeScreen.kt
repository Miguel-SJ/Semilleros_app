package com.example.semillerosapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semillerosapp.network.SemilleroDto
import com.example.semillerosapp.ui.components.AppBottomNavBar
import com.example.semillerosapp.ui.components.AppHeader
import com.example.semillerosapp.ui.components.DropdownSelector
import com.example.semillerosapp.ui.components.OutlinedActionButton
import com.example.semillerosapp.ui.theme.*
import com.example.semillerosapp.ui.viewmodel.MainViewModel

@Composable
fun SemillerosHomeScreen(
    viewModel: MainViewModel,
    onSemilleroClick: (Int) -> Unit,
    onRegistrarProyectoClick: () -> Unit,
    onEventosTabClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val semilleros by viewModel.semilleros.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFacultad by viewModel.selectedFacultadFilter.collectAsState()
    val selectedArea by viewModel.selectedAreaFilter.collectAsState()

    val facultadOptions = listOf("todas", "Ingeniería", "Ciencias Sociales", "Ciencias Ambientales")
    val areaOptions = listOf("todas", "Tecnología", "Estudios sociales", "Ecología")

    Scaffold(
        bottomBar = {
            AppBottomNavBar(
                selectedTab = 0,
                onTabSelected = { tab ->
                    if (tab == 1) onEventosTabClick()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(ScreenBackground)
        ) {
            AppHeader(
                title = "Semilleros",
                subtitle = "${currentUser?.rol?.replaceFirstChar { it.uppercase() } ?: "Estudiante"} · ${currentUser?.nombre ?: "Usuario"}",
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
                        text = "Encuentra tu semillero",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.searchSemilleros(it) },
                        placeholder = { Text("Buscar por nombre", color = TextMuted) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = TextSecondary
                            )
                        },
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
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DropdownSelector(
                            value = "Facultad: $selectedFacultad",
                            options = facultadOptions.map { "Facultad: $it" },
                            onOptionSelected = { sel ->
                                val clean = sel.replace("Facultad: ", "")
                                viewModel.filterByFacultad(clean)
                            },
                            modifier = Modifier.weight(1f)
                        )

                        DropdownSelector(
                            value = "Área: $selectedArea",
                            options = areaOptions.map { "Área: $it" },
                            onOptionSelected = { sel ->
                                val clean = sel.replace("Área: ", "")
                                viewModel.filterByArea(clean)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Text(
                        text = "${semilleros.size} semilleros disponibles",
                        fontSize = 14.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                    )
                }

                items(semilleros) { semillero ->
                    SemilleroItemCard(
                        semillero = semillero,
                        onClick = { onSemilleroClick(semillero.id) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedActionButton(
                        text = "Registrar proyecto",
                        onClick = onRegistrarProyectoClick
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun SemilleroItemCard(
    semillero: SemilleroDto,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(1.dp, BorderColor, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(LightBlueBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Book,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = semillero.nombre,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Ver detalle",
                    tint = PrimaryBlue
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "${semillero.facultad.replace("Facultad de ", "")} · ${semillero.area_estudiada}",
                fontSize = 13.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Líder · ${semillero.docente_lider_nombre ?: "Por asignar"}",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }
    }
}
