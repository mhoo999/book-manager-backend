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


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final jwtTokenProvider jwtTokenProvider;

    @Autowired
    public JwtAuthenticationFilter(jwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * JWT 필터를 적용하지 않을 경로들을 지정
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // admin 경로는 세션 기반 인증이므로 JWT 필터 제외
        if (path.startsWith("/admin/")) {
            return true;
        }

        // 정적 리소스도 JWT 필터 제외
        if (path.startsWith("/images/") ||
                path.startsWith("/uploads/") ||
                path.startsWith("/css/") ||
                path.startsWith("/js/") ||
                path.startsWith("/favicon.ico")) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final Claims claims;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);
        claims = jwtTokenProvider.getAllClaimsFromToken(jwtToken);
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