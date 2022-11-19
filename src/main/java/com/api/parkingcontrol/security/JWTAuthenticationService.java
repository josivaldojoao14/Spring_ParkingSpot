package com.api.parkingcontrol.security;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTAuthenticationService {

    /* 2 days */
    private final static long EXPIRATION_TIME = 172800000;
    /* josivaldojoao14 in MD5 Hash */
    private final static String SECRET = "d9c0985b2b5be594bd021456f460f510";
    private final static String TOKEN_PREFIX = "Bearer";
    private final static String HEADER_STRING = "Authorization";

    /* Gerando o token JWT e dando a resposta */
    public void addAuthentication(HttpServletResponse response, String username) throws Exception{

        String JWT = Jwts.builder().setSubject(username)
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact();
        
        String token = TOKEN_PREFIX + " " + JWT;

        /* Retorna a resposta/token no cabeçalho da requisição */
        response.addHeader(HEADER_STRING, token);

        /* Retorna a resposta/token no corpo da requisição */
        response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
    }

}
