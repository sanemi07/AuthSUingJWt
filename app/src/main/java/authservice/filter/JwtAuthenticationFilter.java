package authservice.filter;

import authservice.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        // Extract Authorization header
        String header = httpServletRequest.getHeader("Authorization");

        // Check if the header contains a Bearer token
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // Extract token without the "Bearer" part

            // Validate the token
            if (jwtUtils.validateToken(token)) {
                String username = jwtUtils.getUsernameFromToken(token);

                // If token is valid and user is not authenticated yet
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // Create authentication token without credentials (null for authorities for now)
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            username, null, Collections.emptyList());

                    // Set authentication in the SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        // Continue with the next filter in the chain
        chain.doFilter(request, response);
    }
}

