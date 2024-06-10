package software.engineering.lysep.security;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class DummyJwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        @Nonnull HttpServletRequest request,
        @Nonnull HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        // Simply continue the filter chain without doing anything
        filterChain.doFilter(request, response);
    }
}
