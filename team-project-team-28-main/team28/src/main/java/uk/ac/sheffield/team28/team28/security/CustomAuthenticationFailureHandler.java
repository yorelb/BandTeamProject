package uk.ac.sheffield.team28.team28.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, 
                                        HttpServletResponse response, 
                                        AuthenticationException exception) 
                                        throws IOException, ServletException {
        // Custom error message
        String errorMessage = "Invalid username or password";

        // Check specific exception type to provide custom messages if needed
        if (exception.getMessage().equalsIgnoreCase("Bad credentials")) {
            errorMessage = "Incorrect username or password";
        }

        // Redirect to login page with the error message as a query parameter
        response.sendRedirect("/auth/login?error=" + errorMessage);
    }
}
