package den.gubarev.springproject.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import den.gubarev.springproject.security.JWTUtil;
import den.gubarev.springproject.security.ResidentDetails;
import den.gubarev.springproject.services.ResidentDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final ResidentDetailsService residentDetailsService;

    @Autowired
    public JWTFilter(JWTUtil jwtUtil, ResidentDetailsService residentDetailsService) {
        this.jwtUtil = jwtUtil;
        this.residentDetailsService = residentDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);

                if (jwt.isBlank()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token in Bearer Header");
                } else {
                    try {
                        String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);

                        UserDetails residentDetails = residentDetailsService.loadUserByUsername(username);

                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        residentDetails,
                                        residentDetails.getPassword(),
                                        residentDetails.getAuthorities()
                                );

                        if (!(SecurityContextHolder.getContext().getAuthentication() == null)) {
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        }
                    } catch (JWTVerificationException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token");
                    }
                }
            }

        filterChain.doFilter(request,response);
    }

}
