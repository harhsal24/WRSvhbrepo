package com.hb.WRSvhb.config.authdtos.authentication;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<String> handleError(HttpServletRequest request) {
        // Customize the response as needed
        HttpStatus status = getStatus(request);
        String errorMessage = "An error occurred. Please check your request and try again.";

        return new ResponseEntity<>(errorMessage, status);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode != null) {
            try {
                return HttpStatus.valueOf(statusCode);
            } catch (Exception ex) {
                // Handle invalid status code
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }


    public String getErrorPath() {
        return "/error";
    }
}
