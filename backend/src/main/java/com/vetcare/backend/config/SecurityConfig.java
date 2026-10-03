package com.vetcare.backend.config;

import com.vetcare.backend.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .csrf(csrf ->
                        csrf.disable()
                )

                .cors(cors -> {
                })

                .authorizeHttpRequests(auth -> auth

                        /*
                         * SWAGGER / OPENAPI
                         */
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        /*
                         * LOGIN
                         */
                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        /*
                         * LISTADO DE VETERINARIOS
                         *
                         * Se permite a Administrador y Recepcionista
                         * consultar los veterinarios activos necesarios
                         * para programar una cita.
                         *
                         * IMPORTANTE:
                         * Esta regla debe estar antes de /api/usuarios/**
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/usuarios/veterinarios"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "RECEPCIONISTA"
                        )

                        /*
                         * USUARIOS
                         * La administración general de usuarios
                         * corresponde únicamente al Administrador.
                         */
                        .requestMatchers(
                                "/api/usuarios/**"
                        ).hasRole("ADMINISTRADOR")

                        /*
                         * REPORTES
                         */
                        .requestMatchers(
                                "/api/reportes/**"
                        ).hasRole("ADMINISTRADOR")

                        /*
                         * ATENCIONES
                         */
                        .requestMatchers(
                                "/api/atenciones/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "VETERINARIO"
                        )

                        /*
                         * MASCOTAS - CONSULTA
                         *
                         * El veterinario puede consultar mascotas
                         * para trabajar con atenciones e historial.
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/mascotas/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "RECEPCIONISTA",
                                "VETERINARIO"
                        )

                        /*
                         * MASCOTAS - REGISTRO Y MODIFICACIÓN
                         */
                        .requestMatchers(
                                "/api/mascotas/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "RECEPCIONISTA"
                        )

                        /*
                         * PROPIETARIOS
                         */
                        .requestMatchers(
                                "/api/propietarios/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "RECEPCIONISTA"
                        )

                        /*
                         * CITAS
                         */
                        .requestMatchers(
                                "/api/citas/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "RECEPCIONISTA"
                        )

                        /*
                         * Cualquier otro endpoint requiere
                         * autenticación.
                         */
                        .anyRequest()
                        .authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}