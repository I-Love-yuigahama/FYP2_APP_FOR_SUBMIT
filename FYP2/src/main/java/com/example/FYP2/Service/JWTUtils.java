package com.example.FYP2.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JWTUtils {


    @Value("${SecretKey}")
    private String secretString;

    private SecretKey key;

    // Access token: 15 minutes (short-lived for security)
    private static final long ACCESS_TOKEN_EXPIRATION = 900_000; // 15 minutes

    // Refresh token: 1 day (used to get new access tokens without re-login)
    private static final long REFRESH_TOKEN_EXPIRATION = 86_400_000; // 1 day

    @PostConstruct//runs once, after Spring finishes dependency injection
    public void init()
    {
//        byte[] keyBytes = Base64.getDecoder().decode((secretString).getBytes(StandardCharsets.UTF_8));
        // ✅ correct — decode base64 string directly
//        byte[] keyBytes = Base64.getDecoder().decode(secretString);


        // ✅ no base64 needed — just use raw string as bytes directly
        byte[] keyBytes = secretString.getBytes(StandardCharsets.UTF_8);

        //getBytes() makes string → bytes,
        //decode() makes Base64 bytes → raw binary bytes
        //without getDecoder i cant decode anything

        //Cryptography + key creation ;
        // Decode = turn it back to the original data
        // Encode = make data safe / readable for storage or transport
        //StandardCharsets.UTF_8 = convert characters to bytes


        this.key = new SecretKeySpec(keyBytes,"HmacSHA256");
        //SecretKeySpec is a wrapper class. that tell java These bytes represent a secret key for this algorithm
        //createa a SecretKey by using decoded bytes and algorithm HmacSHA256(A secure hashing algorithm)
    }

    public String generateToken(UserDetails userDetails)//UserDetails is a Spring Security interface
    {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(HashMap<String, Objects> claims, UserDetails userDetails)
            //(HashMap<key, VALUE>, claims(jsut a name)
    {
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token)
    {
        return extractClaims(token, Claims::getSubject); // :: method references
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction)
            //<> 是 Java 泛型语法，用来 声明或使用泛型类型参数/ declare or use a generic type。
            // <T> Generic method which mean it can be
            // any datatype String/int depend on the caller/ declare a generic type parameter for this method
            //  T returns any type

//            Function<Claims, T> = Function<X, Y>  input of type X and returns a result of type Y
            //Claims is basically a map of key-value pairs stored inside a
            // JWT token. which contain the infromation of the user such as email ,roles etc
    {
        return claimsTFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());
    }

    public boolean isTokenValid(String token, UserDetails userDetails)
    {
        final String username = extractUsername(token);

        return username.equals(userDetails.getUsername())&& !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token)
    {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

}
