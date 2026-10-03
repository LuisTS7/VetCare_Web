package com.vetcare.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtService(
            @Value("${vetcare.jwt.secret}") String secret,
            @Value("${vetcare.jwt.expiration}") long expiration) {

        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

        this.expiration = expiration;
    }

    public String generarToken(
            Long usuarioId,
            String correo,
            String rol) {

        Date ahora = new Date();
        Date vencimiento = new Date(
                ahora.getTime() + expiration
        );

        return Jwts.builder()
                .subject(correo)
                .claim("usuarioId", usuarioId)
                .claim("rol", rol)
                .issuedAt(ahora)
                .expiration(vencimiento)
                .signWith(secretKey)
                .compact();
    }

    public String obtenerCorreo(String token) {
        return obtenerClaims(token).getSubject();
    }

    public String obtenerRol(String token) {
        return obtenerClaims(token)
                .get("rol", String.class);
    }

    public Long obtenerUsuarioId(String token) {

        Number usuarioId = obtenerClaims(token)
                .get("usuarioId", Number.class);

        return usuarioId.longValue();
    }

    public boolean esTokenValido(String token) {

        try {
            Claims claims = obtenerClaims(token);

            return claims.getExpiration()
                    .after(new Date());

        } catch (Exception e) {
            return false;
        }
    }

    private Claims obtenerClaims(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}