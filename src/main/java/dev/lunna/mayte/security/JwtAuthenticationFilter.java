package dev.lunna.mayte.security;

import dev.lunna.mayte.database.model.User;
import dev.lunna.mayte.database.repository.UserRepository;
import dev.lunna.mayte.exception.TokenExpiredException;
import dev.lunna.mayte.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserRepository userRepository;

  public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
    this.jwtService = jwtService;
    this.userRepository = userRepository;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    final String jwt = authHeader.substring(7);

    try {
      final String email = jwtService.extractEmail(jwt);

      if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent() && jwtService.isTokenValid(jwt, email)) {
          User user = userOptional.get();
          final AuthenticatedUser authenticatedUser = new AuthenticatedUser(user.getId(), userRepository);

          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              authenticatedUser,
              null,
              user.getAuthorities()
          );
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
    } catch (TokenExpiredException e) {
      // Handled by GlobalExceptionHandler
    } catch (Exception e) {
      logger.error("Error while processing JWT token: {}", e.getCause());
    }

    filterChain.doFilter(request, response);
  }
}