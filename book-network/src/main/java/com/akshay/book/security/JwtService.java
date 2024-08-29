package com.akshay.book.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//will perform all the necessary jwt token actions
@Service
public class JwtService {

    @Value("${jwt.expiration}")
    private  long jwtExpiration;
    @Value("${jwt.secretKey}")
    private String secretKey;

    public String extractUserName(String token) {
        return extractClaim(token,Claims::getSubject );
    }

    private <T>  T extractClaim(String token , Function<Claims,T> claimResolver) {
      final Claims claims = extractAllClaims(token);
      return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.
                parserBuilder().
                setSigningKey(getSignInKey()).
                build().
                parseClaimsJws(token).
                getBody();
    }


    public String generateToken(UserDetails userDetails) {
      return generateToken(new HashMap<>(), userDetails);
  }

    private  String generateToken( Map<String,Object> claims, UserDetails userDetails) {
        return buildToken(claims,userDetails , jwtExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long jwtExpiration) {
      var authorities = userDetails.getAuthorities().stream().
              map(GrantedAuthority::getAuthority).
              toList();

      return Jwts.builder().
              setClaims(extraClaims).
              setSubject(userDetails.getUsername()).
              setIssuedAt(new Date(System.currentTimeMillis())).
              setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)).
              claim("authorities",authorities).
              signWith(getSignInKey()).compact();

    }

    private boolean isTokenValid(String token , UserDetails userDetails) {
        final String userName = extractUserName(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getSignInKey() {
      byte [] keyBytes = Decoders.BASE64.decode(secretKey);
      return Keys.hmacShaKeyFor(keyBytes);
    }

}
