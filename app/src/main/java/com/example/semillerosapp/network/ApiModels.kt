package com.example.semillerosapp.network

import com.google.gson.annotations.SerializedName

// --- Auth & Usuario ---

data class LoginRequest(
    @SerializedName("correo_institucional") val correo_institucional: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("message") val message: String?,
    @SerializedName("token") val token: String?,
    @SerializedName("usuario") val usuario: UsuarioDto?
)

data class UsuarioDto(
    @SerializedName("_id") val id: String = "",
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellido") val apellido: String? = null,
    @SerializedName("correo_institucional") val correo_institucional: String,
    @SerializedName("rol") val rol: String = "estudiante",
    @SerializedName("codigo_estudiantil") val codigo_estudiantil: String? = null,
    @SerializedName("programa_academico") val programa_academico: String? = null,
    @SerializedName("semestre_actual") val semestre_actual: Int? = null,
    @SerializedName("facultad") val facultad: String? = null
)

data class RegistroEstudianteRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellido") val apellido: String = "",
    @SerializedName("correo_institucional") val correo_institucional: String,
    @SerializedName("password") val password: String,
    @SerializedName("telefono") val telefono: String = "",
    @SerializedName("perfil_estudiante") val perfil_estudiante: PerfilEstudiante
)

data class PerfilEstudiante(
    @SerializedName("codigo_estudiantil") val codigo_estudiantil: String,
    @SerializedName("programa_academico") val programa_academico: String,
    @SerializedName("semestre_actual") val semestre_actual: Int
)

data class HorarioAsesoriaDto(
    @SerializedName("dia") val dia: String,
    @SerializedName("hora_inicio") val hora_inicio: String,
    @SerializedName("hora_fin") val hora_fin: String
)

data class RegistroDocenteRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellido") val apellido: String = "",
    @SerializedName("correo_institucional") val correo_institucional: String,
    @SerializedName("password") val password: String,
    @SerializedName("telefono") val telefono: String = "",
    @SerializedName("perfil_docente") val perfil_docente: PerfilDocente
)

data class PerfilDocente(
    @SerializedName("escalafon") val escalafon: String? = null,
    @SerializedName("departamento_facultad") val departamento_facultad: String? = null
)

data class RegistroResponse(
    @SerializedName("token") val token: String?,
    @SerializedName("usuario") val usuario: UsuarioDto?
)

// --- Semilleros ---

data class SemilleroDto(
    @SerializedName("_id") val id: String = "", // ¡Corregido a String!
    @SerializedName("nombre") val nombre: String,
    @SerializedName("facultad") val facultad: String,
    @SerializedName("id_docente_lider") val id_docente_lider: String? = null,
    @SerializedName("estado") val estado: Boolean = true
)

data class CrearSemilleroRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("facultad") val facultad: String,
    @SerializedName("id_docente_lider") val id_docente_lider: String,
    @SerializedName("area_estudiada") val area_estudiada: Map<String, String>,
    @SerializedName("lineas_investigacion") val lineas_investigacion: List>


data class ActualizarSemilleroRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("facultad") val facultad: String,
    @SerializedName("id_docente_lider") val id_docente_lider: String
)

// --- Proyectos ---

data class ProyectoDto(
    @SerializedName("_id") val id: String = "",
    @SerializedName("titulo") val titulo: String,
    @SerializedName("id_docente_asesor") val id_docente_asesor: String? = null,
    @SerializedName("descripcion_general") val descripcion_general: String? = null
)

data class CrearProyectoRequest(
    @SerializedName("titulo") val titulo: String,
    @SerializedName("id_docente_asesor") val id_docente_asesor: String,
    @SerializedName("integrantes_participacion") val integrantes_participacion: List>


// --- Capacitaciones ---

data class CapacitacionDto(
    @SerializedName("_id") val id: String = "",
    @SerializedName("titulo") val titulo: String,
    @SerializedName("id_semillero_organizador") val id_semillero_organizador: String? = null
)

data class CrearCapacitacionRequest(
    @SerializedName("titulo") val titulo: String,
    @SerializedName("id_semillero_organizador") val id_semillero_organizador: String,
    @SerializedName("detalles_capacitacion") val detalles_capacitacion: Map<String, Any>
)

// --- Carga docentes ---
data class DocenteCargaDto(
    @SerializedName("id_docente") val id_docente: String,
    @SerializedName("docente") val docente: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("departamento") val departamento: String? = null,
    @SerializedName("total_proyectos") val total_proyectos: Int
)

data class CargaDocentesResponse(
    @SerializedName("total_proyectos") val total_proyectos: Int,
    @SerializedName("total_docentes") val total_docentes: Int,
    @SerializedName("docentes") val docentes: List<DocenteCargaDto>
)