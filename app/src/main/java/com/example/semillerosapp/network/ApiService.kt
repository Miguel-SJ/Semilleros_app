package com.example.semillerosapp.network

import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("usuarios/login")
    suspend fun loginUsuario(@Body request: LoginRequest): Response<LoginResponse>

    @POST("usuarios/estudiante")
    suspend fun registrarEstudiante(@Body request: RegistroEstudianteRequest): Response<RegistroResponse>

    @POST("usuarios/docente")
    suspend fun registrarDocente(@Body request: RegistroDocenteRequest): Response<RegistroResponse>

    @GET("usuarios/detalles")
    suspend fun getDocentes(): Response<List<UsuarioDto>>

    @GET("semilleros/detalles")
    suspend fun getSemilleros(
        @Query("facultad") facultad: String? = null,
        @Query("area") area: String? = null
    ): Response<List<SemilleroDto>>

    @GET("semilleros/{id}")
    suspend fun getSemilleroDetail(@Path("id") id: String): Response<SemilleroDto>

    @POST("semilleros")
    suspend fun crearSemillero(@Body request: CrearSemilleroRequest): Response<SemilleroDto>

    @PUT("semilleros/{id}")
    suspend fun actualizarSemillero(
        @Path("id") id: String,
        @Body request: ActualizarSemilleroRequest
    ): Response<SemilleroDto>

    @POST("proyectos")
    suspend fun crearProyecto(@Body request: CrearProyectoRequest): Response<ProyectoDto>

    @POST("publicaciones")
    suspend fun publicarEvento(@Body request: CrearCapacitacionRequest): Response<CapacitacionDto>

    @PUT("publicaciones/{id}/inscribir")
    suspend fun inscribirCapacitacion(@Path("id") id: String, @Body request: Map<String, String>): Response>

    @GET("reportes/carga-docentes")
    suspend fun getCargaDocentes(): Response<List<DocenteCargaDto>>
}