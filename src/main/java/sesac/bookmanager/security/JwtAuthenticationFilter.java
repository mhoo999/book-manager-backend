package sesac.bookmanager.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sesac.bookmanager.user.data.User;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final jwtTokenProvider jwtTokenProvider;
    private static final List<String> WHITELIST = List.of(
            "/", "/home", "/api/auth", "/api/notice", "/api/question", "/api/reply", "/api/v1/books"
    );

    @Autowired
    public JwtAuthenticationFilter(jwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        /* 해당 경로는 토큰 없이 필터를 건너뛰도록 적용*/
        final String path = request.getServletPath();
        boolean isWhitelisted = WHITELIST.stream().anyMatch(path::startsWith);

        if (isWhitelisted) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final Claims claims;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        /* 유효하지 않은 토큰은 401 에러 응답*/
        try {
            jwtToken = authHeader.substring(7);
            claims = jwtTokenProvider.getAllClaimsFromToken(jwtToken);
        } catch (Exception ex) {
            // 유효하지 않은 토큰이면 그냥 인증 없이 진행하거나
            // 혹은 401 에러로 응답
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
            return;
        }

        final int userId = Integer.parseInt(claims.getSubject());
        final String userEmail = claims.get("email", String.class);
        final String userName = claims.get("name",String.class);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetails customUserDetails = new CustomUserDetails(new User(userId, userEmail, userName));
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                customUserDetails, null, Collections.emptyList());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        filterChain.doFilter(request, response);

    }
}
