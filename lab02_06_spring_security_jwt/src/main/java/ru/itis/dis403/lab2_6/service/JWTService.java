package ru.itis.dis403.lab2_6.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys; // Импортируем Keys
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey; // Используем SecretKey
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    // Убираем @Value для secretKey, будем генерировать его программно
    // @Value("${jwt.secret}")
    // private String secretKey;

    // Добавим поле для хранения сгенерированного ключа
    private final SecretKey signingKey;

    // В конструкторе генерируем ключ
    public JWTService() {
        // Генерируем ключ, подходящий для HS384
        this.signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS384);
        // ПРИМЕЧАНИЕ: В продакшене ключ лучше хранить в безопасном месте (например, в environment variable)
        // и передавать его как строку через @Value, затем декодировать.
        // Для тестирования генерация в коде приемлема, но ключ будет меняться при каждом запуске.
        // Если нужно постоянство, можно сгенерировать ключ один раз и сохранить его.
        System.out.println("--- Generated Signing Key (BASE64): " + java.util.Base64.getEncoder().encodeToString(this.signingKey.getEncoded()) + " ---");
    }

    // Если вы решите передавать ключ через application.yml, используйте этот конструктор:
    /*
    public JWTService(@Value("${jwt.secret}") String secretKeyBase64) {
        // Декодируем строку BASE64 в массив байт, затем создаем ключ
        // Убедитесь, что secretKeyBase64 - это строка длиной 64 символа, представляющая 48 байт
        byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(secretKeyBase64);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }
    */


    // Изменяем методы, используя поле signingKey
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // Используем жестко закодированное значение или @Value
                .signWith(signingKey, SignatureAlgorithm.HS384) // <-- Используем сгенерированный ключ и HS384
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey) // <-- Используем сгенерированный ключ
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}