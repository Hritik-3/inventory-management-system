//package com.sboot.security;

//

//import io.jsonwebtoken.*;

//import org.springframework.stereotype.Component;

//

//import java.util.Date;

//

//@Component

//public class JWTUtil {

//    private final String secret = "secret_key_example";  // Use env var in real apps

//    private final long jwtExpiration = 24 * 60 * 60 * 1000; // 1 day

//

//    public String generateToken(String username) {

//        return Jwts.builder()

//                .setSubject(username)

//                .setIssuedAt(new Date())

//                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))

//                .signWith(SignatureAlgorithm.HS256, secret)

//                .compact();

//    }

//

//    public String extractUsername(String token) {

//        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();

//    }

//

//    public boolean validateToken(String token) {

//        try {

//            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

//            return true;

//        } catch (JwtException | IllegalArgumentException e) {

//            return false;

//        }

//    }

//}
 
 
 
package com.boot.ordercraft.security;
 
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;
 
import javax.crypto.SecretKey;

import java.util.Date;

 
@Component

public class JWTUtil {
 
    private SecretKey key;
 
    @PostConstruct

    public void init() {

        // Ensure this key is at least 256 bits (32 characters)

        String secret = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbHZhQGV4YW1wbGUuY29tIiwiaWF0IjoxNzE4NzA4MDAwLCJleHAiOjE3MTg3NDQwMDB9.aAnxF64DQIIOBNzHvPS9IvCKRL5oT-h2I1e1rrgV7Fs\r\n"

        		+ "";

        this.key = Keys.hmacShaKeyFor(secret.getBytes());

    }
 
    public String generateToken(String email) {

        return Jwts.builder()

        		.setSubject(email)

                .setIssuedAt(new Date())

                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours

                .signWith(key, SignatureAlgorithm.HS256)

                .compact();

    }
 
    public boolean validateToken(String token) {

        try {

            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

            return true;

        } catch (Exception e) {

            return false;

        }

    }
 
    public String extractUsername(String token) {

        return Jwts.parserBuilder()

                .setSigningKey(key)

                .build()

                .parseClaimsJws(token)

                .getBody()

                .getSubject();

    }

}
 
 