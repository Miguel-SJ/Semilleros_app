package com.example.semillerosapp.ui.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.semillerosapp.ui.screens.*
import com.example.semillerosapp.ui.viewmodel.MainViewModel

object Routes {
    const val LOGIN = "login"
    const val SEMILLEROS_HOME = "semilleros_home"
    const val SEMILLERO_DETAIL = "semillero_detail/{id}"
    const val GESTIONAR_SEMILLERO = "gestionar_semillero?id={id}"
    const val REGISTRAR_USUARIO = "registrar_usuario"
    const val NUEVO_PROYECTO = "nuevo_proyecto"
    const val EVENTOS = "eventos"
    const val NUEVA_CAPACITACION = "nueva_capacitacion"
    const val CARGA_DOCENTES = "carga_docentes"
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val toastMessage by viewModel.toastMessage.collectAsState()

    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.SEMILLEROS_HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTRAR_USUARIO)
                }
            )
        }

        composable(Routes.SEMILLEROS_HOME) {
            SemillerosHomeScreen(
                viewModel = viewModel,
                onSemilleroClick = { id ->
                    viewModel.selectSemilleroDetail(id)
                    navController.navigate("semillero_detail/$id")
                },
                onRegistrarProyectoClick = {
                    navController.navigate(Routes.NUEVO_PROYECTO)
                },
                onEventosTabClick = {
                    navController.navigate(Routes.EVENTOS)
                },
                onLogoutClick = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.SEMILLERO_DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 1
            LaunchedEffect(id) {
                viewModel.selectSemilleroDetail(id)
            }
            SemilleroDetailScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onEditClick = { semId ->
                    navController.navigate("gestionar_semillero?id=$semId")
                },
                onCreateClick = {
                    navController.navigate("gestionar_semillero?id=-1")
                }
            )
        }

        composable(
            route = Routes.GESTIONAR_SEMILLERO,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            val cleanId = if (id != null && id > 0) id else null
            GestionarSemilleroScreen(
                viewModel = viewModel,
                initialSemilleroId = cleanId,
                onBackClick = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(Routes.REGISTRAR_USUARIO) {
            RegistrarUsuarioScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onSuccess = {
                    if (navController.previousBackStackEntry?.destination?.route == Routes.LOGIN) {
                        navController.navigate(Routes.SEMILLEROS_HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    } else {
                        navController.popBackStack()
                    }
                }
            )
        }

        composable(Routes.NUEVO_PROYECTO) {
            NuevoProyectoScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(Routes.EVENTOS) {
            EventosScreen(
                viewModel = viewModel,
                onSemillerosTabClick = {
                    navController.navigate(Routes.SEMILLEROS_HOME) {
                        popUpTo(Routes.SEMILLEROS_HOME) { inclusive = true }
                    }
                },
                onNuevaCapacitacionClick = {
                    navController.navigate(Routes.NUEVA_CAPACITACION)
                },
                onLogoutClick = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.NUEVA_CAPACITACION) {
            NuevaCapacitacionScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(Routes.CARGA_DOCENTES) {
            CargaDocentesScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onRegistrarUsuarioClick = {
                    navController.navigate(Routes.REGISTRAR_USUARIO)
                }
            )
        }
    }
}
