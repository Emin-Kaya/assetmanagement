package com.bht.assetmanagement.shared.exception;

import com.bht.assetmanagement.shared.error.ErrorMessage;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AccsessDeniedHandler implements AccessDeniedHandler {
    private static final Logger logger = LoggerFactory.getLogger(AccsessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        logger.error("Unauthorized error: {}", accessDeniedException.getMessage());
        ErrorMessage errorMessage = new ErrorMessage();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        errorMessage.setStatus(HttpStatus.UNAUTHORIZED);
        errorMessage.setMessage(accessDeniedException.getMessage());


        new JsonMapper().writeValue(response.getOutputStream(), errorMessage);
    }
}
