package com.example.semillerosapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semillerosapp.data.Repository
import com.example.semillerosapp.network.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository = Repository()) : ViewModel() {

    private val _currentUser = MutableStateFlow<UsuarioDto?>(repository.currentUser)
    val currentUser: StateFlow<UsuarioDto?> = _currentUser.asStateFlow()

    private val _semilleros = MutableStateFlow<List<SemilleroDto>>(emptyList())
    val semilleros: StateFlow<List<SemilleroDto>> = _semilleros.asStateFlow()

    private val _selectedSemillero = MutableStateFlow<SemilleroDto?>(null)
    val selectedSemillero: StateFlow<SemilleroDto?> = _selectedSemillero.asStateFlow()

    private val _proyectos = MutableStateFlow<List<ProyectoDto>>(emptyList())
    val proyectos: StateFlow<List<ProyectoDto>> = _proyectos.asStateFlow()

    private val _capacitaciones = MutableStateFlow<List<CapacitacionDto>>(emptyList())
    val capacitaciones: StateFlow<List<CapacitacionDto>> = _capacitaciones.asStateFlow()

    private val _cargaDocentes = MutableStateFlow<CargaDocentesResponse?>(null)
    val cargaDocentes: StateFlow<CargaDocentesResponse?> = _cargaDocentes.asStateFlow()

    private val _docentes = MutableStateFlow<List<UsuarioDto>>(emptyList())
    val docentes: StateFlow<List<UsuarioDto>> = _docentes.asStateFlow()

    private val _estudiantes = MutableStateFlow<List<UsuarioDto>>(emptyList())
    val estudiantes: StateFlow<List<UsuarioDto>> = _estudiantes.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFacultadFilter = MutableStateFlow("todas")
    val selectedFacultadFilter: StateFlow<String> = _selectedFacultadFilter.asStateFlow()

    private val _selectedAreaFilter = MutableStateFlow("todas")
    val selectedAreaFilter: StateFlow<String> = _selectedAreaFilter.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            _isLoading.value = true
            loadSemilleros()
            loadCapacitaciones()
            loadProyectos()
            loadCargaDocentes()
            loadDocentesAndEstudiantes()
            _isLoading.value = false
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun login(correo: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val res = repository.login(correo, pass)
            _isLoading.value = false
            res.onSuccess { user ->
                _currentUser.value = user
                _toastMessage.value = "Bienvenido ${user.nombre}"
                onSuccess()
            }.onFailure { err ->
                _toastMessage.value = err.message ?: "Error al iniciar sesión"
            }
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        _currentUser.value = null
        onLoggedOut()
    }

    fun loadSemilleros() {
        viewModelScope.launch {
            val res = repository.getSemilleros(
                facultad = _selectedFacultadFilter.value,
                area = _selectedAreaFilter.value,
                query = _searchQuery.value
            )
            res.onSuccess { _semilleros.value = it }
        }
    }

    fun searchSemilleros(query: String) {
        _searchQuery.value = query
        loadSemilleros()
    }

    fun filterByFacultad(facultad: String) {
        _selectedFacultadFilter.value = facultad
        loadSemilleros()
    }

    fun filterByArea(area: String) {
        _selectedAreaFilter.value = area
        loadSemilleros()
    }

    fun selectSemilleroDetail(id: Int) {
        viewModelScope.launch {
            val res = repository.getSemilleroDetail(id)
            res.onSuccess { _selectedSemillero.value = it }
        }
    }

    fun crearSemillero(
        nombre: String,
        facultad: String,
        area: String,
        docenteLiderId: Any,
        lineas: List<String>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val req = CrearSemilleroRequest(nombre, facultad, area, docenteLiderId, lineas)
            val res = repository.crearSemillero(req)
            _isLoading.value = false
            res.onSuccess {
                _toastMessage.value = "Semillero creado exitosamente"
                loadSemilleros()
                onSuccess()
            }
        }
    }

    fun actualizarSemillero(
        id: Int,
        nombre: String,
        facultad: String,
        area: String,
        docenteLiderId: Any,
        lineas: List<String>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val req = ActualizarSemilleroRequest(nombre, facultad, area, docenteLiderId, lineas)
            val res = repository.actualizarSemillero(id, req)
            _isLoading.value = false
            res.onSuccess {
                _selectedSemillero.value = it
                _toastMessage.value = "Semillero actualizado"
                loadSemilleros()
                onSuccess()
            }
        }
    }

    fun cambiarLiderDocente(semilleroId: Int, nuevoLiderId: Any) {
        viewModelScope.launch {
            _isLoading.value = true
            val res = repository.cambiarLiderDocente(semilleroId, nuevoLiderId)
            _isLoading.value = false
            res.onSuccess {
                _selectedSemillero.value = it
                _toastMessage.value = "Docente líder actualizado"
                loadSemilleros()
            }
        }
    }

    fun registrarEstudiante(
        nombre: String,
        correo: String,
        pass: String,
        codigo: String,
        programa: String,
        semestre: Int,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val req = RegistroEstudianteRequest(
                nombre = nombre,
                correo_institucional = correo,
                password = pass,
                perfil_estudiante = PerfilEstudiante(codigo, programa, semestre)
            )
            val res = repository.registrarEstudiante(req)
            _isLoading.value = false
            res.onSuccess {
                _toastMessage.value = "Estudiante registrado con éxito"
                loadDocentesAndEstudiantes()
                onSuccess()
            }
        }
    }

    fun registrarDocente(
        nombre: String,
        correo: String,
        pass: String,
        facultad: String,
        horarios: List<HorarioAsesoriaDto>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val req = RegistroDocenteRequest(
                nombre = nombre,
                correo_institucional = correo,
                password = pass,
                facultad = facultad
            )
            val res = repository.registrarDocente(req)
            _isLoading.value = false
            res.onSuccess {
                _toastMessage.value = "Docente registrado con éxito"
                loadDocentesAndEstudiantes()
                loadCargaDocentes()
                onSuccess()
            }
        }
    }

    fun loadProyectos() {
        viewModelScope.launch {
            val res = repository.getProyectos()
            res.onSuccess { _proyectos.value = it }
        }
    }

    fun crearProyecto(
        titulo: String,
        semilleroId: Any,
        docenteAsesorId: Any,
        descripcion: String,
        integrantes: List<IntegranteDto>,
        articulacion: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val req = CrearProyectoRequest(
                titulo = titulo,
                semillero_id = semilleroId,
                docente_asesor_id = docenteAsesorId,
                descripcion = descripcion,
                integrantes = integrantes,
                articulacion_interdisciplinar = articulacion
            )
            val res = repository.crearProyecto(req)
            _isLoading.value = false
            res.onSuccess {
                _toastMessage.value = "Proyecto registrado correctamente"
                loadProyectos()
                loadCargaDocentes()
                onSuccess()
            }
        }
    }

    fun loadCapacitaciones(semilleroId: Int? = null) {
        viewModelScope.launch {
            val res = repository.getCapacitaciones(semilleroId)
            res.onSuccess { _capacitaciones.value = it }
        }
    }

    fun crearCapacitacion(
        titulo: String,
        semilleroId: Int,
        descripcion: String,
        fecha: String,
        horaInicio: String,
        horaFin: String,
        modalidad: String,
        lugarOEnlace: String,
        infoAdicional: String?,
        cupoMaximo: Int,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val req = CrearCapacitacionRequest(
                titulo = titulo,
                semillero_id = semilleroId,
                descripcion = descripcion,
                fecha = fecha,
                hora_inicio = horaInicio,
                hora_fin = horaFin,
                modalidad = modalidad,
                lugar_o_enlace = lugarOEnlace,
                informacion_adicional = infoAdicional,
                cupo_maximo = cupoMaximo
            )
            val res = repository.crearCapacitacion(req)
            _isLoading.value = false
            res.onSuccess {
                _toastMessage.value = "Capacitación publicada"
                loadCapacitaciones()
                onSuccess()
            }
        }
    }

    fun inscribirCapacitacion(id: Int) {
        viewModelScope.launch {
            val res = repository.inscribirCapacitacion(id)
            res.onSuccess {
                _toastMessage.value = "¡Inscripción confirmada!"
                loadCapacitaciones()
            }
        }
    }

    fun loadCargaDocentes() {
        viewModelScope.launch {
            val res = repository.getCargaDocentes()
            res.onSuccess { _cargaDocentes.value = it }
        }
    }

    fun loadDocentesAndEstudiantes() {
        viewModelScope.launch {
            repository.getDocentes().onSuccess { _docentes.value = it }
            repository.getEstudiantes().onSuccess { _estudiantes.value = it }
        }
    }
}
