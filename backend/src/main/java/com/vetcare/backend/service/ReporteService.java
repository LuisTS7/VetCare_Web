package com.vetcare.backend.service;

import com.vetcare.backend.entity.Cita;
import com.vetcare.backend.repository.AtencionRepository;
import com.vetcare.backend.repository.CitaRepository;
import com.vetcare.backend.repository.MascotaRepository;
import com.vetcare.backend.repository.PropietarioRepository;
import com.vetcare.backend.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ReporteService {

    private final PropietarioRepository propietarioRepository;
    private final MascotaRepository mascotaRepository;
    private final CitaRepository citaRepository;
    private final AtencionRepository atencionRepository;
    private final UsuarioRepository usuarioRepository;

    public ReporteService(
            PropietarioRepository propietarioRepository,
            MascotaRepository mascotaRepository,
            CitaRepository citaRepository,
            AtencionRepository atencionRepository,
            UsuarioRepository usuarioRepository) {

        this.propietarioRepository = propietarioRepository;
        this.mascotaRepository = mascotaRepository;
        this.citaRepository = citaRepository;
        this.atencionRepository = atencionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Map<String, Object> obtenerIndicadores() {

        LocalDate hoy = LocalDate.now();

        List<Cita> citasHoy =
                citaRepository.findByFechaOrderByHoraAsc(hoy);

        long citasPendientes = citasHoy.stream()
                .filter(c ->
                        c.getEstado().equalsIgnoreCase("Pendiente")
                )
                .count();

        long citasConfirmadas = citasHoy.stream()
                .filter(c ->
                        c.getEstado().equalsIgnoreCase("Confirmada")
                )
                .count();

        long citasAtendidas = citasHoy.stream()
                .filter(c ->
                        c.getEstado().equalsIgnoreCase("Atendida")
                )
                .count();

        long citasCanceladas = citasHoy.stream()
                .filter(c ->
                        c.getEstado().equalsIgnoreCase("Cancelada")
                )
                .count();

        long veterinariosActivos =
                usuarioRepository
                        .findByRolIgnoreCaseAndEstadoIgnoreCase(
                                "Médico veterinario",
                                "Activo"
                        )
                        .size();

        Map<String, Object> indicadores =
                new LinkedHashMap<>();

        indicadores.put("fecha", hoy);
        indicadores.put(
                "totalPropietarios",
                propietarioRepository.count()
        );
        indicadores.put(
                "totalMascotas",
                mascotaRepository.count()
        );
        indicadores.put(
                "totalCitas",
                citaRepository.count()
        );
        indicadores.put(
                "totalAtenciones",
                atencionRepository.count()
        );
        indicadores.put(
                "veterinariosActivos",
                veterinariosActivos
        );

        indicadores.put(
                "citasHoy",
                citasHoy.size()
        );
        indicadores.put(
                "citasPendientesHoy",
                citasPendientes
        );
        indicadores.put(
                "citasConfirmadasHoy",
                citasConfirmadas
        );
        indicadores.put(
                "citasAtendidasHoy",
                citasAtendidas
        );
        indicadores.put(
                "citasCanceladasHoy",
                citasCanceladas
        );

        return indicadores;
    }
}