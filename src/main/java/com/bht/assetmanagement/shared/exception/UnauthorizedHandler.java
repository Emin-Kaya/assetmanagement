package com.bht.assetmanagement.shared.exception;

import com.bht.assetmanagement.shared.error.ErrorMessage;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UnauthorizedHandler implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(UnauthorizedHandler.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        logger.error("Unauthorized error: {}", authException.getMessage());
        ErrorMessage errorMessage = new ErrorMessage();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        errorMessage.setStatus(HttpStatus.UNAUTHORIZED);
        errorMessage.setMessage(authException.getMessage());

        new JsonMapper().writeValue(response.getOutputStream(), errorMessage);

    }
}
