package com.pd.expense_tracker.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import com.auth0.jwt.JWT;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class OAuthTokenValidationFilter implements Filter {

    private JwkProvider jwkProvider;
    private final String issuer = "https://dev-1ynmzjaba3fk1xkn.us.auth0.com";
    private final String audience = "https://dev-1ynmzjaba3fk1xkn.us.auth0.com/api/v2/";

    public OAuthTokenValidationFilter() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        if (path.startsWith("/h2-console") || path.startsWith("/swagger-ui")  || path.startsWith("/v3/api-docs") || path.startsWith("/swagger-resources")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);

        boolean valid = validateToken(token);


        if (valid) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken("externalUser", null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean validateToken(String token) throws IOException {

        this.jwkProvider = new JwkProviderBuilder(issuer)
                .cached(10, 24, TimeUnit.HOURS)
                .build();
        try {

            var decodedJwt = JWT.decode(token);
            String kid = decodedJwt.getKeyId();

            Jwk jwk = jwkProvider.get(kid);
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .build();

            verifier.verify(token);  // throws if invalid

        } catch (Exception e) {

        }

        return true;
    }

}
