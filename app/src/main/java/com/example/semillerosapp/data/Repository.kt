package com.example.semillerosapp.data

import com.example.semillerosapp.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val apiService: ApiService = RetrofitClient.instance) {

    var currentUser: UsuarioDto? = null
        private set

    suspend fun login(correo: String, pass: String): Result<UsuarioDto> = withContext(Dispatchers.IO) {
        try {
            val req = LoginRequest(correo_institucional = correo.trim().lowercase(), password = pass.trim())
            val response = apiService.loginUsuario(req)

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                body.token?.let { RetrofitClient.authToken = it }
                currentUser = body.usuario
                Result.success(body.usuario!!)
            } else {
                Result.failure(Exception("Credenciales incorrectas"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión con la API"))
        }
    }

    suspend fun registrarEstudiante(request: RegistroEstudianteRequest): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.registrarEstudiante(request)
            if (response.isSuccessful) {
                response.body()?.token?.let { RetrofitClient.authToken = it }
                currentUser = response.body()?.usuario
                Result.success(true)
            } else {
                Result.failure(Exception("Error al registrar"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión"))
        }
    }

    suspend fun crearSemillero(request: CrearSemilleroRequest): Result<SemilleroDto> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.crearSemillero(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear semillero"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión"))
        }
    }
}